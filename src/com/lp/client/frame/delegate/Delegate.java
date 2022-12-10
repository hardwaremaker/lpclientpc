/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.frame.delegate;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.jboss.tm.JBossRollbackException;
import org.postgresql.util.PSQLException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.UstWarnungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.BelegPruefungDto.RePosInfo;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * <I>Basisklasse fuer alle BusinessDelegates.</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Uli Walch <I>18.10.04</I>
 * </p>
 * 
 * codeverantwortlich: Martin Bluehweis
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.17 $
 * 
 *          Date $Date: 2011/03/28 14:30:40 $
 */

public class Delegate {

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(getClass());

	/**
	 * Konstruktor.
	 */
	public Delegate() {
		// nothing here
	}

	protected <T> T lookupFac(Context context, Class<T> callInterface) {
		try {
			return (T) context
					.lookup("lpserver/ejb/" + callInterface.getSimpleName() + "Bean!" + callInterface.getName());
		} catch (Throwable t) {
			t.printStackTrace();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	public Double rabattAusRechnungsadresse(Integer iIdAuftragI) throws Throwable {
		AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(iIdAuftragI);

		double dAuftragsrabatt = auftragDto.getFAllgemeinerRabattsatz() + auftragDto.getFProjektierungsrabattsatz();

		KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(auftragDto.getKundeIIdRechnungsadresse());
		Double d = null;
		if (kdDto.getFRabattsatz() != null) {
			double dKundenRabatt = kdDto.getFRabattsatz();

			if (dAuftragsrabatt != dKundenRabatt) {

				int iAuftrag = 0;
				int iRechnungsadresse = 1;
				int iAnzahlOptionen = 2;

				Object[] aOptionen = new Object[iAnzahlOptionen];
				aOptionen[iAuftrag] = LPMain.getTextRespectUISPr("auft.auftrag");
				aOptionen[iRechnungsadresse] = LPMain.getTextRespectUISPr("lp.rechnungsadresse");

				int iAuswahl = DialogFactory.showModalDialog(null,
						LPMain.getTextRespectUISPr("lp.error.rabatt.uebernahme"),
						LPMain.getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[0]);

				if (iAuswahl == iRechnungsadresse) {
					return dKundenRabatt;
				}

			}
		}

		return d;
	}

	/**
	 * Zentrale Methode zur Behandlung von Server Ausnahmen. delegateexc: 6 hier
	 * wird verarbeitet
	 * 
	 * @param t Throwable
	 * @throws ExceptionLP
	 */
	public void handleThrowable(Throwable t) throws ExceptionLP {
		// delegateexc: 7 ExceptionLP direkt wieder rausgeben

		myLogger.error(t.getMessage(), t);

		if (t instanceof ExceptionLP) {
			throw (ExceptionLP) t;
		}
		// exccatch: hier wird ausgepackt.
		if (t instanceof RuntimeException) {
			RuntimeException reI = (RuntimeException) t;
			// Throwable t2 = reI.getCause();
			// if (t2 != null && t2 instanceof ServerException) {
			Throwable t3 = reI.getCause();

			if (t3 != null && t3.getCause() != null && t3.getCause() instanceof ConstraintViolationException) {

				ConstraintViolationException e = (ConstraintViolationException) t3.getCause();

				if (e.getSQLException() != null && e.getSQLException().getMessage() != null) {
						
					ArrayList al = new ArrayList();

					String message = e.getSQLException().getMessage();

					if (message.contains("\u00bb")) {
						// POSTGRES
						String[] teile = message.split("\u00bb");

						if (teile.length > 2) {
							int i = teile[2].indexOf("\u00ab");
							if (i > 0) {
								al.add(teile[2].substring(0, i));
							}
						}
						throw new ExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e.getSQLException().getMessage(), al,
								t3.getCause());
					} else if (message.contains("\"")) {
						// MSSQL
						String[] teile = message.split("\"");

						if (teile.length > 1) {

							al.add(teile[1].toLowerCase());
						}
						throw new ExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e.getSQLException().getMessage(), al,
								t3.getCause());
					}else if (message.contains("'")) {
						// MSSQL SP8754
						String[] teile = message.split("'");

						if (teile.length > 1) {

							al.add(teile[1].toLowerCase());
						}
						throw new ExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e.getSQLException().getMessage(), al,
								t3.getCause());
					}

				}

				if (e.getConstraintName() != null) {
					// POSTGRES
					ArrayList al = new ArrayList();
					al.add(e.getConstraintName());
					throw new ExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e.getConstraintName(), al,
							t3.getCause());
				}

			}

