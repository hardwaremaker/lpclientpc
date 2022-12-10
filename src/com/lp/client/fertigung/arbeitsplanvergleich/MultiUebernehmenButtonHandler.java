package com.lp.client.fertigung.arbeitsplanvergleich;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.WrapperTextField;

public class MultiUebernehmenButtonHandler<T> implements IUebernehmenButtonHandler {

	private List<UebernehmenButtonData<T>> buttons;
	private Optional<UebernehmenButtonData<T>> selectedButton;
	protected final WrapperTextField txtFieldTarget;
	private Color origBGColor;

	public MultiUebernehmenButtonHandler(WrapperTextField textField) {
		buttons = new ArrayList<UebernehmenButtonData<T>>();
		selectedButton = Optional.empty();
		origBGColor = textField.getBackground();
		this.txtFieldTarget = textField;
	}

	public void add(UebernehmenButtonData<T> btn) {
		buttons.add(btn);
		btn.setHandler(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void activate(UebernehmenBtn sourceBtnRaw) {
		if (!(sourceBtnRaw instanceof UebernehmenButtonData<?>)) {
			return;
		}
		UebernehmenButtonData<T> clickedBtn = (UebernehmenButtonData<T>) sourceBtnRaw;
		onActivate(clickedBtn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void deactivate(UebernehmenBtn sourceBtnRaw) {
		if (!(sourceBtnRaw instanceof UebernehmenButtonData<?>)) {
			return;
		}
		UebernehmenButtonData<T> clickedBtn = (UebernehmenButtonData<T>) sourceBtnRaw;
		if (selectedButton.isPresent() && selectedButton.get().equals(clickedBtn)) {
			onDeactivate(clickedBtn);
		}
	}

	public Optional<T> getSelected() {
		return selectedButton.map(UebernehmenButtonData::getData);
	}

	/**
	 * Called when the currently active button is deactivated
	 * 
	 * @param deactivatedButton
	 */
	protected void onDeactivate(UebernehmenButtonData<T> deactivatedButton) {
		selectedButton = Optional.empty();
		deactivatedButton.setDeactivated();
		txtFieldTarget.setBackground(origBGColor);
	}

	/**
	 * Called when a button is activated.
	 * 
	 * @param activatedButton
	 */
	protected void onActivate(UebernehmenButtonData<T> activatedButton) {
		if (selectedButton.isPresent()) {
			selectedButton.get().setDeactivated();
		}
		selectedButton = Optional.of(activatedButton);
		activatedButton.setActivated();
		txtFieldTarget.setBackground(Defaults.getInstance().getSelectedFieldBgColor());
	}

}
