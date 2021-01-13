/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/** This class defines the public Alerts Class. */
public class Alerts {
    
    
    public static void blankFieldMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("Blank Field");
        alert.setContentText(s + " Textfield needs to include a(n) " + s);  
        alert.showAndWait();
    }

    public static void emptyCountryRegionCB(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("No Country/Region Selected");
        alert.setContentText(s + " Selection Required");  
        alert.showAndWait();
    }
    
    public static void emptyCustomerCB(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("No Customer was Selected");
        alert.setContentText(s + " Selection Required");  
        alert.showAndWait();
    }
    
    public static void emptyContactCB(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("No Contact was Selected");
        alert.setContentText(s + " Selection Required");  
        alert.showAndWait();
    }
    
    public static void emptyStartTimeCB(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("No Start Time was Selected");
        alert.setContentText(s + " Selection Required");  
        alert.showAndWait();
    }
    
    public static void emptyEndTimeCB(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("No End Time was Selected");
        alert.setContentText(s + " Selection Required");  
        alert.showAndWait();
    }
    
    public static void emptyDatePicker(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("No Date was Selected");
        alert.setContentText(s + " Selection Required");  
        alert.showAndWait();
    }
    
    public static void emptySelection(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("No " + s + " was Selected");
        alert.setContentText(s + " Selection Required");  
        alert.showAndWait();
    }
    
    public static boolean confirmSelection(String s, Integer id, String type){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Confirmation of Deletion");
        alert.setHeaderText("Confirm " + s + " Deletion");
        alert.setContentText("Are you sure you want to Delete " + s + " with the id"
                            + " of:" + String.valueOf(id) + " Type:" + type + "?");
        alert.getDialogPane().setMinSize(300, 200);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    public static void checkForOfficeHours() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("Incorrect Appointment Hours were Selected");
        alert.setContentText("Time(s) selected outside of the office hours of 8:00am to 10:00pm EST"
                            + ". Please enter another time for appointment.");  
        alert.getDialogPane().setMinSize(300, 200);
        alert.showAndWait();
    
    }
    
    public static void upcomingAppointments(Integer id, String start)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setTitle("Upcoming Appointments");
        alert.setHeaderText("Upcoming Appoinmtents soon");
        alert.setContentText("You have an appointment soon of id:" + id +
                " and at " + start + ".");  
        alert.getDialogPane().setMinSize(300, 200);
        alert.showAndWait();
    }
    
    public static void noUpcomingAppointments()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setTitle("Empty Schedule");
        alert.setHeaderText("There are no upcoming Appoinmtents soon");
        alert.setContentText("You have no appointments scheduled soon.");  
        alert.showAndWait();
    }
    
    public static void overlappingAppointmentTimes(String s)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("Overlapping Appointment Hours were Selected");
        alert.setContentText("Time(s) selected for this " + s + " appointment are over with another appointment. Please "
                + "select another time for this appointment.");  
        alert.getDialogPane().setMinSize(300, 200);
        alert.showAndWait(); 
    }
}