			if (t3 instanceof EJBExceptionLP) {
				EJBExceptionLP ejbt4 = (EJBExceptionLP) t3;
				if (ejbt4 instanceof EJBExceptionLP) {
					Throwable ejbt5 = ejbt4.getCause();
					if (ejbt5 instanceof EJBExceptionLP) {
						// wegen zB. unique key knaller
						EJBExceptionLP ejbt6 = (EJBExceptionLP) ejbt5;
						throw new ExceptionLP(ejbt6.getCode(), ejbt6.getMessage(), ejbt6.getAlInfoForTheClient(),
								ejbt6.getCause());
					} else if (ejbt5 != null) {
						Throwable ejbt7 = ejbt5.getCause();
						if (ejbt7 != null && ejbt7 instanceof EJBExceptionLP) {
							// zB. fuer WARNUNG_KTO_BESETZT
							EJBExceptionLP ejbt8 = (EJBExceptionLP) ejbt7;
							throw new ExceptionLP(ejbt8.getCode(), ejbt8.getMessage(), ejbt8.getAlInfoForTheClient(),
									ejbt8.getCause());
						} else {
							throw new ExceptionLP(ejbt4.getCode(), ejbt4.getMessage(), ejbt4.getAlInfoForTheClient(),
									ejbt4.getCause(), ejbt4.getExceptionData());
						}
					} else {
						throw new ExceptionLP(ejbt4.getCode(), ejbt4.getMessage(), ejbt4.getAlInfoForTheClient(),
								ejbt4.getCause());
					}
				}
			} else if (reI instanceof EJBExceptionLP) {
				EJBExceptionLP exc = (EJBExceptionLP) reI;
				throw new ExceptionLP(exc.getCode(), exc.getMessage(), exc.getAlInfoForTheClient(), exc.getCause(),
						exc.getExceptionData());
			}
			// }
			else if (t3 instanceof EJBExceptionLP) {
				// MB 13. 03. 06 wird ausgeloest, wenn belegnummern ausserhalb
				// des gueltigen bereichs generiert werden
				// (liegt vermutlich am localen interface des BN-Generators)
				EJBExceptionLP ejbt6 = (EJBExceptionLP) t3;
				throw new ExceptionLP(ejbt6.getCode(), ejbt6.getMessage(), ejbt6.getAlInfoForTheClient(),
						ejbt6.getCause());
			} else if (t3 instanceof java.io.InvalidClassException) {
				// zB. unique key knaller.
				java.io.InvalidClassException ejb = (java.io.InvalidClassException) t3;
				throw new ExceptionLP(EJBExceptionLP.FEHLER_BUILD_CLIENT, ejb.getMessage(), null, ejb.getCause());
			} else if (t3 instanceof java.lang.NoClassDefFoundError) {
				// zB. unique key knaller.
				java.lang.NoClassDefFoundError ejb = (java.lang.NoClassDefFoundError) t3;
				throw new ExceptionLP(EJBExceptionLP.FEHLER_NOCLASSDEFFOUNDERROR, ejb.getMessage(), null,
						ejb.getCause());
			} else if (t3 instanceof PersistenceException) {
				// WILDFLY
				PersistenceException ejb = (PersistenceException) t3;
				if (ejb.getCause() instanceof ConstraintViolationException) {
					throw new ExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ejb.getMessage(), null, ejb.getCause());
				}
			}

		}
		if (t instanceof java.lang.IllegalStateException) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_TRANSACTION_TIMEOUT, t);
		}
		if (t instanceof EJBException) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_EJB, t.getMessage(), null, t.getCause());
		}
		if (t.getCause() != null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t.getMessage(), null, t.getCause());
		} else {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t.getMessage(), null, t);
		}
	}

	public void dialogBelegpruefung(BelegPruefungDto pruefungDto) {
		try { // SP3983
			if (DelegateFactory.getInstance().getParameterDelegate().getKundenPositionskontierung())
				return;
		} catch (Throwable e) {
		}

		if (pruefungDto.isKundeHatKeineUstAberUstPositionen()) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("lp.vkbelege.error.kundesteuerfrei"));
		}

		if (pruefungDto.isKundeHatUstAberNichtUstPositionen()) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("lp.vkbelege.error.kundesteuerpflichtig"));
		}

		if (pruefungDto.getZeroPriceRePosInfos().size() > 0) {
			String info = "";
			List<RePosInfo> reposInfos = pruefungDto.getZeroPriceRePosInfos();
			for (RePosInfo rePosInfo : reposInfos) {
				if (info.length() > 0) {
					info += ", ";
				}

				info += LPMain.getMessageTextRespectUISPr("lp.vkbelege.error.position0preisreposinfo",
						rePosInfo.getLsposIds().isEmpty() ? rePosInfo.getReposNr() : rePosInfo.getLsCnr());

//				info += "Pos";
				if (!rePosInfo.getLsposIds().isEmpty()) {
//					info += " " + rePosInfo.getLsCnr();
//					info += " [LS-Position(en): ";
					String lsInfo = "";
					for (Integer lsposId : rePosInfo.getLsposNrs()) {
						if (lsInfo.length() > 0) {
							lsInfo += ", ";
						}
						lsInfo += "" + lsposId;
					}
					info += LPMain.getMessageTextRespectUISPr("lp.vkbelege.error.position0preislsposinfo", lsInfo);
//				} else {
//					info += " " + rePosInfo.getReposNr();
				}

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
						LPMain.getMessageTextRespectUISPr("lp.vkbelege.error.position0preis", info));
			}
		}

		if (pruefungDto.getUstWarnungDtos().size() > 0) {
			String info = "";
			for (UstWarnungDto ustWarnungDto : pruefungDto.getUstWarnungDtos()) {
				String token = null;
				if (ustWarnungDto.hasKeineSteuerObwohlErwartet()) {
					token = "lp.vkbelege.error.keineustobwohlerwartet";
				}
				if (ustWarnungDto.hasSteuerbetragObwohlNichtErwartet()) {
					token = "lp.vkbelege.error.ustobwohlkeineerwartet";
				}
				try {
					KontoDto basisKonto = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKey(ustWarnungDto.getKontoId());
					KontoDto uebersetztKontoDto = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKey(ustWarnungDto.getKontoUebersetztId());
					String msg = LPMain.getMessageTextRespectUISPr(token, uebersetztKontoDto.getCNr(),
							uebersetztKontoDto.getCBez(), basisKonto.getCNr());
					info += "\n" + msg;
				} catch (ExceptionLP e) {
					info += "\n" + "Ex:" + e.getMessage();
				}
			}

			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getMessageTextRespectUISPr("lp.vkbelege.error.ust", info));
		}
	}
}
