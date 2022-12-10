package com.lp.client.fertigung.arbeitsplanvergleich;

public class UebernehmenButtonData<T> extends UebernehmenBtn {
	private static final long serialVersionUID = 1L;

	private final T data;

	public UebernehmenButtonData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

}
