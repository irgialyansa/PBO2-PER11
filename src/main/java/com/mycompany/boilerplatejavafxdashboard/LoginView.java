/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package com.mycompany.boilerplatejavafxdashboard;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {
    private Stage primaryStage;
    private UserOperations userOperations;

    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.web("#FAFAFA"), CornerRadii.EMPTY, Insets.EMPTY)));

        Label titleLabel = new Label("🔐 Login ke Akun Anda");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        TextField usernameField = new TextField();
        usernameField.setPromptText("👤 Username");
        styleInput(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("🔑 Password");
        styleInput(passwordField);

        Button loginButton = new Button("➡️ Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8px;");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        loginButton.setEffect(new DropShadow(2, Color.gray(0.4)));

        Label registerLink = new Label("❓ Belum punya akun? Register di sini.");
        registerLink.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        registerLink.setTextFill(Color.web("#1E88E5"));
        registerLink.setStyle("-fx-underline: true;");
        registerLink.setOnMouseClicked(event -> {
            RegisterView registerView = new RegisterView(primaryStage);
            Scene registerScene = new Scene(registerView.getView(), 800, 600);
            primaryStage.setScene(registerScene);
        });

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                userOperations = new UserOperations();
            } catch (SQLException ex) {
                Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (userOperations.loginUser(username, password)) {
                DashboardView dashboardView = new DashboardView(primaryStage, username);
                Scene dashboardScene = new Scene(dashboardView.getView(), 800, 600);
                primaryStage.setScene(dashboardScene);
            } else {
                showError("❌ Login gagal! Periksa username dan password Anda.");
            }
        });

        VBox card = new VBox(15, titleLabel, usernameField, passwordField, loginButton, registerLink);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        card.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        card.setEffect(new DropShadow(5, Color.gray(0.5)));

        root.getChildren().add(card);
        return root;
    }

    private void styleInput(TextField field) {
        field.setPrefWidth(300);
        field.setFont(Font.font("Arial", 14));
        field.setPadding(new Insets(10));
        field.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        field.setStyle("-fx-border-color: #BDBDBD; -fx-border-radius: 5px;");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Gagal");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
