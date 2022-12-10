package com.lp.client.util;

import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.util.HvOptional;

/**
 * Helper der dar&uuml;ber Auskunft gibt, ob sich ein Mwstsatz ge&auml;ndert
 * hat und den g&uuml;ltigen Satz zur Verf&uuml;gung stellt.</p>
 * <p>Mir ist bewusst, dass das nichts tolles ist, dient in erster Linie
 * als Ansatz zur Refaktorisierung.</p>
 * 
 * @author gerold
 */
public class MwstsatzChangedInfo {
	private final boolean changed;
	private final HvOptional<MwstsatzDto> mwstsatz;
	private final HvOptional<Integer> oldMwstsatz;
	
	public MwstsatzChangedInfo() {
		this.changed = false;
		this.mwstsatz = HvOptional.empty();
		this.oldMwstsatz = HvOptional.empty();
	}
	
	public MwstsatzChangedInfo(Integer oldMwstsatzId, MwstsatzDto validMwstsatzDto) {
		this.oldMwstsatz = HvOptional.of(oldMwstsatzId);
		this.changed = !oldMwstsatzId.equals(validMwstsatzDto.getIId());
		this.mwstsatz = HvOptional.of(validMwstsatzDto);
	}

	public boolean isChanged() {
		return changed;
	}

	public HvOptional<MwstsatzDto> getMwstsatz() {
		return mwstsatz;
	}
	
	public HvOptional<Integer> getOldMwstsatzId() {
		return oldMwstsatz;
	}
}
