/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Utils.DBQuerry.getAllAppointmentReport;
import static Utils.DBQuerry.getAppointmentByContactReport;
import static Utils.DBQuerry.getAppointmentByTypeReport;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** This class defines the functionality of the View Reports Menu of the program. */
public class ViewAppointmentReportsController implements Initializable {

    Stage stage;
    Parent scene;
    
    @FXML
    private Button returnMainMenuBtn;
    @FXML
    private TextField totalAppsReportLabel;
    @FXML
    private TextField totalTypeAppsReportLabel;
    @FXML
    private TextField typeSearchTF;
    @FXML
    private TextField totalDGReportsLabel;
    @FXML
    private TextField totalACReportsLabel;
    @FXML
    private TextField totalLLReportsLabel;

    /**
        Initializes the controller class. 
        @param url This is the FXML address sent from the Main function.
        @param rb This is the resource bundles sent from the Main function, if any.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
            totalAppsReportLabel.setText(String.valueOf(getAllAppointmentReport()));
            totalDGReportsLabel.setText(String.valueOf(getAppointmentByContactReport(2)));
            totalACReportsLabel.setText(String.valueOf(getAppointmentByContactReport(1)));
            totalLLReportsLabel.setText(String.valueOf(getAppointmentByContactReport(3)));

        }catch(Exception e)
        {
            System.out.print(e);
        }
    }    
    
    /** This defines the on Action event for the Back Button. This function is called when the Back Button is pressed. This will change load another scene.
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionReturnMainMenu(ActionEvent event) throws IOException {
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
	stage.setScene(new Scene(scene));
	stage.show();
    }
    
    /** This defines the on Action event for the Search Button. This function is called when the Search Button is pressed. The function will call the getAppointmentByTypeReport
     * function where it will change the totalTypeAppsReportLabel accordingly.
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionAppointmentTypeSearch(ActionEvent event) {
        String s = typeSearchTF.getText();
        
        if(!s.isEmpty())
        {
            try{
                totalTypeAppsReportLabel.setText(String.valueOf(getAppointmentByTypeReport(s)));
            }catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }
    
}
