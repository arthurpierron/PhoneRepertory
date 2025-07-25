package org.example;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class App implements EntryPoint {
	@Override
	public void onModuleLoad() {
		showLoginForm();
	}

	private void showLoginForm() {
		RootPanel.get("mainContainer").clear();
		RootPanel.get("mainContainer").add(new LoginForm(this::showPhoneDirectory));
	}

	private void showPhoneDirectory() {
		RootPanel.get("mainContainer").clear();
		RootPanel.get("mainContainer").add(new PhoneDirectory(this::showLoginForm));
	}
}
