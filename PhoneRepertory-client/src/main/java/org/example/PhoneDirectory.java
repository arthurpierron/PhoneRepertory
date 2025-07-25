package org.example;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class PhoneDirectory extends Composite {

    private final VerticalPanel container = new VerticalPanel();
    private final FlexTable contactTable = new FlexTable();

    public PhoneDirectory(Runnable onLogout) {
        initWidget(container);
        container.setStyleName("login-box");
        container.setSpacing(10);
        container.setWidth("400px");
        container.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        Label title = new Label("📒 Répertoire");
        container.add(title);

        final TextBox nameBox = new TextBox();
        nameBox.getElement().setPropertyString("placeholder", "Nom");
        nameBox.setStyleName("user-box-input");

        final TextBox phoneBox = new TextBox();
        phoneBox.getElement().setPropertyString("placeholder", "Téléphone");
        phoneBox.setStyleName("user-box-input");

        Button addBtn = new Button("Ajouter");
        addBtn.setStyleName("styled-button");

        container.add(wrapInput(nameBox));
        container.add(wrapInput(phoneBox));
        container.add(addBtn);

        contactTable.setStyleName("contact-table");
        contactTable.setText(0, 0, "Nom");
        contactTable.setText(0, 1, "Téléphone");
        contactTable.setText(0, 2, "Action");
        container.add(contactTable);

        Button logoutBtn = new Button("Déconnexion");
        logoutBtn.setStyleName("styled-button");
        logoutBtn.addClickHandler(e -> {
            RootPanel.get("mainContainer").clear();
            onLogout.run();
        });
        container.add(logoutBtn);

        addBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String name = nameBox.getText().trim();
                String phone = phoneBox.getText().trim();

                if (name.isEmpty() || phone.isEmpty()) {
                    Window.alert("Remplir tous les champs.");
                    return;
                }

                int row = contactTable.getRowCount();
                contactTable.setText(row, 0, name);
                contactTable.setText(row, 1, phone);

                Button deleteBtn = new Button("❌");
                deleteBtn.addClickHandler(e -> contactTable.removeRow(row));
                contactTable.setWidget(row, 2, deleteBtn);

                nameBox.setText("");
                phoneBox.setText("");
            }
        });
    }

    private Widget wrapInput(TextBox box) {
        VerticalPanel wrapper = new VerticalPanel();
        wrapper.setStyleName("user-box");
        wrapper.add(box);
        return wrapper;
    }
}
