package ru.pentragon.java2.networkclient.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import ru.pentragon.java2.clientserver.Command;
import ru.pentragon.java2.clientserver.user.Message;
import ru.pentragon.java2.networkclient.ClientApp;
import ru.pentragon.java2.networkclient.models.ClientNetwork;


import java.io.IOException;
import java.util.*;

import static java.util.List.of;

public class MainViewController {
    ClientApp mainApp;
    private ClientNetwork network;
    private static final List<Message> emptyMsg = of(
            new Message("У вас нет сообщений. Начните общение!")
    );

    @FXML
    public TableView<String> contactTable;
    @FXML
    public TableColumn<String, String> contactColumn;
    @FXML
    public TableView<Message> messageTable;//Messages
    @FXML
    public TableColumn<Message, String> messageColumn;//Messages, String
    @FXML
    public TextArea msgTextArea;
    @FXML
    public Button sendButton;
    @FXML
    public Button changeUsername;
    @FXML
    public TextField newUsernameTextField;

    private List<String> clientsList;


    public void setMainApp(ClientApp mainApp) {
        this.mainApp = mainApp;
        //contactTable.setItems(FXCollections.observableArrayList(mainApp.getPersonData()));
    }

    public void setNetwork(ClientNetwork network) {
        this.network = network;
    }

    @FXML
    public void initialize() {
/*        usersList.setItems(FXCollections.observableArrayList(NetworkChatClient.USERS_TEST_DATA));
        sendButton.setOnAction(event -> sendMessage());
        textField.setOnAction(event -> sendMessage());

        //тут я задаю обработчик события нажатия Enter если активно поле TextArea
        //Вообще я бы не хотел бы его добавлять, т.к. Enter позволял переходить на другую строку
        //Но пока так нормально
        msgBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSendButton();
            }
        });
        //список собеседников
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().getUserNameStringPropertyType());*/

        //если выделен собеседник отображается чат
        contactTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //событие на выделение ячейки
            showMessages();
        });
    }

    @FXML
    public void handleSendButton(ActionEvent actionEvent) {
        String selectedUser = contactTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                network.getOutputStream().writeObject(Command.messageCommand(selectedUser, (network.getUser().getUsername() + ": " + msgTextArea.getText()), network.getUser().getLogin()));
                network.getUser().saveMsgToDialog(selectedUser, msgTextArea.getText());
                network.getMyMessagesLogger().writeToLogFile(selectedUser,msgTextArea.getText());
                msgTextArea.clear();
                showMessages();
            } catch (IOException e) {
                e.printStackTrace();
                String errorMessage = "Failed to send message";
                ClientApp.showErrorWindow(e.getMessage(), errorMessage);
            }

        } else {
            showNoSelectionAlert();
        }

    }
    @FXML
    public void handleChangeUsername(ActionEvent actionEvent) {
        String insertData = newUsernameTextField.getText();
            try {
                if(!insertData.equals("")){
                    System.out.println(insertData);
                    network.getOutputStream().writeObject(Command.changeUsernameCommand(insertData));
                    network.getUser().setUsername(insertData);
                    newUsernameTextField.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
                String errorMessage = "Failed to send message";
                ClientApp.showErrorWindow(e.getMessage(), errorMessage);
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
    public void showMessages() {//String user
        String selectedUser = contactTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            LinkedList<Message> myData = network.getUser().getMessages().getMSGsForView(selectedUser);
            if (myData != null) {
                messageTable.setItems(FXCollections.observableArrayList(network.getUser().getMessages().getMSGsForView(selectedUser)));
                messageColumn.setCellValueFactory(cellData -> cellData.getValue().msgProperty());
            } else {
                messageTable.setItems(FXCollections.observableArrayList(emptyMsg));
                messageColumn.setCellValueFactory(cellData -> cellData.getValue().msgProperty());
            }
        }

    }

    public void updateUserList(Map<String, String> users) {
        this.clientsList = new ArrayList<>();
        this.clientsList.addAll(users.keySet());
        System.out.println(clientsList.toString());
        LinkedList<String> myList;
        for (String s : clientsList) {
            if(!network.getUser().getMessages().getMessageData().containsKey(s)){
                myList = network.getMyMessagesLogger().readFromLogFile(s);
                if(myList!=null){
                    network.getUser().getMessages().getMessageData().put(s,  myList);
                    System.out.println(myList.toString());
                }
            }
        }
        contactTable.setItems(FXCollections.observableArrayList(clientsList));
        contactColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
    }

}
