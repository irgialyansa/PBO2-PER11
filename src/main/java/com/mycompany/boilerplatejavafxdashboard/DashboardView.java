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

public class DashboardView {
    private Stage primaryStage;
    private UserOperations userOperations;
    private String username;

    public DashboardView(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.web("#F5F5F5"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Sidebar Navigasi
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(25));
        menu.setAlignment(Pos.TOP_LEFT);
        menu.setPrefWidth(200);
        menu.setBackground(new Background(new BackgroundFill(Color.web("#E0E0E0"), new CornerRadii(12), Insets.EMPTY)));

        Label titleLabel = new Label("📋 Todo Apps");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#333"));

        Button todoButton = new Button("📝 Lihat Todos");
        Button logoutButton = new Button("🚪 Logout");

        styleSidebarButton(todoButton);
        styleSidebarButton(logoutButton);

        todoButton.setOnAction(e -> {
            TodoView todoView = new TodoView(primaryStage, username);
            primaryStage.setScene(new Scene(todoView.getView(), 800, 600));
        });

        logoutButton.setOnAction(e -> {
            LoginView loginView = new LoginView(primaryStage);
            primaryStage.setScene(new Scene(loginView.getView(), 800, 600));
        });

        menu.getChildren().addAll(titleLabel, todoButton, logoutButton);
        root.setLeft(menu);

        // Konten Utama
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_LEFT);

        Label welcomeLabel = new Label("👋 Selamat datang, " + username);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.DARKSLATEBLUE);

        try {
            userOperations = new UserOperations();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
        }

        User user = userOperations.getProfile(username);

        VBox profileCard = new VBox(10);
        profileCard.setPadding(new Insets(20));
        profileCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        profileCard.setEffect(new DropShadow(3, Color.gray(0.5)));

        Label profileHeader = new Label("👤 Profil Pengguna");
        profileHeader.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        profileHeader.setTextFill(Color.web("#424242"));

        if (user != null) {
            Label profileLabel = new Label("🧑 Username: " + user.getUsername());
            Label roleLabel = new Label("🔐 Role: " + user.getRole());
            profileLabel.setFont(Font.font(14));
            roleLabel.setFont(Font.font(14));
            profileCard.getChildren().addAll(profileHeader, profileLabel, roleLabel);
        } else {
            profileCard.getChildren().add(new Label("⚠️ Gagal memuat profil pengguna."));
        }

        content.getChildren().addAll(welcomeLabel, profileCard);
        root.setCenter(content);

        return root;
    }

    private void styleSidebarButton(Button button) {
        button.setPrefWidth(160);
        button.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8px;");
        button.setEffect(new DropShadow(2, Color.gray(0.3)));
    }
}
