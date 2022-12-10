package com.lp.client.rechtschreibung;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.WoerterbuchEintragDto;
import com.lp.util.FileDeleteVisitor;

import dk.dren.hunspell.Hunspell;
import dk.dren.hunspell.Hunspell.Dictionary;

public class HunspellDictionaryHandler {
	private static final String DICTIONARY_BASE = "/com/lp/client/res/dictionaries/";
	private Path tempDir;
	private Map<String, Optional<Path>> tempDictsForLocales;

	private Logger logger = LpLogger.getLogger("Rechtschreibpruefung");

	public HunspellDictionaryHandler() {
		tempDictsForLocales = new ConcurrentHashMap<>();
		try {
			tempDir = Files.createTempDirectory("hv_woerterbuch");
			Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
		} catch (IOException e) {
			logger.error("Fehler beim erstellen des Woerterbuch-Verzeichnisses", e);
		}
	}

	private void onShutdown() {
		try {
			Files.walkFileTree(tempDir, new FileDeleteVisitor());
			Files.deleteIfExists(tempDir);
			tempDir = null;
			tempDictsForLocales.clear();
		} catch (IOException e) {
		}
	}

	public List<Dictionary> getDictsForLanguage(Locale loc) {
		List<Dictionary> list = new ArrayList<>();
		checkTempDir(loc);
		Optional<Dictionary> mainDict = getDictionaryIfPathExists(getDictionaryPath(loc));
		Optional<Dictionary> customLoc = getDictionaryIfPathExists(loadCustomWordsForLocale(loc.toString()));
		Optional<Dictionary> customAll = getDictionaryIfPathExists(
				loadCustomWordsForLocale(LocaleFac.WOERTERBUCH_LOCALE_ALL));
		// Wenn kein mainDict vorhanden, soll gar keine Rechtschreibpruefung passieren
		if (mainDict.isPresent()) {
			mainDict.ifPresent(list::add);
			customLoc.ifPresent(list::add);
			customAll.ifPresent(list::add);
		}
		return list;
	}

	/**
	 * Check if the cached Files for the Locale exist, if not it invalides the cache
	 * and causes getDictionaryPath to recreate it
	 * 
	 * @param loc
	 */
	private void checkTempDir(Locale loc) {
		if (tempDir != null && Files.notExists(tempDir)) {
			try {
				tempDir = Files.createTempDirectory("hv_woerterbuch");
			} catch (IOException e) {
				tempDir = null;
				logger.error("Fehler beim erstellen des Woerterbuch-Verzeichnisses", e);
			}
			tempDictsForLocales.clear();
		} else {
			Iterator<Entry<String, Optional<Path>>> iter = tempDictsForLocales.entrySet().iterator();
			while (iter.hasNext()) {
				Optional<Path> filename = iter.next().getValue();
				if (!filename.isPresent()) {
					continue;
				}
				Path basename = filename.get();
				Path dicfile = Paths.get(basename.toString().concat(".dic"));
				Path afffile = Paths.get(basename.toString().concat(".aff"));
				if (Files.notExists(dicfile) || Files.notExists(afffile)) {
					iter.remove();
				}
			}
		}
	}

	private Optional<Dictionary> getDictionaryIfPathExists(Optional<Path> path) {
		if (path.isPresent()) {
			try {
				Dictionary dict = Hunspell.getInstance().getDictionary(path.get().toString(), true);
				return Optional.of(dict);
			} catch (FileNotFoundException | UnsupportedEncodingException | UnsatisfiedLinkError
					| UnsupportedOperationException e) {
				logger.warn("Exception in Hunspell.getDictionary()", e);
				return Optional.empty();
			}
		} else {
			return Optional.empty();
		}
	}

	private Optional<Path> getDictionaryPath(Locale loc) {
		Optional<Path> cached = tempDictsForLocales.get(loc.toString());
		if (cached != null) {
			return cached;
		}
		String base_name = DICTIONARY_BASE + loc.getLanguage() + "/" + loc.toString();
		try (InputStream stream1 = getClass().getResourceAsStream(base_name + ".dic");
				InputStream stream2 = getClass().getResourceAsStream(base_name + ".aff")) {
			if (stream1 != null && stream2 != null) {
				Files.copy(stream1, tempDir.resolve(loc.toString() + ".dic"));
				Files.copy(stream2, tempDir.resolve(loc.toString() + ".aff"));
				Path path = tempDir.resolve(loc.toString()).toAbsolutePath();
				Optional<Path> opt = Optional.of(path);
				tempDictsForLocales.put(loc.toString(), opt);
				return opt;
			}
		} catch (IOException e) {
		}
		// Bei exception und wenn im try kein return ausgefuehrt wird
		logger.warn("Kein Woerterbuch fuer Sprache " + loc.toString() + " vorhanden");
		tempDictsForLocales.put(loc.toString(), Optional.empty());
		return Optional.empty();
	}

	/**
	 * L&auml;dt eine Liste von W&ouml;rtern aus der Datenbank und erstellt ein
	 * nicht affix behaftetes W&ouml;rterbuch
	 * 
	 * @param localeName
	 * @return
	 */
	private Optional<Path> loadCustomWordsForLocale(String localeName) {
		String localeStr = localeName + "_custom";
		Optional<Path> cached = tempDictsForLocales.get(localeStr);
		if (cached != null) {
			return cached;
		}
		try {
			List<WoerterbuchEintragDto> words = DelegateFactory.getInstance().getLocaleDelegate()
					.getAllWoerterbuchEintragZuSprache(localeName);
			Path affFile = tempDir.resolve(localeStr + ".aff");
			Files.createFile(affFile);
			Path dicFile = tempDir.resolve(localeStr + ".dic");
			try (BufferedWriter writer = Files.newBufferedWriter(dicFile, StandardOpenOption.CREATE)) {
				int size = words.size();
				// Size ist nur ungefaehre Anzahl an Woertern, darf aber nicht 0 sein
				size = Math.max(size, 1);
				writer.write(String.valueOf(size));
				writer.newLine();
				for (WoerterbuchEintragDto eintrag : words) {
					String word = eintrag.getWort();
					writer.write(word);
					writer.newLine();
				}
			}
			Path basePath = tempDir.toAbsolutePath().resolve(localeStr);
			Optional<Path> opt = Optional.of(basePath);
			tempDictsForLocales.put(localeStr, opt);
			return opt;
		} catch (Throwable e) {
			logger.warn("Kein Woerterbuch fuer Sprache " + localeName + " vorhanden", e);
			tempDictsForLocales.put(localeName, Optional.empty());
			return Optional.empty();
		}
	}

	public void reloadCustomWords() {
		tempDictsForLocales.clear();
	}
}
