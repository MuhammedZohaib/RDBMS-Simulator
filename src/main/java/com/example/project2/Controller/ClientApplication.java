package com.example.project2.Controller;

import com.example.project2.DAO.DB_Logger;
import com.example.project2.DAO.Database;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class ClientApplication implements Initializable {

    @FXML
    private Label connectionStatus_Label;

    @FXML
    private ChoiceBox<String> dbURLChoice;

    @FXML
    private ChoiceBox<String> dbUserChoice;

    @FXML
    private PasswordField password;

    @FXML
    private TextArea queryBox;

    @FXML
    private TableView<ObservableList<String>> tableView;

    @FXML
    private TextField username;

    private String dbUsername;
    private String dbPassword;
    private String sqlCommand;
    private String dbURL;
    private ResultSet resultSet;

    private String tmpUser;
    private String tmpPwd;

    @FXML
    void clearResultWindow() {
        tableView.getColumns().clear();
    }

    @FXML
    void clearSQLCommand() {
        queryBox.clear();
    }

    @FXML
    void connectToDB() {
        dbUsername = username.getText().trim();
        dbPassword = password.getText().trim();

        if (dbURLChoice.getValue() != null && dbURLChoice.getValue().equals("project2.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(getClass().getResource("project2.properties")).getPath().replace("%20", " "))) {
                properties.load(fis);
                dbURL = properties.getProperty("url");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (dbURLChoice.getValue() != null && dbURLChoice.getValue().equals("bikedb.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(getClass().getResource("bikedb.properties")).
                    getPath().replace("%20", " "))) {
                properties.load(fis);
                dbURL = properties.getProperty("url");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (dbUserChoice != null && dbUserChoice.getValue().equals("root.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(getClass().getResource("root.properties")).getPath().replace("%20", " "))) {
                properties.load(fis);
                this.setTmpUser(properties.getProperty("username"));
                this.setTmpPwd(properties.getProperty("password"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (dbUserChoice != null && dbUserChoice.getValue().equals("client.properties")) {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Objects.requireNonNull(getClass().getResource("client.properties")).getPath().replace("%20", " "))) {
                properties.load(fis);
                this.setTmpUser(properties.getProperty("username"));
                this.setTmpPwd(properties.getProperty("password"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (dbUsername != null && dbPassword != null && !dbUsername.equals(tmpUser) && !dbPassword.equals(tmpPwd)) {
            connectionStatus_Label.setText("Invalid Credentials");
        } else {
            Database.connect(dbURL, dbUsername, dbPassword);
            connectionStatus_Label.setText("Connected to: " + dbURL);
            connectionStatus_Label.setStyle("-fx-background-color:  #000000; -fx-text-fill: #00ff00; -fx-padding: 5px 10px; -fx-border: none; -fx-background-radius: 5px;");
        }
    }

    @FXML
    void executeSQLCommand() {
        sqlCommand = queryBox.getText().trim();
        try{
            resultSet = Database.executeQuery(sqlCommand);
            DB_Logger.totalOperations();
            if(dbUserChoice.getValue().equals("root.properties")){
                DB_Logger.totalUpdates();
            }
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error executing SQL command");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            // Create columns dynamically
            ObservableList<TableColumn<ObservableList<String>, String>> columns = FXCollections.observableArrayList();
            for (int i = 1; i <= columnCount; i++) {
                final int columnIndex = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(resultSetMetaData.getColumnName(i));
                column.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().get(columnIndex - 1)));
                columns.add(column);
            }
            tableView.getColumns().setAll(columns);
            tableView.getColumns().forEach(column -> column.setMinWidth(150));

            // Populate data
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                data.add(row);
            }
            tableView.setItems(data);
        } catch (SQLException e) {
            System.out.println("Invalid SQL command");
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbURLChoice.getItems().add("project2.properties");
        dbURLChoice.getItems().add("bikedb.properties");

        dbUserChoice.getItems().add("root.properties");
        dbUserChoice.getItems().add("client.properties");
    }

    public void setTmpUser(String tmpUser) {
        this.tmpUser = tmpUser;
    }

    public void setTmpPwd(String tmpPwd) {
        this.tmpPwd = tmpPwd;
    }
}
