/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package com.mycompany.boilerplatejavafxdashboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inisialisasi Root Layout
            BorderPane root = new BorderPane();

            // Tampilkan Halaman LoginView terlebih dahulu
            LoginView loginView = new LoginView(primaryStage);
            root.setCenter(loginView.getView());

            // Buat Scene
            Scene scene = new Scene(root, 800, 600);

            // Atur Stage
            primaryStage.setTitle("Todo App");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
