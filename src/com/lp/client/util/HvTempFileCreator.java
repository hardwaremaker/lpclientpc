package com.lp.client.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

import com.lp.util.FileDeleteVisitor;

/**
 * Utility Klasse, mit der tempor&auml;re Dateien erstellt werden k&ouml;nnen,
 * die bei schlie&szlig;en des Programmes wieder gel&ouml;scht werden
 * 
 * @author Alexander Daum
 *
 */
public class HvTempFileCreator {
	private static List<Path> files;
	private static List<Path> folders;

	static {
		files = new ArrayList<Path>();
		folders = new ArrayList<Path>();
		Runtime.getRuntime().addShutdownHook(new Thread(HvTempFileCreator::deleteAllFiles));
	}

	/**
	 * L&ouml;scht alle erstellten Dateien
	 */
	private static void deleteAllFiles() {
		for (Path file : files) {
			try {
				Files.deleteIfExists(file);
			} catch (IOException e) {
			}
		}
		for (Path dir : folders) {
			try {
				Files.walkFileTree(dir, new FileDeleteVisitor());
			} catch (IOException e) {
			}
		}
	}

	public static Path createTempFile(String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
		Path newFile = Files.createTempFile(prefix, suffix, attrs);
		files.add(newFile);
		return newFile;
	}

	public static Path createTempDir(String prefix, FileAttribute<?>... attrs) throws IOException {
		Path newDir = Files.createTempDirectory(prefix, attrs);
		folders.add(newDir);
		return newDir;
	}
	
	public static void addFileToDelete(Path file) {
		files.add(file);
	}
}
