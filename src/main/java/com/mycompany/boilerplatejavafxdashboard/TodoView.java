/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package com.mycompany.boilerplatejavafxdashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

import java.sql.SQLException;

public class TodoView {
    private TodoOperations todoOperations;
    private TableView<Todo> tableView;
    private ObservableList<Todo> todoList;
    private Stage primaryStage;
    private String username;

    public TodoView(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;
        try {
            todoOperations = new TodoOperations();
            todoList = FXCollections.observableArrayList(todoOperations.getTodos());
        } catch (SQLException e) {
            showError("❌ Gagal memuat data: " + e.getMessage());
        }
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.web("#FAFAFA"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Sidebar
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(20));
        menu.setPrefWidth(200);
        menu.setBackground(new Background(new BackgroundFill(Color.web("#E0E0E0"), new CornerRadii(10), Insets.EMPTY)));

        Label menuTitle = new Label("📋 Todo Apps");
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        menuTitle.setTextFill(Color.web("#333"));

        Button dashboardButton = new Button("🏠 Dashboard");
        Button logoutButton = new Button("🚪 Logout");
        styleMenuButton(dashboardButton);
        styleMenuButton(logoutButton);

        dashboardButton.setOnAction(e -> {
            DashboardView dashboardView = new DashboardView(primaryStage, username);
            primaryStage.setScene(new Scene(dashboardView.getView(), 800, 600));
        });

        logoutButton.setOnAction(e -> {
            LoginView loginView = new LoginView(primaryStage);
            primaryStage.setScene(new Scene(loginView.getView(), 800, 600));
        });

        menu.getChildren().addAll(menuTitle, dashboardButton, logoutButton);
        root.setLeft(menu);

        // Konten Utama
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("📝 Daftar Kegiatan");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button addButton = new Button("➕ Tambah Todo");
        addButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 6px;");
        addButton.setFont(Font.font(14));
        addButton.setOnAction(e -> showTodoModal(null));

        tableView = new TableView<>();
        tableView.setItems(todoList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Todo, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        TableColumn<Todo, String> titleColumn = new TableColumn<>("Judul");
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());

        TableColumn<Todo, String> descriptionColumn = new TableColumn<>("Deskripsi");
        descriptionColumn.setCellValueFactory(data -> data.getValue().descriptionProperty());

        TableColumn<Todo, Boolean> isCompletedColumn = new TableColumn<>("✅ Selesai");
        isCompletedColumn.setCellValueFactory(data -> data.getValue().isCompletedProperty());

        TableColumn<Todo, Void> actionColumn = new TableColumn<>("⚙️ Aksi");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("✏️");
            private final Button deleteButton = new Button("🗑️");
            private final Button completeButton = new Button("✅");

            {
                editButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
                completeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Todo selected = getTableView().getItems().get(getIndex());

                    editButton.setOnAction(e -> showTodoModal(selected));
                    deleteButton.setOnAction(e -> {
                        todoOperations.deleteTodo(selected.getId());
                        refreshTable();
                        showSuccess("🗑️ Todo dihapus!");
                    });
                    completeButton.setOnAction(e -> {
                        todoOperations.markAsCompleted(selected.getId());
                        refreshTable();
                        showSuccess("✅ Todo ditandai selesai!");
                    });

                    HBox box = new HBox(5, editButton, deleteButton, completeButton);
                    box.setPadding(new Insets(5));
                    setGraphic(box);
                }
            }
        });

        actionColumn.setMinWidth(280);
        tableView.getColumns().addAll(idColumn, titleColumn, descriptionColumn, isCompletedColumn, actionColumn);

        content.getChildren().addAll(titleLabel, addButton, tableView);
        root.setCenter(content);

        return root;
    }

    private void showTodoModal(Todo todo) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle(todo == null ? "➕ Tambah Todo" : "✏️ Edit Todo");

        Label titleLabel = new Label("📝 Judul:");
        TextField titleField = new TextField(todo == null ? "" : todo.getTitle());

        Label descLabel = new Label("📄 Deskripsi:");
        TextField descriptionField = new TextField(todo == null ? "" : todo.getDescription());

        Button saveButton = new Button(todo == null ? "Tambah" : "Simpan");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            if (todo == null) {
                todoOperations.addTodo(new Todo(0, titleField.getText(), descriptionField.getText(), false, null));
                showSuccess("✅ Todo ditambahkan!");
            } else {
                todoOperations.updateTodo(todo.getId(), titleField.getText(), descriptionField.getText());
                showSuccess("✏️ Todo diperbarui!");
            }
            refreshTable();
            modalStage.close();
        });

        VBox modalContent = new VBox(10, titleLabel, titleField, descLabel, descriptionField, saveButton);
        modalContent.setPadding(new Insets(20));
        modalContent.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(6), Insets.EMPTY)));

        Scene modalScene = new Scene(modalContent, 320, 250);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    private void refreshTable() {
        todoList.setAll(todoOperations.getTodos());
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

    private void styleMenuButton(Button button) {
        button.setPrefWidth(160);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 6px;");
        button.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        button.setEffect(new DropShadow(2, Color.gray(0.3)));
    }
}
