/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Model.Alerts.noUpcomingAppointments;
import static Model.Alerts.upcomingAppointments;
import Model.Appointments;
import Model.User;
import static Utils.DBQuerry.appointmentLoginChecker;
import static Utils.DBQuerry.login;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** This class defines the functionality of the Login Menu of the program. */
 
public class LoginController implements Initializable {

    Stage stage;
    Parent scene;
    
    @FXML
    private Label ZoneIDLabel;
    
    @FXML
    private Label LoginTitleLabel;
    
    @FXML
    private Button exitButton;

    @FXML
    private TextField usernameTF;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordTF;
    
    //Private String variables used for Resource Bundle Translations
    private String alertTitle;
    private String alertHeader;
    private String alertContent;
    private String zoneIDString;

    /** This defines the on Action event for the Login Button. This function is called when the Login Button is pressed. 
     @param event The mouse click on the Button.
     */
    //On Action for FXML buttons
    @FXML
    void onActionLogin(ActionEvent event) throws IOException {

        //Retrives text from usernameTF and passwordTF
        String usernameInput, passwordInput;
        usernameInput = usernameTF.getText();
        passwordInput = passwordTF.getText();
        
        //Validate whet
        if (login(usernameInput, passwordInput)) {
            
            //Changes Screens if Login was Successful
            System.out.println("Login Successful!");
            
            try{
                ObservableList<Appointments> var = appointmentLoginChecker(User.getUserID());
                if(var.isEmpty())
                {
                    noUpcomingAppointments();
                }
                else{
                    upcomingAppointments(var.get(0).getId(), var.get(0).getStart().toString());
                }
            }catch(Exception e)
            {
                System.out.println(e);
            }
            
            //Used to get time of user log in
            LocalDateTime now = LocalDateTime.now();
            //Formatting LDT object
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String newTimeNow = dtf.format(now);
            System.out.println("Local Time Zone: " + ZoneId.systemDefault().toString());
            System.out.println(Locale.getDefault().getCountry());
            System.out.println(newTimeNow);
            
            //Records the successful login attempts to text file at the root of source folder
            //Side Note: When testing on other machines, please update path of program file for the file name to work
            String fileName = "..\\GlobalAppointmentMangerV2\\login_activity.txt";
            FileWriter fWriter = new FileWriter(fileName, true);
            PrintWriter outputFile = new PrintWriter(fWriter);
            outputFile.println(User.getUserName() + " successfully logged in on " + newTimeNow); 
            System.out.println(User.getUserName() + " successfully logged in on " + newTimeNow); 
            
            //Closes file to save changes
            outputFile.close();
            
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            
        }
        else {
            //Username and/or password is incorrect will display Alert in either English or French
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle(alertTitle);
                alert.setHeaderText(alertHeader);
                alert.setContentText(alertContent); 
                alert.showAndWait();
                
                //Used to get time of user log in
            LocalDateTime now = LocalDateTime.now();
            //Formatting LDT object
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String newTimeNow = dtf.format(now);
            System.out.println("Local Time Zone: " + ZoneId.systemDefault().toString());
            System.out.println(Locale.getDefault().getCountry());
            System.out.println(newTimeNow);
            
            //Records the successful login attempts to text file at the root of source folder
            //Side Note: When testing on other machines, please update path of program file for the file name to work
            String fileName = "..\\GlobalAppointmentMangerV2\\login_activity.txt";
            FileWriter fWriter = new FileWriter(fileName, true);
            PrintWriter outputFile = new PrintWriter(fWriter);
            outputFile.println(usernameTF.getText() + " unsuccessfully logged in on " + newTimeNow); 
            System.out.println(usernameTF.getText() + " unsuccessfully logged in on " + newTimeNow); 
            
            //Closes file to save changes
            outputFile.close();

        }
    }

    /** This defines the on Action event for the Exit Button. This function is called when the Login Button is pressed.  
     @param event The mouse click on the Button.
     */
    @FXML
    void onActionExit(ActionEvent event) {
        //Closes Application
        System.exit(0);
    }
    
    /** Initializes the controller class. 
        @param location This is the FXML address sent from the Main function.
        @param resources This is the resource bundles sent from the Main function, if any.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        try
        {
            resources = ResourceBundle.getBundle("Languages/Nat", Locale.getDefault());
            if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en"))
            {
                //Fills in Alert with appropriate Language for User
                LoginTitleLabel.setText(resources.getString("LoginTitle"));
                usernameTF.setPromptText(resources.getString("Username"));
                passwordTF.setPromptText(resources.getString("Password"));
                exitButton.setText(resources.getString("Exit"));
                loginButton.setText(resources.getString("Login"));
                zoneIDString = resources.getString("ZoneID");
                alertTitle = resources.getString("alertTitle");
                alertHeader = resources.getString("alertHeader");
                alertContent = resources.getString("alertContent");
            
            }
            
            //Used to Display User's System Location
            ZoneIDLabel.setText(zoneIDString + ZoneId.systemDefault().toString());
            
            //Trouble shooting
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
            ZoneId targetTimeZone = ZoneId.of("UTC");
            LocalDateTime now = LocalDateTime.now();
            
            ZonedDateTime currentISTZoneDateTime = now.atZone(sourceTimeZone);
            ZonedDateTime now2 = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);
            
            System.out.println(ZoneId.systemDefault().toString());
            System.out.println(currentISTZoneDateTime);
            System.out.println(now);
            System.out.println(now2);
            
            LocalDateTime now3 = now2.toLocalDateTime();
            System.out.println(now3);
        
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }


    
}
