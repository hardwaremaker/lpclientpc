package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.angebotstkl.service.WebFindChipsDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.service.findchips.FindchipsPart;
import com.lp.service.findchips.FindchipsPartPrice;
import com.lp.service.findchips.FindchipsResponse;

public class FindChipsPartFactory implements IPartNormalizationFactory<FindchipsResponse>, IPartImportFactory {
	private final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(FindChipsPartFactory.class);
	
	private WebabfrageBaseModel baseModel;
	private String waehrungMandant;

	public FindChipsPartFactory(WebabfrageBaseModel baseModel) {
		this.baseModel = baseModel;
		try {
			waehrungMandant = LPMain.getTheClient().getSMandantenwaehrung();
		} catch (Throwable e1) {
			waehrungMandant = "EUR";
			myLogger.error("waehrungMandant not found! Using \"EUR\" instead!", e1);
		}
	}

	@Override
	public List<NormalizedWebabfragePart> normalize(Integer searchId, FindchipsResponse part) {
		List<NormalizedWebabfragePart> normalizedParts = new ArrayList<NormalizedWebabfragePart>();
		
		for (FindchipsPart findchipsPart : part.getParts()) {
			NormalizedWebabfragePart normalizedPart = new NormalizedWebabfragePart(
					NormalizedWebabfragePart.AbfrageTyp.FINDCHIPS, part);
			WebFindChipsDto webfindchipsDto = new WebFindChipsDto();
			webfindchipsDto.setcDistributor(part.getDistributor().getId());
			webfindchipsDto.setcName(part.getDistributor().getName());
			webfindchipsDto.setWebabfrageIId(AngebotstklFac.WebabfrageTyp.FINDCHIPS);

			normalizedPart.setSearchId(searchId);
			normalizedPart.setRawPart(findchipsPart);
			normalizedPart.setDistributor(webfindchipsDto);
			normalizedPart.setDistributorItemNo(findchipsPart.getDistributorItemNo());
			normalizedPart.setManufacturer(findchipsPart.getManufacturer());
			normalizedPart.setPartName(findchipsPart.getPart());
			normalizedPart.setStock(findchipsPart.getStock());
			normalizedPart.setDescription(findchipsPart.getDescription());
			normalizedPart.setUrl(findchipsPart.getBuyNowUrl());
			List<PartPrice> normalizedPrices = normalizePricesNew(searchId, findchipsPart.getPrice());
			normalizedPart.setQuantityScale(new QuantityScale(normalizedPrices));

			normalizedParts.add(normalizedPart);
		}
		
		return normalizedParts;
	}

	private List<PartPrice> normalizePricesNew(Integer searchId, List<FindchipsPartPrice> findchipsPrices) {
		List<PartPrice> normalizedPrices = new ArrayList<PartPrice>();
		Collections.sort(findchipsPrices, new Comparator<FindchipsPartPrice>() {
			@Override
			public int compare(FindchipsPartPrice p1, FindchipsPartPrice p2) {
				if (p1.getQuantity() == null) return -1;
				if (p2.getQuantity() == null) return 1;
				
				return p1.getQuantity().compareTo(p2.getQuantity());
			}
		});
		
		for (Entry<Integer, BigDecimal> ekMenge : baseModel.getEkMengen().entrySet()) {
			PartPrice partPrice = getPriceForMenge(ekMenge.getValue().multiply(baseModel.getResultById(searchId).getMenge()), findchipsPrices);
			normalizedPrices.add(partPrice.getPrice() != null && partPrice.getCurrency() != null ? convertToMandantenWaehrung(partPrice) : partPrice);
		}

		return normalizedPrices;
	}
	
	private PartPrice convertToMandantenWaehrung(PartPrice partPrice) {
		if (!waehrungMandant.equals(partPrice.getCurrency())) {
			WaehrungDto waehrungDto = null;
			try {
				waehrungDto = DelegateFactory.getInstance().getLocaleDelegate().waehrungFindByPrimaryKey(partPrice.getCurrency());
			} catch (Throwable e) {
				LpLogger.getInstance(FindChipsPartFactory.class).debug("Cannot find currency=" + partPrice.getCurrency(), e);
			}
			try {
				partPrice.setPrice(waehrungDto == null ? partPrice.getPrice() : DelegateFactory.getInstance().getLocaleDelegate().
					rechneUmInAndereWaehrung(partPrice.getPrice(), waehrungDto.getCNr(), waehrungMandant));
			} catch (Throwable e) {
				LpLogger.getInstance(FindChipsPartFactory.class).error("Error during currency conversion from " + waehrungDto != null ? waehrungDto.getCNr() : null + " to " + waehrungMandant, e);
			}
			partPrice.setCurrency(waehrungDto == null ? partPrice.getCurrency() : waehrungMandant);
		}
		
		return partPrice;
	}

