package com.bptn.car_management_system.controllers;

import com.bptn.car_management_system.MainApplication;
import com.bptn.car_management_system.entities.User;
import com.bptn.car_management_system.entities.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    FXMLLoader loader;
    public static boolean actionConfirmed = false;
    public User user;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lblMessage;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    // handles user's login request, validate inputs and initiate next the stage of login process
    public void loginButtonOnAction(ActionEvent event) {

        // validate user entries
        if (txtUsername.getText().isEmpty()) {
            lblMessage.setText("Please enter your username");
            txtUsername.requestFocus();
            return;
        }

        if (txtPassword.getText().isEmpty()) {
            lblMessage.setText("Please enter your password");
            txtPassword.requestFocus();
            return;
        }

        try {
            // if valid user, store user info for use in the dashBoard
            user = UserDAO.validateLoginDetails(txtUsername.getText(), txtPassword.getText());

            if (user != null) {
                lblMessage.setText("You've successfully logged in!");
                switchToDashboard(event);
            } else {
                lblMessage.setText("Invalid username or password");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void cancelButtonOnAction(ActionEvent actionEvent) {
        showConfirmDialog("exit");
        if (actionConfirmed) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    // confirm if user really want to exit the application
    public static void showConfirmDialog(String message) {
        actionConfirmed = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");

        if(message.equals("exit")){
            alert.setHeaderText("Exiting the application...");
            alert.setContentText("Do you really want to close the application?");
        } else if(message.equals("logout")){
            alert.setHeaderText("Logging out of the application...");
            alert.setContentText("Do you really want to logout of the application?");
        }

        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            actionConfirmed = true;
        }
    }

    // after a successful login attempt, the user is redirected to the application dashboard
    public void switchToDashboard(ActionEvent event) {

        try {
            // Create an FXMLLoader instance
            loader = new FXMLLoader(MainApplication.class.getResource("Dashboard.fxml"));

            // Load the FXML file and get the root node (Parent)
            root = loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get the controller for the next view
        DashboardController dashboardController = loader.getController();
        dashboardController.setLoginInfo(user);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

        stage.setMaxWidth(970);
        stage.setMaxHeight(584);

        stage.centerOnScreen();
        stage.show();

        stage.setMaxWidth(970);
        stage.setMaxHeight(584);

    }

    // for a new user that has not registered, direct them to the registration screen
    public void switchToSignupScreen(ActionEvent event) {
        try {
            root = FXMLLoader.load(MainApplication.class.getResource("SignupScreen.fxml"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }
}