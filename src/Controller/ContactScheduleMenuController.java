/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Appointments;
import static Utils.DBQuerry.getAllAppointmentsByContact;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author corte
 */
public class ContactScheduleMenuController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TableColumn<Appointments, LocalDateTime> appEndCol;

    @FXML
    private TableColumn<Appointments, Integer> appContactCol;

    @FXML
    private TableColumn<Appointments, String> appTypeCol;

    @FXML
    private TableView<Appointments> appointmentsTV;

    @FXML
    private TableColumn<Appointments, String> appTitleCol;

    @FXML
    private TableColumn<Appointments, String> appDescrCol;

    @FXML
    private TableColumn<Appointments, Integer> appCustIDCol;

    @FXML
    private TableColumn<Appointments, String> appLocCol;

    @FXML
    private TableColumn<Appointments, Integer> appIDCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> appStartCol;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Loads All Appointments from the beginning
        appIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appDescrCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appCustIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        
    }    

    @FXML
    private void onActionLLSchedule(ActionEvent event) {
        //Fills in the Appointments table view with appointments filtered by Contact ID of Li Lee
        appointmentsTV.setItems(getAllAppointmentsByContact(3));
    }

    @FXML
    private void onActionDGSchedule(ActionEvent event) {
        //Fills in the Appointments table view with appointments filtered by Contact ID of Daniel Garcia
        appointmentsTV.setItems(getAllAppointmentsByContact(2));
    }

    @FXML
    private void onActionACSchedule(ActionEvent event) {
        //Fills in the Appointments table view with appointments filtered by Contact ID of Anika Costa
        appointmentsTV.setItems(getAllAppointmentsByContact(1));
    }

    @FXML
    private void onActionReturnToMainMenu(ActionEvent event) throws IOException {
        
        //Used to Return to main Menu Screen
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
        
    }
    
}
