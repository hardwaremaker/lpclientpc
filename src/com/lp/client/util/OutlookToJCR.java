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
package com.lp.client.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;

import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeMail;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.MediaFac;
import com.lp.util.Helper;

public class OutlookToJCR extends FileToJCR {

	public static final String MIME = MediaFac.DATENFORMAT_MIMETYPE_TIKA_MSOFFICE;

	@Override
	protected List<JCRDocDto> createJCRImpl(File file, JCRDocDto jcr)
			throws IOException {
		List<JCRDocDto> jcrs = new ArrayList<JCRDocDto>();
		MAPIMessage msg = new MAPIMessage(file.getPath());

		jcr.setbData(Helper.getBytesFromFile(file));
		try {
			jcr.setsName(msg.getSubject() == null || msg.getSubject().isEmpty() ? "Email"
					: msg.getSubject());
			StringBuffer schlagworte = new StringBuffer();
			schlagworte.append(msg.getDisplayFrom());
			schlagworte.append(" TO ");
			schlagworte.append(msg.getDisplayTo());

			jcr.setsSchlagworte(schlagworte.toString());

		} catch (ChunkNotFoundException e) {
			e.printStackTrace();
			// wg. SP8831 auskommentiert 
			//return null;
			jcr.setsName(Helper.getName(file.getName()).replaceAll("'", "_"));
			jcr.setsFilename(file.getName());
			jcr.setsMIME(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT);
			jcr.setDocPath(new DocPath().add(new DocNodeFile(jcr.getsName())));
			jcrs.add(jcr);
			return jcrs;
		}

		jcr.setDocPath(new DocPath().add(new DocNodeMail(jcr.getsName())));

		jcrs.addAll(getAttachments(msg, jcr));

		jcr.getDocPath().add(new DocNodeFile("original"));

		jcrs.add(jcr);

		//SP8871
		msg.close();
		
		return jcrs;
	}

	private List<JCRDocDto> getAttachments(MAPIMessage msg, JCRDocDto vorlage) {
		List<JCRDocDto> jcrs = new ArrayList<JCRDocDto>();
		
		AttachmentChunks[] attachments = msg.getAttachmentFiles();
		if(attachments != null) {
			for(AttachmentChunks attachment:attachments) {
				if(attachment.getAttachData() == null)
					continue;
				JCRDocDto jcr = new JCRDocDto();
				jcr.setbData(attachment.getAttachData().getValue());
				String filename = attachment.getAttachLongFileName() == null?
						attachment.getAttachFileName().toString() : attachment.getAttachLongFileName().toString();
				jcr.setsFilename(filename);
				jcr.setsMIME(Helper.getMime(filename));
				jcr.setsName(Helper.getName(filename));
				
				
				jcr.setDocPath(vorlage.getDocPath().getDeepCopy().add(new DocNodeFile(filename)));
				jcr.setsSchlagworte(vorlage.getsSchlagworte());
				jcrs.add(jcr);
			}
		}
		
		return jcrs;
	}
}
