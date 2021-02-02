/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Appointments;
import Model.User;
import Utils.DBConnection;
import static Utils.DBQuerry.getAllAppointments;
import static Utils.DBQuerry.getMonthAppointments;
import static Utils.DBQuerry.getWeekAppointments;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/** This class defines the functionality of the Main Menu of the program. */

public class MainMenuController implements Initializable {
    
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
    
    @FXML
    private ToggleGroup filterType;
    
    @FXML
    private RadioButton ViewMonthRB;
    
    @FXML
    private RadioButton ViewWeekRB;

    @FXML
    private RadioButton ViewAllRB;
    
    @FXML
    private DatePicker appointmentDatePicker;


    /**
        Initializes the controller class. 
        @param url This is the FXML address sent from the Main function.
        @param rb This is the resource bundles sent from the Main function, if any.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
        
        //Prefills the Appointment TableView with all current Appointments    
        appointmentsTV.setItems(getAllAppointments());
        
        //Sets Current Date on Date Picker
        appointmentDatePicker.setValue(LocalDate.now());
        
    }    

    /** This defines the on Action event for the Add Customer Button. This function is called when the Add Customer Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/AddCustomer.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
    }

    /** This defines the on Action event for the Log Out Button. This function is called when the Add Customer Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
    @FXML
    void onActionLogOutBtn(ActionEvent event) throws IOException {

        User.setUserName(null);
        User.setUserID(null);
        User.setActiveState(false);
        User.setZoneID(null);
        DBConnection.closeConnection();
        
        System.out.println("Log Out Successful.");
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }
    /** This defines the on Action event for the Modify Customer Button. This function is called when the Modify Customer Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
    @FXML
    void onActionModifyCustomer(ActionEvent event) throws IOException {
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/ModifyCustomer.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
    }
    
    /** This defines the on Action event for the Add Appointment Button. This function is called when the Add Appointment Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
        @FXML
    void onActionAddAppointment(ActionEvent event) throws IOException {

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/AddAppointment.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
    }
    
    /** This defines the on Action event for the Modify Appointment Button. This function is called when the Modify Appointment Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
    @FXML
    void onActionModifyAppointment(ActionEvent event) throws IOException {
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/ModifyAppointment.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
    }

    /** This defines the on Action event for the View by Month Radio Button. This function is called when the View by Month Radio Button is pressed.  
     @param event The mouse click on the Radio Button.
     */
    @FXML
    void onActionViewMonthApps(ActionEvent event) {
        
        //Fills in the Appointments table view with appointments filtered by Month
        appointmentsTV.setItems(getMonthAppointments(appointmentDatePicker.getValue()));
        
    }
    /** This defines the on Action event for the View All Radio Button. This function is called when the View All Radio Button is pressed.  
     @param event The mouse click on the Radio Button.
     */
    @FXML
    void onActionViewAllApps(ActionEvent event) {
        
        //Fills the Appointment TableView with all current Appointments    
        appointmentsTV.setItems(getAllAppointments());

    }
    /** This defines the on Action event for the View by Week Radio Button. This function is called when the View by Week Radio Button is pressed.  
     @param event The mouse click on the Radio Button.
     */
    @FXML
    void onActionViewWeekApps(ActionEvent event) {
        //Fills the Appointment TableView with Appointments filtered by Week
        appointmentsTV.setItems(getWeekAppointments(appointmentDatePicker.getValue()));
    }
    
    /** This defines the on Action event for the View Reports Button. This function is called when the View Reports Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
    @FXML
    void onActionViewReports(ActionEvent event) throws IOException {
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/ViewAppointmentReports.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
    }
    
    /** This defines the on Action event for the Contact Schedule Button. This function is called when the Contact Schedule Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
    @FXML
    void onActionContactScheduleMenu(ActionEvent event) throws IOException {
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/ContactScheduleMenu.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
    }
    
}
