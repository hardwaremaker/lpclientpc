package com.lp.client.geodaten;

import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.partner.service.GeodatenDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.SerienbriefEmpfaengerDto;

public class SerienbriefTransformer extends BaseMapDataTransformer implements IMapData {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private SerienbriefEmpfaengerDto[] empfaengerRaw;
	private List<SerienbriefEmpfaengerDto> empfaenger;
	
	public SerienbriefTransformer(SerienbriefEmpfaengerDto[] empfaenger) throws Throwable {
		super();
		this.empfaengerRaw = empfaenger;
		initPartnerMap();
	}

	public SerienbriefEmpfaengerDto[] getEmpfaengerRaw() {
		return empfaengerRaw;
	}
	
	public List<SerienbriefEmpfaengerDto> getEmpfaenger() {
		if (empfaenger == null) {
			empfaenger = new ArrayList<SerienbriefEmpfaengerDto>();
		}
		return empfaenger;
	}
	
	private void initPartnerMap() {
		for (SerienbriefEmpfaengerDto dto : empfaengerRaw) {
			if (isValid(dto.getPartnerDto())) {
				getEmpfaenger().add(dto);
				getPartnerMap().put(dto.getPartnerDto().getIId(), dto.getPartnerDto());
			}
		}
		myLogger.info("Anzahl Serienbriefempfaenger = " + (empfaenger != null ? empfaengerRaw.length : 0) + ", Anzahl gueltige Adressen = " + getEmpfaenger().size());
	}

	@Override
	public MapData getMapData() {
//		try {
//			loadGeodaten();
//		} catch (Throwable e) {
//			myLogger.error("Throwable", e);
//		}
		return transformEmpfaenger();
	}

	private MapData transformEmpfaenger() {
		List<MapPosition> mapPositions = new ArrayList<MapPosition>();
		for (SerienbriefEmpfaengerDto dto : getEmpfaenger()) {
			if (dto.isInteressent()) {
				MapInteressent interessent = createMapInteressent(dto);
				if (interessent != null) 
					mapPositions.add(interessent);
			} else if (dto.isKunde()) {
				MapKunde kunde = createMapKunde(dto);
				if (kunde != null) 
					mapPositions.add(kunde);
			} else if (dto.isLieferant()) {
				MapLieferant lieferant = createMapLieferant(dto);
				if (lieferant != null)
					mapPositions.add(lieferant);
			} else {
				MapPartner partner = createMapPartner(dto);
				if (partner != null)
					mapPositions.add(partner);
			}
		}
		
		return new MapData(mapPositions);
	}

	private MapPartner createMapPartner(SerienbriefEmpfaengerDto dto) {
		GeodatenDto geodatenDto = getPartnerMap().get(dto.getPartnerDto().getIId()).getGeodatenDto();
		if (geodatenDto == null) return null;
		
		MapPartner mapPartner = new MapPartner(dto.getPartnerDto());
		mapPartner.setGeodatenDto(geodatenDto);
		return mapPartner;
	}

	private MapLieferant createMapLieferant(SerienbriefEmpfaengerDto dto) {
		try {
			GeodatenDto geodatenDto = getPartnerMap().get(dto.getPartnerDto().getIId()).getGeodatenDto();
			if (geodatenDto == null) return null;
			
			LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate().lieferantFindByPrimaryKey(dto.getLieferantIId());
			MapLieferant mapLieferant = new MapLieferant(lieferantDto);
			mapLieferant.setGeodatenDto(geodatenDto);
		return mapLieferant;
		} catch (Throwable t) {
			myLogger.error("Error bei Datenbeschaffung fuer die Map Position eines Lieferanten", t);
			return null;
		}
	}

	private MapInteressent createMapInteressent(SerienbriefEmpfaengerDto dto) {
		try {
			GeodatenDto geodatenDto = getPartnerMap().get(dto.getPartnerDto().getIId()).getGeodatenDto();
			if (geodatenDto == null) return null;
			
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(dto.getKundeIId());
			MapInteressent mapInteressent = new MapInteressent(kundeDto);
			mapInteressent.setGeodatenDto(geodatenDto);
			return mapInteressent;
		} catch (Throwable t) {
			myLogger.error("Error bei Datenbeschaffung fuer die Map Position eines Kunden", t);
			return null;
		}
	}

	private MapKunde createMapKunde(SerienbriefEmpfaengerDto dto) {
		try {
			GeodatenDto geodatenDto = getPartnerMap().get(dto.getPartnerDto().getIId()).getGeodatenDto();
			if (geodatenDto == null) return null;
			
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(dto.getKundeIId());
			MapKunde mapKunde = new MapKunde(kundeDto);
			mapKunde.setGeodatenDto(geodatenDto);
			return mapKunde;
		} catch (Throwable t) {
			myLogger.error("Error bei Datenbeschaffung fuer die Map Position eines Kunden", t);
			return null;
		}
	}
	
}
