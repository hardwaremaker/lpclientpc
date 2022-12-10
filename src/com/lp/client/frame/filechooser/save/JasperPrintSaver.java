package com.lp.client.frame.filechooser.save;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRSaveContributor;

public class JasperPrintSaver<T extends JRSaveContributor> implements IJasperPrintSaver {
	
	private Class<T> contributorClass;
	private Locale locale;
	
	public JasperPrintSaver(Class<T> contributorClass, Locale locale) {
		this.contributorClass = contributorClass;
		this.locale = locale;
	}

	private T getSaveContributor() {
		try {
			Constructor<T> constructor = contributorClass.getConstructor(new Class[] { Locale.class,
					ResourceBundle.class });
			T contributor = constructor.newInstance(new Object[] {locale, null});
			return contributor;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void save(JasperPrint jrPrint, File file) throws JRException {
		T contributor = getSaveContributor();
		if (contributor == null) return;
		
		contributor.save(jrPrint, file);
	}

}
