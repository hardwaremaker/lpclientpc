package com.lp.client.frame.dialog;

import java.math.BigDecimal;

/**
 * <p>
 * Die Klasse speichert mehrere Variablen und dient als Rückgabewert der Funktion <i>pruefeUnterpreisigkeitUndMindestVKMengeDlg</i>:
 * </p>
 * <p>
 * Erstellung: Andreas Daum, 22.07.2020
 * </p>
 * @author andreas
 *	
 */
public class ArtikelMengenDialogRueckgabe {
	private boolean store;
	private BigDecimal amount;
	private boolean changed;
	
	/**
	 * Konstruktor falls <i>amount</i> nicht geändert wurde, <i>changed</i> wird auf false gesetzt
	 * @param store
	 * @param amount
	 */
	public ArtikelMengenDialogRueckgabe(boolean store, BigDecimal amount) {
		this(store, amount, false);
	}
	/**
	 * Falls <i>amount</i> geändert wurde, sollte <i>changed</i> auf true gesetzt werden
	 * @param store
	 * @param amount
	 * @param changed
	 */
	public ArtikelMengenDialogRueckgabe(boolean store, BigDecimal amount, boolean changed) {
		this.store = store;
		this.amount = amount;
		this.changed = changed;
	}
	
	public boolean isStore() {
		return store;
	}

	public void setStore(boolean store) {
		this.store = store;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * Wenn <i>amount</i> geändert wird, sollte <i>changed</i> auf true gesetzt werden
	 * @param amount
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
