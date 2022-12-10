package com.lp.service.csvverbrauch;

import com.lp.server.fertigung.service.errors.ImportException;
import com.lp.server.fertigung.service.errors.NoFertigungsgruppeException;
import com.lp.server.fertigung.service.errors.NoKostenstelleException;
import com.lp.server.fertigung.service.errors.NoLoslagerException;
import com.lp.server.fertigung.service.errors.NoMontageartException;
import com.lp.server.fertigung.service.errors.NoOrUnknownDefaultArticleException;
import com.lp.server.fertigung.service.errors.NoSuchArticleException;
import com.lp.server.fertigung.service.errors.NoSuchColumnException;
import com.lp.server.fertigung.service.errors.NotEnoughInStockException;
import com.lp.server.fertigung.service.errors.NumberOfColumnsException;
import com.lp.server.fertigung.service.errors.ParseDateFormatException;
import com.lp.server.fertigung.service.errors.ParseNumberFormatException;

public class CsvErrorFactory {

	public ICsvErrorAction getCsvErrorAction(NoSuchArticleException exception) {
		return new CsvErrorNoSuchArticle(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(NoSuchColumnException exception) {
		return new CsvErrorNoSuchColumn(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(NotEnoughInStockException exception) {
		return new CsvErrorNotEnoughInStock(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(NumberOfColumnsException exception) {
		return new CsvErrorNumberOfColumns(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(ParseDateFormatException exception) {
		return new CsvErrorParseDateFormat(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(ParseNumberFormatException exception) {
		return new CsvErrorParseNumberFormat(exception);
	}

	public ICsvErrorAction getCsvErrorAction(NoFertigungsgruppeException exception) {
		return new CsvErrorNoFertigungsgruppe(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(NoKostenstelleException exception) {
		return new CsvErrorNoKostenstelle(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(NoLoslagerException exception) {
		return new CsvErrorNoLoslager(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(NoMontageartException exception) {
		return new CsvErrorNoMontageart(exception);
	}
	
	public ICsvErrorAction getCsvErrorAction(NoOrUnknownDefaultArticleException exception) {
		return new CsvErrorNoOrUnknownDefaultArticle(exception);
	}

	public ICsvErrorAction getCsvErrorAction(ImportException exception) {
		if (exception instanceof NoSuchArticleException) {
			return getCsvErrorAction((NoSuchArticleException)exception);
		} else if (exception instanceof NoSuchColumnException) {
			return getCsvErrorAction((NoSuchColumnException)exception);
		} else if (exception instanceof NotEnoughInStockException) {
			return getCsvErrorAction((NotEnoughInStockException)exception);
		} else if (exception instanceof NumberOfColumnsException) {
			return getCsvErrorAction((NumberOfColumnsException)exception);
		} else if (exception instanceof ParseDateFormatException) {
			return getCsvErrorAction((ParseDateFormatException)exception);
		} else if (exception instanceof ParseNumberFormatException) {
			return getCsvErrorAction((ParseNumberFormatException)exception);
		} else if (exception instanceof NoFertigungsgruppeException) {
			return getCsvErrorAction((NoFertigungsgruppeException)exception);
		} else if (exception instanceof NoKostenstelleException) {
			return getCsvErrorAction((NoKostenstelleException)exception);
		} else if (exception instanceof NoLoslagerException) {
			return getCsvErrorAction((NoLoslagerException)exception);
		} else if (exception instanceof NoMontageartException) {
			return getCsvErrorAction((NoMontageartException)exception);
		} else if (exception instanceof NoOrUnknownDefaultArticleException) {
			return getCsvErrorAction((NoOrUnknownDefaultArticleException)exception);
		}
		
		return new CsvErrorUnbekannt(exception);
	}
}
