package com.bptn.car_management_system.controllers;

import com.bptn.car_management_system.MainApplication;
import com.bptn.car_management_system.entities.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

// Controller class that handles new user account registration
public class SignUpController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField txtUsername,
            txtFirstname,
            txtLastname,
            txtEmail,
            txtPassword,
            txtPlateNumber;

    @FXML
    private Label lblMessage;

    // method that handles transfer of control to the login screen after registration is completed
    public void switchToLoginScreen(ActionEvent event) {
        try {
            root = FXMLLoader.load(MainApplication.class.getResource("LoginScreen.fxml"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // method that takes user entries, validates them and if valid, initiates new user creation
    public void registerUser(ActionEvent event) throws SQLException {

        // validate user entries
        if (txtUsername.getText().isEmpty()) {
            lblMessage.setText("Please enter your username");
            txtUsername.requestFocus();
            return;
        }

        if (txtFirstname.getText().isEmpty()) {
            lblMessage.setText("Please enter your firstname");
            txtFirstname.requestFocus();
            return;
        }
        if(txtLastname.getText().isEmpty()) {
            lblMessage.setText("Please enter your lastname");
            txtLastname.requestFocus();
            return;
        }

        if(txtEmail.getText().isEmpty()) {
            lblMessage.setText("Please enter your email");
            txtEmail.requestFocus();
            return;
        }

        if (txtPassword.getText().isEmpty()) {
            lblMessage.setText("Please enter your password");
            txtPassword.requestFocus();
            return;
        }

        if(txtPlateNumber.getText().isEmpty()) {
            lblMessage.setText("Please enter your plate number");
            txtPlateNumber.requestFocus();
            return;
        }

        if(UserDAO.usernameExists(txtUsername.getText().trim())){
            lblMessage.setText("Username already exists! Enter a different username");
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean successful = userDAO.createUser(txtUsername.getText().trim(),
                txtFirstname.getText(),
                txtLastname.getText(),
                txtEmail.getText(),
                txtPassword.getText(),
                "user",
                txtPlateNumber.getText()
        );

        if(successful){
            lblMessage.setText("Registration successful! Exit and login to the application");
        }

    }
}
