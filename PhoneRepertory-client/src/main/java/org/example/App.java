package org.example;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class App implements EntryPoint {

	private static final String SERVER_ERROR = "Une erreur s'est produite côté serveur.";
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public void onModuleLoad() {
		final Button sendButton = new Button("Confirmer");
		final Button registerButton = new Button("Créer un compte");
		final TextBox nameField = new TextBox();
		final TextBox passwordField = new TextBox();
		nameField.setText("Username");
		passwordField.setText("Password");
		final Label errorLabel = new Label();

		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("passwordFieldContainer").add(passwordField);
		RootPanel.get("ConfirmButtonContainer").add(sendButton);
		RootPanel.get("RegisterButtonContainer").add(registerButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Dialog pour les erreurs
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Erreur");
		final Button closeButton = new Button("Fermer");
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		closeButton.addClickHandler(event -> {
			dialogBox.hide();
			sendButton.setEnabled(true);
			sendButton.setFocus(true);
		});

		nameField.setFocus(true);
		nameField.selectAll();

		// Handler principal
		MyHandler handler = new MyHandler(nameField, passwordField, errorLabel, sendButton);

		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
		passwordField.addKeyUpHandler(handler);

		// Créer un compte → ouvrir un formulaire dans un DialogBox
		registerButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showRegisterDialog();
			}
		});
	}

	private void showRegisterDialog() {
		final DialogBox registerDialog = new DialogBox();
		registerDialog.setText("Créer un compte");

		VerticalPanel panel = new VerticalPanel();
		final TextBox newUsername = new TextBox();
		final TextBox newPassword = new TextBox();
		final Button createButton = new Button("Valider");
		final Label registerError = new Label();

		newUsername.getElement().setPropertyString("placeholder", "Nom d'utilisateur");
		newPassword.getElement().setPropertyString("placeholder", "Mot de passe");

		panel.setSpacing(10);
		panel.add(newUsername);
		panel.add(newPassword);
		panel.add(createButton);
		panel.add(registerError);

		registerDialog.setWidget(panel);
		registerDialog.center();
		registerDialog.show();

		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String username = newUsername.getText().trim();
				String password = newPassword.getText().trim();

				if (username.isEmpty() || password.isEmpty()) {
					registerError.setText("Tous les champs sont obligatoires.");
					return;
				}

				greetingService.createAccount(username, password, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						registerError.setText("erreur de création de compte");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							registerError.setText("Compte créé avec succès !");
							registerDialog.hide();
						} else {
							registerError.setText("Nom d'utilisateur déjà utilisé.");
						}
					}
				});
			}
		});
	}

	class MyHandler implements ClickHandler, KeyUpHandler {
		private final TextBox usernameField;
		private final TextBox passwordField;
		private final Label errorLabel;
		private final Button sendButton;

		public MyHandler(TextBox nameField, TextBox pwdField, Label error, Button sendBtn) {
			this.usernameField = nameField;
			this.passwordField = pwdField;
			this.errorLabel = error;
			this.sendButton = sendBtn;
		}

		@Override
		public void onClick(ClickEvent event) {
			login();
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				login();
			}
		}

		private void login() {
			String username = usernameField.getText().trim();
			String password = passwordField.getText().trim();
			errorLabel.setText("");

			if (username.isEmpty() || password.isEmpty()) {
				errorLabel.setText("Veuillez remplir tous les champs.");
				return;
			}

			sendButton.setEnabled(false);

			greetingService.login(username, password, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(SERVER_ERROR);
					sendButton.setEnabled(true);
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						errorLabel.setText("Connexion réussie !");
					} else {
						errorLabel.setText("Identifiants incorrects.");
					}
					sendButton.setEnabled(true);
				}
			});
		}
	}
}
