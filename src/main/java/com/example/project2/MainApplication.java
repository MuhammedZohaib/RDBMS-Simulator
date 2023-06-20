package com.example.project2;

import com.example.project2.DAO.DB_Logger;
import com.example.project2.DAO.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ClientApplication.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Project 2 !");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        stage.onCloseRequestProperty().setValue(e -> {
            System.exit(0);
            Database.disconnect();
        });
        DB_Logger.connect();
    }

    public static void main(String[] args) {
        launch();}
}