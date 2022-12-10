package com.lp.client.frame.filechooser;

import net.sf.jasperreports.engine.JasperPrint;


public interface ChooserSaveDialogJasperFilter extends ChooserSaveDialogJasper, ChooserDialogFilterSelection<ChooserSaveDialogJasper>, ChooserDialogEnd {

	WrapperSaveFileChooser<JasperPrint> build();

}
