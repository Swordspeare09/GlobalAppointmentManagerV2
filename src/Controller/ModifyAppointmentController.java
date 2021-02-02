/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Model.Alerts.blankFieldMessage;
import static Model.Alerts.checkForOfficeHours;
import static Model.Alerts.confirmSelection;
import static Model.Alerts.emptyContactCB;
import static Model.Alerts.emptyCustomerCB;
import static Model.Alerts.emptyDatePicker;
import static Model.Alerts.emptyEndTimeCB;
import static Model.Alerts.emptySelection;
import static Model.Alerts.emptyStartTimeCB;
import static Model.Alerts.overlappingAppointmentTimes;
import Model.Appointments;
import Model.Contact;
import Model.Customer;
import Model.Users;
import Model.conversionCheckForOfficeHours;
import static Utils.DBQuerry.appointmentModifyOverlapByCustomer;
import static Utils.DBQuerry.appointmentModifyOverlapCheckByContact;
import static Utils.DBQuerry.appointmentModifyOverlapCheckByUser;
import static Utils.DBQuerry.deleteAppointment;
import static Utils.DBQuerry.getAllAppointments;
import static Utils.DBQuerry.getAllContacts;
import static Utils.DBQuerry.getAllCustomers;
import static Utils.DBQuerry.getAllUsers;
import static Utils.DBQuerry.modifyAppointment;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/** This class defines the functionality of the Add Appointment Menu of the program. */
public class ModifyAppointmentController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField appTitleTF;
    @FXML
    private TextField appDescriptionTF;
    @FXML
    private TextField appTypeTF;
    @FXML
    private TextField appLocationTF;
    @FXML
    private ComboBox<Contact> contactCB;
    @FXML
    private TextField appIDTF;
    @FXML
    private TableView<Appointments> appointmentsTV;
    @FXML
    private TableColumn<Appointments, Integer> appIDCol;
    @FXML
    private TableColumn<Appointments, String> appTitleCol;
    @FXML
    private TableColumn<Appointments, String> appDescriptionCol;
    @FXML
    private TableColumn<Appointments, String> appLocationCol;
    @FXML
    private TableColumn<Appointments, Integer> appContactCol;
    @FXML
    private TableColumn<Appointments, String> appTypeCol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appStartCol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appEndCol;
    @FXML
    private TableColumn<Appointments, Integer> appCustIDCol;
    @FXML
    private DatePicker appDatePicker;
    @FXML
    private ComboBox<Customer> customerCB;
    @FXML
    private ComboBox<LocalTime> startTimeCB;
    @FXML
    private ComboBox<LocalTime> endTimeCB;
    @FXML
    private ComboBox<Users> userCB;

    /**
        Initializes the controller class. 
        @param url This is the FXML address sent from the Main function.
        @param rb This is the resource bundles sent from the Main function, if any.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
            contactCB.setItems(getAllContacts());
            customerCB.setItems(getAllCustomers());
            userCB.setItems(getAllUsers());
        }catch(SQLException ex){
            Logger.getLogger(ModifyAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Used to fill the Start and End time combo boxes
        LocalTime start = LocalTime.of(7, 0);
        LocalTime end = LocalTime.of(22, 0);
        
        while(start.isBefore(end.plusSeconds(1))){
            startTimeCB.getItems().add(start);
            endTimeCB.getItems().add(start);
            start = start.plusMinutes(30);
        }
        
        //Sets Date to Current Date on DatePicker
        appDatePicker.setValue(LocalDate.now());
        
        // Loads All Appointments from the beginning
        appIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appCustIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        appointmentsTV.setItems(getAllAppointments());
        
        // Intitially makes all User Input Fields disabled
        appTitleTF.setDisable(true);
        appDescriptionTF.setDisable(true);
        appTypeTF.setDisable(true);
        appLocationTF.setDisable(true);
        appDatePicker.setDisable(true);
        customerCB.setDisable(true);
        startTimeCB.setDisable(true);
        endTimeCB.setDisable(true);
        contactCB.setDisable(true);
        userCB.setDisable(true);
        
    }    

     /** *  This defines the on Action event for the Save Button.This function is called when the Save Button is pressed. The program takes the User's TextField input 
 and validates before calling the modifyAppointment function. Throws SQLException if Alerts class messages do not catch errors. The Lambda Expression defined in this function
 is used to convert the User's times selected and converts them to UTC. 
     @param event The mouse click on the Button.
     * @throws SQLException Throws an SQL Exception if there is problem with the DataBase or the credentials. 
     */
    @FXML
    public void onActionModifyAppointment(ActionEvent event) throws SQLException {

        conversionCheckForOfficeHours validateOfficeHours = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to validate incoming appointment times are within office hours            
            ZoneId targetTimeZone = ZoneId.of("America/New_York");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime();
            
            if(ldt.isAfter(LocalDateTime.of(ldt.toLocalDate(), LocalTime.of(8, 0))) && ldt.isBefore(LocalDateTime.of(ldt.toLocalDate(), LocalTime.of(22, 0))))
                return true;
            else
                return false;
            };
        
        try{
            //Checks to see if an appointment was chosen prior to running function call
            Appointments modApp = appointmentsTV.getSelectionModel().getSelectedItem();
            appIDTF.setText(String.valueOf(modApp.getId()));
            
            String tID = appIDTF.getText();
            String tTitle = appTitleTF.getText();
            String tDescr = appDescriptionTF.getText();
            String tLocation =  appLocationTF.getText();
            String tType =  appTypeTF.getText();
            Customer tCustomer = null;
            Contact tContact = null;
            Users tUser = null;
            LocalTime startTime = null;
            LocalTime endTime = null;
            LocalDate appDate = null;
        
            if(!tTitle.isEmpty() && !tDescr.isEmpty() && !tLocation.isEmpty() && !tType.isEmpty())
            {
                tCustomer = customerCB.getSelectionModel().getSelectedItem();
                tContact = contactCB.getSelectionModel().getSelectedItem();
                tUser = userCB.getSelectionModel().getSelectedItem();
                startTime = startTimeCB.getSelectionModel().getSelectedItem();
                endTime = endTimeCB.getSelectionModel().getSelectedItem();
                appDate = appDatePicker.getValue();
            
            if( tCustomer instanceof Customer && tContact instanceof Contact && tUser instanceof Users && startTime instanceof LocalTime && endTime instanceof LocalTime && appDate instanceof LocalDate)
            {
                if(validateOfficeHours.conversionCheckForOfficeHours(LocalDateTime.of(appDate, startTime)) && validateOfficeHours.conversionCheckForOfficeHours(LocalDateTime.of(appDate, endTime)))
                {
                    if(appointmentModifyOverlapByCustomer(LocalDateTime.of(appDate, startTime), LocalDateTime.of(appDate, endTime), tCustomer.getId(), Integer.parseInt(tID)))
                    {
                        if(appointmentModifyOverlapCheckByUser(LocalDateTime.of(appDate, startTime), LocalDateTime.of(appDate, endTime), tUser.getUserID(), Integer.parseInt(tID))) 
                        {
                            if(appointmentModifyOverlapCheckByContact(LocalDateTime.of(appDate, startTime), LocalDateTime.of(appDate, endTime), tContact.getId(), Integer.parseInt(tID)))
                            {
                                //Creates new Appointments Object to send to add Appointment function call
                                Appointments tempApp = new Appointments(
                                            Integer.parseInt(tID),
                                            tTitle,
                                            tType,
                                            tDescr,
                                            tLocation,
                                            LocalDateTime.of(appDate, startTime),
                                            LocalDateTime.of(appDate, endTime),
                                            tCustomer.getId(),
                                            tUser.getUserID(),
                                            tContact.getId() 
                                            );
                
                                //Sends new app created to function call
                                modifyAppointment(tempApp, tUser);
                                //Clears out User Fields
                                onActionClearFields(event);
                                //Reloads Table View with new data from Database
                                appointmentsTV.setItems(getAllAppointments());
                            } else{
                                overlappingAppointmentTimes("Contact's");
                            }
                        } else {
                            overlappingAppointmentTimes("User's");
                        }
                    }else{
                        overlappingAppointmentTimes("Customer's");
                    }
                }else{
                    checkForOfficeHours();
                }
            } else{
                //Used to Display Appropriate Error Messages for User 
                if(tCustomer == null)
                    emptyCustomerCB("Customer");
                if(tContact == null)
                    emptyContactCB("Contact");
                if(tUser == null)
                    emptySelection("User");
                if(startTime == null)
                    emptyStartTimeCB("Start Time");
                if(endTime == null)
                    emptyEndTimeCB("End Time");
                if(appDate == null)
                    emptyDatePicker("Date");
            }
            }else{
                //Used to Display Appropriate Error Messages for User 
                if(tTitle.isEmpty())
                    blankFieldMessage("Title");
                if(tDescr.isEmpty())
                    blankFieldMessage("Description");
                if(tLocation.isEmpty())
                    blankFieldMessage("Location");
                if(tType.isEmpty())
                    blankFieldMessage("Type");
            }
        }catch(NullPointerException e){
            //Used to display appopriate alert for User when trying to push the Edit Appointment button with no Appointment selected
            emptySelection("Appoinment");
        }
        
    }
    
    /** This defines the on Action event for the Back Button. This function is called when the Back Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionReturnMainMenu(ActionEvent event) throws IOException {
        
        //Used to Return to main Menu Screen
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
        
    }

    @FXML
    private void onActionEditApointment(ActionEvent event) {
        try{
        //Creates temporary object
        Appointments modApp = appointmentsTV.getSelectionModel().getSelectedItem();
        
        //Assigns text values to textfields
        appIDTF.setText(String.valueOf(modApp.getId()));
        appTitleTF.setText(modApp.getTitle());
        appDescriptionTF.setText(modApp.getDescription());
        appLocationTF.setText(modApp.getLocation());
        appTypeTF.setText(modApp.getType());
        
        //Used to autofill Customer Combo Box
        for(int i = 0; i < customerCB.getItems().size(); i++)
        {
            Customer selCustomer = customerCB.getItems().get(i);
            if(selCustomer.getId() == modApp.getCustomerID())
            {
             customerCB.setValue(selCustomer);
             break;
            }
        
        }
        
        //Used to autofill Contact Combo Box
        for(int i = 0; i < contactCB.getItems().size(); i++)
        {
            Contact selContact = contactCB.getItems().get(i);
            if(selContact.getId() == modApp.getContactID())
            {
             contactCB.setValue(selContact);
             break;
            }
        
        }
        
        //Used to autofill User Combo Box
        for(int i = 0; i < userCB.getItems().size(); i++)
        {
            Users selUser = userCB.getItems().get(i);
            if(Objects.equals(selUser.getUserID(), modApp.getUserID()))
            {
             userCB.setValue(selUser);
             break;
            }
        }
        
        //Used to autofill Start Time and End Time combo Box and Date Picker 
        startTimeCB.setValue(modApp.getStart().toLocalTime());        
        endTimeCB.setValue(modApp.getEnd().toLocalTime());
        appDatePicker.setValue(modApp.getStart().toLocalDate());
        
        //Enables the user to adjust the fields as neccessary 
        appTitleTF.setDisable(false);
        appDescriptionTF.setDisable(false);
        appTypeTF.setDisable(false);
        appLocationTF.setDisable(false);
        appDatePicker.setDisable(false);
        customerCB.setDisable(false);
        startTimeCB.setDisable(false);
        endTimeCB.setDisable(false);
        contactCB.setDisable(false);
        userCB.setDisable(false);
       
        }catch(NullPointerException e) {
            
            //Used to display appopriate alert for User when trying to push the Edit Appointment button with no Appointment selected
            emptySelection("Appointment");
        }
    }
    
    /** This defines the on Action event for the Delete Button. This function is called when the Delete Button is pressed. The program takes the User's selection and 
     * validates to make sure an Appointments Object was selected from the TableView. After validation, the program will display and Alert message from the Alerts class to confirm with user
     * before calling the deleteAppointment function. The program will also reload the items in the TableView with with the getAllAppointments function.Will catch NullPointerException and display an Alert message from the Alerts class. 
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionDeleteAppointment(ActionEvent event) {
        try
        {
            //Checks to see if an appointment was chosen prior to running function call
            Appointments modApp = appointmentsTV.getSelectionModel().getSelectedItem();
            appIDTF.setText(String.valueOf(modApp.getId()));
            
            //If User confirms Selection, then the program continues
            if(confirmSelection("Appointment", modApp.getId(), modApp.getType()) == true) 
            {
                //After Confrimation, Selected Appointment is Deleted
                deleteAppointment(modApp);
                //Reloads Table View with new data from Database
                appointmentsTV.setItems(getAllAppointments());
                //Clears out and disables all User fields
                onActionClearFields(event);
            }
            
            appIDTF.setText(null);

        }catch(NullPointerException e) {
            
            //Used to display appopriate alert for User when trying to push the Edit Appointment button with no Appointment selected
            emptySelection("Appoinment");
        }
    }
    
    /** This defines the on Action event for the Clear Button. This function is called when the Save Button is pressed. The Menu TextFields are cleared out.
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionClearFields(ActionEvent event) {
        
        
        //Clears out all TextFields and Combo Box Selections
        appIDTF.clear();
        appTitleTF.clear();
        appDescriptionTF.clear();
        appTypeTF.clear();
        appLocationTF.clear();
        contactCB.getSelectionModel().clearSelection();
        customerCB.getSelectionModel().clearSelection();
        startTimeCB.getSelectionModel().clearSelection();
        userCB.getSelectionModel().clearSelection();
        endTimeCB.getSelectionModel().clearSelection();
        appointmentsTV.getSelectionModel().clearSelection();
        
        //Resets Date Picker to Current Date
        appDatePicker.setValue(LocalDate.now());
        
        //Resets All Text fields and Combo Box Back to Disabled
        appTitleTF.setDisable(true);
        appDescriptionTF.setDisable(true);
        appTypeTF.setDisable(true);
        appLocationTF.setDisable(true);
        appDatePicker.setDisable(true);
        customerCB.setDisable(true);
        startTimeCB.setDisable(true);
        endTimeCB.setDisable(true);
        contactCB.setDisable(true);
        userCB.setDisable(true);
    }
    
}
