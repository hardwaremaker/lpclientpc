package com.lp.client.frame.maps;

import java.util.ArrayList;
import java.util.List;

import com.lp.util.Helper;

public class HtmlBuilder {
	private String cssStyle;
	private String bodyDivWrapperName;
	private String body;
	private List<Script> scripts;
	
	private List<Script> getScripts() {
		if (scripts == null) 
			scripts = new ArrayList<Script>();
		return scripts;
	}
	
	public HtmlBuilder css(String css) {
		cssStyle = css;
		return this;
	}
	
	public HtmlBuilder bodyDivWrapper(String divName) {
		bodyDivWrapperName = divName;
		return this;
	}
	
	public HtmlBuilder body(String body) {
		this.body = body;
		return this;
	}
	
	public ScriptBuilder script() {
		return new ScriptBuilder(this);
	}
	
	public String build() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html lang=\"de\"><head>");
		if (!Helper.isStringEmpty(cssStyle)) {
			htmlBuilder.append("<style>")
				.append(cssStyle)
				.append("</style>");
		}
		
		for (Script script : getScripts()) {
			htmlBuilder.append(script.asString());
		}
		
		htmlBuilder.append("</head>")
			.append("<body>");
		
		if (!Helper.isStringEmpty(bodyDivWrapperName)) {
			htmlBuilder.append("<div id=\"").append(bodyDivWrapperName).append("\">");
		}
		htmlBuilder.append(body != null ? body : "");
		if (!Helper.isStringEmpty(bodyDivWrapperName)) {
			htmlBuilder.append("</div>");
		}
		htmlBuilder.append("</body></html>");
		
		return htmlBuilder.toString();
	}

	protected void addScript(Script script) {
		getScripts().add(script);
	}
	
}
