package com.lp.client.frame.maps;

public class ScriptBuilder {
	private HtmlBuilder htmlBuilder;
	private Script script;
	
	public ScriptBuilder(HtmlBuilder htmlBuilder) {
		this.htmlBuilder = htmlBuilder;
		script = new Script();
	}

	public ScriptBuilder async() {
		script.setAsync(true);
		return this;
	}
	
	public ScriptBuilder defer() {
		script.setDefer(true);
		return this;
	}
	
	public ScriptBuilder source(String source) {
		script.setSource(source);
		return this;
	}
	
	public ScriptBuilder type(String type) {
		script.setType(type);
		return this;
	}
	
	public ScriptBuilder javascriptType() {
		script.setType("text/javascript");
		return this;
	}
	
	public HtmlBuilder end() {
		htmlBuilder.addScript(script);
		return htmlBuilder;
	}

	public ScriptBuilder content(String content) {
		script.setContent(content);
		return this;
	}

}