	private PartPrice getPriceForMenge(BigDecimal menge, List<FindchipsPartPrice> findchipsPrices) {
		if (menge == null || findchipsPrices == null || findchipsPrices.isEmpty()) return new PartPrice();
		
		try {
		FindchipsPartPrice fcPartPrice = findchipsPrices.get(0);
		for (int i = 0; i < findchipsPrices.size(); i++) {
			if (findchipsPrices.get(i).getQuantity() != null && menge.compareTo(findchipsPrices.get(i).getQuantity()) == -1) {
				break;
			}
			fcPartPrice = findchipsPrices.get(i);
		}
		PartPrice partPrice = new PartPrice();
		
		if (fcPartPrice != null) {
			partPrice.setPrice(fcPartPrice.getPrice());
			partPrice.setCurrency(fcPartPrice.getCurrency());
			partPrice.setQuantityFrom(menge);
			return partPrice;
		}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new PartPrice();
	}

	@Override
	public List<EinkaufsangebotpositionDto> transformWebabfrageResult(EinkaufsangebotDto einkaufsangebotDto, IWebabfrageResult result) throws ExceptionLP, Throwable {
		List<EinkaufsangebotpositionDto> ekpositionDtos = new ArrayList<EinkaufsangebotpositionDto>();
		List<IWebabfrageResult> results = new ArrayList<IWebabfrageResult>();
		
		if (result.getSelectedPart() != null) {
			if (result instanceof CondensedWebabfrageResultItem) {
				results.addAll(((CondensedWebabfrageResultItem)result).getList());
			} else {
				results.add(result);
			}
			
			for (IWebabfrageResult item : results) {
				EinkaufsangebotpositionDto positionDto = item.getEinkaufsangebotpositionDto();
				positionDto.setCBuyerurl(result.getSelectedPart().getUrl());
				positionDto.setTLetztewebabfrage(new Timestamp(System.currentTimeMillis()));
				List<PartPrice> partPrices = result.getSelectedPart().getQuantityScale().getPrices();
				if (partPrices == null) {
					myLogger.warn("Could not import part=" + result.getSelectedPart().getPartName() + 
							" of position=" + result.getName() + ", pricelist is null");
					continue;
				}
				
				positionDto.setNPreis1(getPriceForQuantity(partPrices, 
						baseModel.getEkMengen().get(1) == null ? null : baseModel.getEkMengen().get(1).multiply(result.getMenge())));
				positionDto.setNPreis2(getPriceForQuantity(partPrices, 
						baseModel.getEkMengen().get(2) == null ? null : baseModel.getEkMengen().get(2).multiply(result.getMenge())));
				positionDto.setNPreis3(getPriceForQuantity(partPrices, 
						baseModel.getEkMengen().get(3) == null ? null : baseModel.getEkMengen().get(3).multiply(result.getMenge())));
				positionDto.setNPreis4(getPriceForQuantity(partPrices, 
						baseModel.getEkMengen().get(4) == null ? null : baseModel.getEkMengen().get(4).multiply(result.getMenge())));
				positionDto.setNPreis5(getPriceForQuantity(partPrices, 
						baseModel.getEkMengen().get(5) == null ? null : baseModel.getEkMengen().get(5).multiply(result.getMenge())));
					
				IWebpartnerDto webfindchipsDto = DelegateFactory.getInstance().getAngebotstklDelegate().
						webfindchipsFindByDistributorId(result.getSelectedPart().getDistributor().getId());
				positionDto.setLieferantIId(webfindchipsDto == null ? null : webfindchipsDto.getLieferantIId());
				
				ekpositionDtos.add(positionDto);
			}
		}
		
		return ekpositionDtos;
	}

	private BigDecimal getPriceForQuantity(List<PartPrice> partPrices, BigDecimal menge) {
		if (menge == null) return null;
		
		for (PartPrice partPrice : partPrices) {
			if (menge.compareTo(partPrice.getQuantityFrom()) == 0) {
				return partPrice.getPrice();
			}
		}
		return null;
	}

}
