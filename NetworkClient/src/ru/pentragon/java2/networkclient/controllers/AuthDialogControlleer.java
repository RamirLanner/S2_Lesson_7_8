package ru.pentragon.java2.networkclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.pentragon.java2.networkclient.ClientApp;
import ru.pentragon.java2.networkclient.models.ClientNetwork;

public class AuthDialogControlleer {
    @FXML
    public Button logInButton;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;

    private ClientNetwork network;
    private ClientApp clientApp;

    @FXML
    public void executeAuth(ActionEvent actionEvent) {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            ClientApp.showErrorWindow("Username and password should be not empty!", "Auth error");
            return;
        }

        String authError = network.sendAuthCommand(login, password);
        if (authError == null) {
            clientApp.openChat();
            //System.out.println("auth good");
        } else {
            ClientApp.showErrorWindow(authError, "Auth error");
        }
    }

    public void setNetwork(ClientNetwork network) {
        this.network = network;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }
}
