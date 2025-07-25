package org.example;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class LoginForm extends Composite {
    private final VerticalPanel panel = new VerticalPanel();
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    public LoginForm(Runnable onLoginSuccess) {
        HTMLPanel loginBox = new HTMLPanel("<div class='login-box'>" +
                "<h2>Login</h2>" +
                "<div class='user-box' id='usernameContainer'></div>" +
                "<div class='user-box' id='passwordContainer'></div>" +
                "<div id='buttonContainer'></div>" +
                "<div id='registerContainer'></div>" +
                "<div style='color: red;' id='errorContainer'></div>" +
                "</div>");

        initWidget(loginBox);

        TextBox usernameField = new TextBox();
        PasswordTextBox passwordField = new PasswordTextBox();
        Button loginButton = new Button("Se connecter");
        Button registerButton = new Button("Créer un compte");
        Label errorLabel = new Label();

        usernameField.getElement().setPropertyString("placeholder", "Nom d'utilisateur");
        passwordField.getElement().setPropertyString("placeholder", "Mot de passe");

        usernameField.setStyleName("user-box-input");
        passwordField.setStyleName("user-box-input");
        loginButton.setStyleName("styled-button");
        registerButton.setStyleName("styled-button");

        loginBox.add(usernameField, "usernameContainer");
        loginBox.add(passwordField, "passwordContainer");
        loginBox.add(loginButton, "buttonContainer");
        loginBox.add(registerButton, "registerContainer");
        loginBox.add(errorLabel, "errorContainer");

        loginButton.addClickHandler(e -> tryLogin(usernameField, passwordField, errorLabel, loginButton, onLoginSuccess));

        KeyUpHandler keyHandler = event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                tryLogin(usernameField, passwordField, errorLabel, loginButton, onLoginSuccess);
            }
        };

        usernameField.addKeyUpHandler(keyHandler);
        passwordField.addKeyUpHandler(keyHandler);

        registerButton.addClickHandler(e -> showRegisterDialog());
    }

    private void tryLogin(TextBox usernameField, TextBox passwordField, Label errorLabel, Button loginButton, Runnable onLoginSuccess) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        loginButton.setEnabled(false);
        errorLabel.setText("");

        greetingService.login(username, password, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText("Erreur serveur.");
                loginButton.setEnabled(true);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    onLoginSuccess.run();
                } else {
                    errorLabel.setText("Identifiants incorrects.");
                }
                loginButton.setEnabled(true);
            }
        });
    }

    private void showRegisterDialog() {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Créer un compte");

        VerticalPanel panel = new VerticalPanel();
        final TextBox newUsername = new TextBox();
        final PasswordTextBox newPassword = new PasswordTextBox();
        final Button createButton = new Button("Valider");
        final Label errorLabel = new Label();

        newUsername.getElement().setPropertyString("placeholder", "Nom d'utilisateur");
        newPassword.getElement().setPropertyString("placeholder", "Mot de passe");

        panel.setSpacing(10);
        panel.add(newUsername);
        panel.add(newPassword);
        panel.add(createButton);
        panel.add(errorLabel);

        dialogBox.setWidget(panel);
        dialogBox.center();
        dialogBox.show();

        createButton.addClickHandler(e -> {
            String username = newUsername.getText().trim();
            String password = newPassword.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Tous les champs sont obligatoires.");
                return;
            }

            greetingService.createAccount(username, password, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    errorLabel.setText("Erreur lors de la création.");
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        dialogBox.hide();
                    } else {
                        errorLabel.setText("Nom d'utilisateur déjà utilisé.");
                    }
                }
            });
        });
    }
}
