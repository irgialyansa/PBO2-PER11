/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package com.mycompany.boilerplatejavafxdashboard;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

public class RegisterView {
    private final Stage primaryStage;
    private UserOperations userOperations;

    public RegisterView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.web("#FAFAFA"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Title
        Label titleLabel = new Label("📝 Register Akun");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.DARKSLATEBLUE);

        // Username
        TextField usernameField = new TextField();
        usernameField.setPromptText("👤 Username");
        styleInputField(usernameField);

        // Password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("🔒 Password");
        styleInputField(passwordField);

        // Role
        ComboBox<String> roleComboBox = new ComboBox<>();
        ObservableList<String> roles = FXCollections.observableArrayList("User", "Admin");
        roleComboBox.setItems(roles);
        roleComboBox.setValue("User");
        styleInputField(roleComboBox);

        // Register Button
        Button registerButton = new Button("✅ Daftar Sekarang");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 6px;");
        registerButton.setPrefWidth(300);
        registerButton.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        registerButton.setEffect(new DropShadow(2, Color.gray(0.3)));

        // Link ke Login
        Label loginLink = new Label("🔁 Sudah punya akun? Login di sini.");
        loginLink.setTextFill(Color.BLUE);
        loginLink.setStyle("-fx-underline: true;");
        loginLink.setOnMouseClicked(event -> {
            LoginView loginView = new LoginView(primaryStage);
            Scene loginScene = new Scene(loginView.getView(), 800, 600);
            primaryStage.setScene(loginScene);
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();

            try {
                userOperations = new UserOperations();
            } catch (SQLException ex) {
                Logger.getLogger(RegisterView.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (userOperations.registerUser(username, password, role)) {
                showSuccess("✅ Registrasi berhasil! Silakan login.");
                LoginView loginView = new LoginView(primaryStage);
                primaryStage.setScene(new Scene(loginView.getView(), 800, 600));
            } else {
                showError("❌ Gagal mendaftar! Username mungkin sudah digunakan.");
            }
        });

        root.getChildren().addAll(titleLabel, usernameField, passwordField, roleComboBox, registerButton, loginLink);
        return root;
    }

    private void styleInputField(Control control) {
        control.setPrefWidth(300);
        control.setStyle("-fx-padding: 10px; -fx-background-radius: 6px; -fx-font-size: 14px;");
        control.setEffect(new DropShadow(1, Color.gray(0.2)));
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
