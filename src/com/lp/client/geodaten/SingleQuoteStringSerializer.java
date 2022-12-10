package com.lp.client.geodaten;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SingleQuoteStringSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String str, JsonGenerator jGen, SerializerProvider sP)
			throws IOException, JsonProcessingException {
		str = str.replace("\n", "\\n");
        str = str.replace("'", "\\'"); // turns all ' into \'
        jGen.writeRawValue("'" + str + "'"); // write surrounded by single quote
	}
}
