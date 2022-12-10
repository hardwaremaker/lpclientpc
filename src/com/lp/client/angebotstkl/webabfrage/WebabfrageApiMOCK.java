package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.WebFindChipsDto;
import com.lp.service.findchips.FindchipsPart;

public class WebabfrageApiMOCK implements IWebabfrageApi {

	private IWebabfrageCallback callback;
	
	public WebabfrageApiMOCK(IWebabfrageCallback callback) {
		this.callback = callback;
	}

	@Override
	public void find(Map<Integer, List<String>> idSearchStringMap, Integer requestType) throws ExceptionLP {
		NormalizedWebabfragePart part1 = new NormalizedWebabfragePart(NormalizedWebabfragePart.AbfrageTyp.FINDCHIPS, new Object());
		WebFindChipsDto webfindchipsDto1 = new WebFindChipsDto();
		webfindchipsDto1.setcDistributor("6666");
		webfindchipsDto1.setcName("TestDistributor1");
		webfindchipsDto1.setWebabfrageIId(AngebotstklFac.WebabfrageTyp.FINDCHIPS);
		part1.setSearchId(getSearchId(idSearchStringMap));
		part1.setDistributor(webfindchipsDto1);
		part1.setManufacturer("Manu 1");
		part1.setPartName("Part Name 1");
		part1.setStock(new BigDecimal(3000));
		part1.setDescription("Ich bin ein Mock");
		part1.setUrl("www.google.at");
		FindchipsPart findchipsPart = new FindchipsPart();
		findchipsPart.setBuyNowUrl("buynowUrl");
		part1.setRawPart(findchipsPart);
		
		PartPrice price1 = new PartPrice();
		price1.setCurrency("EUR");
		price1.setPrice(new BigDecimal("0.12"));
		price1.setQuantityFrom(new BigDecimal("20"));
		price1.setQuantityTo(new BigDecimal("50"));
		
		PartPrice price2 = new PartPrice();
		price2.setCurrency("EUR");
		price2.setPrice(new BigDecimal("0.08"));
		price2.setQuantityFrom(new BigDecimal("50"));
		price2.setQuantityTo(new BigDecimal("100"));

		PartPrice price3 = new PartPrice();
		price3.setCurrency("EUR");
		price3.setPrice(new BigDecimal("0.05"));
		price3.setQuantityFrom(new BigDecimal("100"));
		price3.setQuantityTo(null);
		
		part1.setQuantityScale(new QuantityScale(Arrays.asList(new PartPrice[] {price1, price2, price3})));

		NormalizedWebabfragePart part2 = new NormalizedWebabfragePart(NormalizedWebabfragePart.AbfrageTyp.FINDCHIPS, new Object());
		WebFindChipsDto webfindchipsDto2 = new WebFindChipsDto();
		webfindchipsDto2.setcDistributor("7777");
		webfindchipsDto2.setcName("TestDistributor2");
		webfindchipsDto2.setWebabfrageIId(AngebotstklFac.WebabfrageTyp.FINDCHIPS);
		part2.setSearchId(getSearchId(idSearchStringMap));
		part2.setDistributor(webfindchipsDto2);
		part2.setManufacturer("Manu AAA");
		part2.setPartName("Part Name AAA");
		part2.setStock(new BigDecimal(5500));
		part2.setDescription("Auch ich bin ein Mock");
		part2.setUrl("http://www.heliumv.at");
		part2.setRawPart(findchipsPart);
		
		PartPrice price4 = new PartPrice();
		price4.setCurrency("EUR");
		price4.setPrice(new BigDecimal("1.1"));
		price4.setQuantityFrom(new BigDecimal("1"));
		price4.setQuantityTo(new BigDecimal("10"));
		
		PartPrice price5 = new PartPrice();
		price5.setCurrency("EUR");
		price5.setPrice(new BigDecimal("1.05"));
		price5.setQuantityFrom(new BigDecimal("10"));
		price5.setQuantityTo(new BigDecimal("25"));

		PartPrice price6 = new PartPrice();
		price6.setCurrency("EUR");
		price6.setPrice(new BigDecimal("1.02"));
		price6.setQuantityFrom(new BigDecimal("25"));
		price6.setQuantityTo(new BigDecimal("50"));
		
		PartPrice price7 = new PartPrice();
		price7.setCurrency("EUR");
		price7.setPrice(new BigDecimal("0.97"));
		price7.setQuantityFrom(new BigDecimal("50"));
		price7.setQuantityTo(null);

		part2.setQuantityScale(new QuantityScale(Arrays.asList(new PartPrice[] {price4, price5, price6, price7})));
		
		callback.done(new ArrayList<WebabfrageError>(), new ArrayList<WebabfrageException>());
		callback.processParts(Arrays.asList(new NormalizedWebabfragePart[] {part1, part2}));
	}

	private Integer getSearchId(Map<Integer, List<String>> idSearchStringMap) {
		for (Integer id : idSearchStringMap.keySet()) {
			return id;
		}
		return null;
	}

	@Override
	public void cancelRequest(Integer requestType) {
		
	}

}
