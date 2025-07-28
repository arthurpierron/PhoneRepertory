package org.example;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.HashMap;
import java.util.Map;

public class PhoneDirectory extends Composite {

    private final VerticalPanel contactListPanel = new VerticalPanel();
    private final VerticalPanel detailPanel = new VerticalPanel();
    private final Map<String, String> contacts = new HashMap<>();

    public PhoneDirectory(Runnable onLogout) {
        HTMLPanel htmlPanel = new HTMLPanel("" +
                "<div class='top-bar'>" +
                "<span class='title-label'>📒 Mon Répertoire</span>" +
                "<span id='logoutBtn'></span>" +
                "</div>" +
                "<div style='display: flex; height: calc(100vh - 60px);'>" +
                "<div class='sidebar' id='sidebar'></div>" +
                "<div class='detail-panel' id='detailPanel'></div>" +
                "</div>"
        );

        initWidget(htmlPanel);

        ScrollPanel contactScroll = new ScrollPanel(contactListPanel);
        contactScroll.setSize("100%", "100%");
        htmlPanel.add(contactScroll, "sidebar");

        htmlPanel.add(detailPanel, "detailPanel");

        Button logoutBtn = new Button("Déconnexion");
        logoutBtn.addClickHandler(e -> {
            RootPanel.get("mainContainer").clear();
            onLogout.run();
        });
        htmlPanel.add(logoutBtn, "logoutBtn");
        renderForm();
    }

    private void renderForm() {
        detailPanel.clear();

        Label sectionTitle = new Label("Ajouter un contact");
        sectionTitle.setStyleName("section-title");

        final TextBox nameBox = new TextBox();
        nameBox.getElement().setPropertyString("placeholder", "Nom");
        nameBox.setStyleName("directory-input");

        final TextBox phoneBox = new TextBox();
        phoneBox.getElement().setPropertyString("placeholder", "Téléphone");
        phoneBox.setStyleName("directory-input");;

        Button addBtn = new Button("Ajouter");
        addBtn.setStyleName("styled-button");

        addBtn.addClickHandler(event -> {
            String name = nameBox.getText().trim();
            String phone = phoneBox.getText().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Window.alert("Veuillez remplir tous les champs.");
                return;
            }

            contacts.put(name, phone);
            refreshContactList();
            showContactDetails(name);

            nameBox.setText("");
            phoneBox.setText("");
        });

        VerticalPanel form = new VerticalPanel();
        form.setSpacing(8);
        form.add(sectionTitle);
        form.add(nameBox);
        form.add(phoneBox);
        form.add(addBtn);

        detailPanel.add(form);
    }

    private void refreshContactList() {
        contactListPanel.clear();
        for (String name : contacts.keySet()) {
            Button contactBtn = new Button(name);
            contactBtn.setWidth("100%");
            contactBtn.setStyleName("contact-button");
            contactBtn.addClickHandler(e -> showContactDetails(name));
            contactListPanel.add(contactBtn);
        }
    }

    private void showContactDetails(String name) {
        if (!contacts.containsKey(name)) return;

        String phone = contacts.get(name);

        VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setStyleName("contact-details");
        infoPanel.setSpacing(8);

        Label nameLabel = new Label("👤 " + name);
        Label phoneLabel = new Label("📞 " + phone);

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.setStyleName("delete-button");
        deleteBtn.addClickHandler(e -> {
            contacts.remove(name);
            refreshContactList();
            renderForm();
        });

        infoPanel.add(nameLabel);
        infoPanel.add(phoneLabel);
        infoPanel.add(deleteBtn);

        detailPanel.clear();
        detailPanel.add(infoPanel);

        renderForm();
    }
}
