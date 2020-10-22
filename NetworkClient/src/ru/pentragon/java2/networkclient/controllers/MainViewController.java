package ru.pentragon.java2.networkclient.controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import ru.pentragon.java2.networkclient.ClientApp;
import ru.pentragon.java2.networkclient.models.ClientNetwork;
import ru.pentragon.java2.networkclient.user_repo.Message;
import ru.pentragon.java2.networkclient.user_repo.Messages;
import ru.pentragon.java2.networkclient.user_repo.User;
import ru.pentragon.java2.networkclient.user_repo.Users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.List.of;

public class MainViewController {
    ClientApp mainApp;
    private ClientNetwork network;
    private static final List<Message> emptyMsg = of(
            new Message("У вас нет сообщений. Начните общение!")
    );

    @FXML
    public TableView<User> contactTable;
    @FXML
    public TableColumn<User, String> contactColumn;
    @FXML
    public TableView<Message> messageTable;//Messages
    @FXML
    public TableColumn<Message,String> messageColumn;//Messages, String
    @FXML
    public TextArea msgTextArea;
    @FXML
    public Button sendButton;



    public void setMainApp(ClientApp mainApp) {
        this.mainApp = mainApp;
        contactTable.setItems(FXCollections.observableArrayList(mainApp.getPersonData()));
    }

    public void setNetwork(ClientNetwork network) {
        this.network = network;
    }

    @FXML
    public void initialize() {

//        usersList.setItems(FXCollections.observableArrayList(NetworkChatClient.USERS_TEST_DATA));
        //sendButton.setOnAction(event -> sendMessage());
//        textField.setOnAction(event -> sendMessage());


//        //тут я задаю обработчик события нажатия Enter если активно поле TextArea
//        //Вообще я бы не хотел бы его добавлять, т.к. Enter позволял переходить на другую строку
//        //Но пока так нормально
//        msgBox.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                handleSendButton();
//            }
//        });
        //список собеседников
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().getUserNameStringPropertyType());
        //если выделен собеседник отображается чат
        contactTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //событие на выделение ячейки
            showMessages(newValue);
        });
    }

    @FXML
    public void handleSendButton(ActionEvent actionEvent) {
        User selectedUser = contactTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                network.getOutputStream().writeUTF(selectedUser.getLogin()+" "+msgTextArea.getText());
                network.getUser().saveMsgToDialog(selectedUser, msgTextArea.getText());
                msgTextArea.clear();
                showMessages(selectedUser);
            } catch (IOException e) {
                e.printStackTrace();
                String errorMessage = "Failed to send message";
                ClientApp.showErrorWindow(e.getMessage(), errorMessage);
            }

        } else {
            showNoSelectionAlert();
        }


    }

    /*окно предупреждение, если не выделен собеседник и программа не знает кому отправить сообщение*/
    private void showNoSelectionAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("User, not selected!");
        alert.setContentText("Please select a user in the left table");
        alert.showAndWait();
    }

    /*оображение переписки*/
    @FXML
    public void showMessages(User user) {
        LinkedList<Message> myData = network.getUser().getMessages().getMSGsForView(user);
        if(myData!=null){
            messageTable.setItems(FXCollections.observableArrayList(network.getUser().getMessages().getMSGsForView(user)));

            messageColumn.setCellValueFactory(cellData -> cellData.getValue().msgProperty());
        }
        else{
            messageTable.setItems(FXCollections.observableArrayList(emptyMsg));
            messageColumn.setCellValueFactory(cellData -> cellData.getValue().msgProperty());
        }
    }

}
