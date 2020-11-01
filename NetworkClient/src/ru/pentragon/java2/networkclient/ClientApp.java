package ru.pentragon.java2.networkclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.pentragon.java2.logger.ClientMessageContainer;
import ru.pentragon.java2.logger.Logger;
import ru.pentragon.java2.networkclient.controllers.AuthDialogControlleer;
import ru.pentragon.java2.networkclient.controllers.MainViewController;
import ru.pentragon.java2.networkclient.models.ClientNetwork;

import java.io.IOException;


public class ClientApp extends Application {
    private Stage primaryStage;
    private Stage authDialogStage;
    private ClientNetwork network;
    private MainViewController viewController;

//    public void setPrimaryStage(Stage primaryStage) {
//        this.primaryStage = primaryStage;
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //создаем окно чата оно является основным но мы его не показываем до того как будет выполнена авторизация
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(ClientApp.class.getResource("views/MainView.fxml"));
        Parent root = mainLoader.load();

        //MainViewController controller = mainLoader.getController();
        viewController = mainLoader.getController();
        viewController.setMainApp(this);
        this.primaryStage = primaryStage;

        primaryStage.setScene(new Scene(root));//, 300, 275
        primaryStage.setResizable(false);

        MainViewController mainDialogController = mainLoader.getController();

        authWindowStart(primaryStage);//вызываем авторизацию

        mainDialogController.setNetwork(network);
        primaryStage.setOnCloseRequest(event -> network.close());//при закрытии окна закрываем сокет

    }

    private void authWindowStart(Stage primaryStage) {
        try {
            network = new ClientNetwork();
            if (!network.connect()) {
                showErrorWindow("", "Ошибка подключения к серверу");
                return;
            }//попытка подключния к серверу, если не успешно, то программа дальше не работает

            FXMLLoader authDialogLoader = new FXMLLoader();
            authDialogLoader.setLocation(ClientApp.class.getResource("views/AuthDialogView.fxml"));
            Parent authDialogPanel = authDialogLoader.load();
            authDialogStage = new Stage();
            authDialogStage.setResizable(false);
            authDialogStage.setTitle("Authorization");
            authDialogStage.initModality(Modality.WINDOW_MODAL);
            authDialogStage.initOwner(primaryStage);
            Scene scene = new Scene(authDialogPanel);
            authDialogStage.setScene(scene);

            //передаем данные в контроллер
            AuthDialogControlleer authDialogControlleer = authDialogLoader.getController();
            authDialogControlleer.setNetwork(network);
            authDialogControlleer.setClientApp(this);

            authDialogStage.show();
        } catch (IOException e) {
            showErrorWindow("Окно авторизации не загружено", "Ошибка загрузки");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    public Window getPrimaryStage() {
        return primaryStage;
    }

    public static void showErrorWindow(String errorDetails, String errorTitle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Network Error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorDetails);
        alert.showAndWait();
    }

    //закрываем окно с авторизацией, переходим в окно чата
    public void openChat() {
        authDialogStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUser().getUsername());
        network.waitMessages(viewController);

    }

    public void openChangeNameDialog(){

    }

}
