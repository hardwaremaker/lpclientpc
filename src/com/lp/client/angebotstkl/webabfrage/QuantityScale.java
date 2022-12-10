package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuantityScale {
	
	private List<PartPrice> prices;
	private Integer selectedPriceIndex;

	public QuantityScale(List<PartPrice> prices) {
		this.prices = prices;
		this.selectedPriceIndex = -1;
//		sort();
		fillQuantityTo();
	}
	
	/**
	 * Sortiert die Preise in aufsteigender Reihenfolge
	 */
	private Comparator<PartPrice> getPartPriceComparator() {
		return new Comparator<PartPrice>() {
			@Override
			public int compare(PartPrice price1, PartPrice price2) {
				if (price1.getQuantityFrom() == null) return 1;
				if (price2.getQuantityFrom() == null) return -1;
				
				return price1.getQuantityFrom().compareTo(price2.getQuantityFrom());
			}
		};
	}

	/**
	 * Setzt die Bis-Menge der Mengenstaffel
	 */
	private void fillQuantityTo() {
		List<PartPrice> temp = new ArrayList<PartPrice>(prices);
		Collections.sort(temp, getPartPriceComparator());
		
		for (int i = 0; i < temp.size()-1; i++) {
			temp.get(i).setQuantityTo(temp.get(i+1).getQuantityFrom());
		}
	}

	public List<PartPrice> getPrices() {
		if (prices == null) {
			prices = new ArrayList<PartPrice>();
		}
		return prices;
	}

	/**
	 * Setzt den Preis fuer die angegebene Menge ueber den {@link selectedPriceIndex}
	 * 
	 * @param quantity gegebene Menge
	 */
	public void setPriceByQuantity(BigDecimal quantity) {
		selectedPriceIndex = getPrices().indexOf(getPriceByQuantity(quantity));
	}
	
	public PartPrice getSelectedPrice() {
		return selectedPriceIndex < 0 ? null : prices.get(selectedPriceIndex);
	}

	/**
	 * Liefert den Preis fuer die angegebene Menge.
	 * 
	 * @param quantity gegebene Menge
	 * @return Preis fuer die gegebene Menge. Falls Menge unter der Mengenstaffel liegt,
	 * wird der erste zurueckgeliefert
	 */
	public PartPrice getPriceByQuantity(BigDecimal quantity) {
		for (PartPrice price : getPrices()) {
			if (price.isPriceForQuantity(quantity)) {
				return price;
			}
		}
		
		return getPrices().isEmpty() ? null : getPrices().get(0);
	}

	public void setSelectedPriceIndex(int index) {
		if (index >= prices.size()) index = -1;
		
		selectedPriceIndex = index;
	}
	
}
