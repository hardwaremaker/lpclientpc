package com.lp.client.frame.uiproperty;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.ParameterDelegate;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

public abstract class HvUIProperty<T> {

	private T value = null;
	private boolean loaded = false;
	
	public HvUIProperty() {
	}

	public T get() throws Throwable {
		if (!loaded) {
			set(load());
			loaded = true;
		}
		return value;
	}
	
	public T getNoExc() {
		if (!loaded) {
			set(loadNoExc());
			loaded = true;
		}
		return value;
	}
	
	protected void set(T newValue) {
		this.value = newValue;
	}
	
	public void reset() {
		value = null;
		loaded = false;
	}

	protected T getDefaultValue() {
		return null;
	}
	
	protected abstract T load() throws Throwable;
	
	private T loadNoExc() {
		try {
			return load();
		} catch (Throwable t) {
			return getDefaultValue();
		}
	}
	
	protected ParameterDelegate parameterDelegate() throws Throwable {
		return DelegateFactory.getInstance().getParameterDelegate();
	}
	
	protected ArbeitsplatzparameterDto holeArbeitsplatzparameter(String param) throws ExceptionLP, Throwable {
		return parameterDelegate().holeArbeitsplatzparameter(param);
	}
	
	protected ParametermandantDto holeMandantparameter(String param, String kategorie) throws ExceptionLP, Throwable {
		return parameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), kategorie, param);
	}
	
	protected ParametermandantDto holeMandantparameterAllgemein(String param) throws ExceptionLP, Throwable {
		return holeMandantparameter(param, ParameterFac.KATEGORIE_ALLGEMEIN);
	}

	protected ParametermandantDto holeMandantparameterFertigung(String param) throws ExceptionLP, Throwable {
		return holeMandantparameter(param, ParameterFac.KATEGORIE_FERTIGUNG);
	}
}
