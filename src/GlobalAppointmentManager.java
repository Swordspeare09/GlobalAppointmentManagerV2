/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Utils.DBConnection;
import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** This class creates an application that creates, modifies, and deletes appointments, customers and reports for a fictitious company. */
public class GlobalAppointmentManager extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    /** This is the main method of the application. This is the first method that launches the application. 
     @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Initially start conncection to DB
        DBConnection.startConnection();
        
        launch(args);
        
        //Immediately Closes the DB Connection
        DBConnection.closeConnection();
    }
    
}
