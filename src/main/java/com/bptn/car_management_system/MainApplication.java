package com.bptn.car_management_system;

import com.bptn.car_management_system.entities.DbUtilityDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // initialize database
        DbUtilityDAO.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        stage.initStyle(StageStyle.UNDECORATED); // remove title bar of the main app window
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}