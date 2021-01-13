/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Model.Alerts.blankFieldMessage;
import static Model.Alerts.confirmSelection;
import static Model.Alerts.emptyCountryRegionCB;
import static Model.Alerts.emptySelection;
import Model.Country;
import Model.Customer;
import Model.Region;
import static Utils.DBQuerry.addCustomer;
import static Utils.DBQuerry.deleteCustomer;
import static Utils.DBQuerry.deleteCustomerAppointments;
import static Utils.DBQuerry.getAllCustomers;
import static Utils.DBQuerry.getCountries;
import static Utils.DBQuerry.getNewID;
import static Utils.DBQuerry.getRegions;
import static Utils.DBQuerry.modifyCustomer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/** This class defines the functionality of the Modify Customer Menu of the program. */
public class ModifyCustomerController implements Initializable {

    Stage stage;
    Parent scene;
    
    @FXML
    private Button returnMainMenuBtn;
    @FXML
    private TextField cNameTF;
    @FXML
    private TextField cAddressTF;
    @FXML
    private TextField cPhoneTF;
    @FXML
    private TextField cPostalCodeTF;
    @FXML
    private ComboBox<Region> regionCB;
    @FXML
    private ComboBox<Country> countryCB;
    @FXML
    private TextField cIDTF;
    @FXML
    private TableView<Customer> customersTV;
    @FXML
    private TableColumn<Customer, Integer> customerIDCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    @FXML
    private TableColumn<Customer, String> customerZipCol;
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;
    @FXML
    private TableColumn<Customer, String> customerDivCol;
    @FXML
    private TableColumn<Customer, String> customerCountryCol;

    /**
        Initializes the controller class. 
        @param url This is the FXML address sent from the Main function.
        @param rb This is the resource bundles sent from the Main function, if any.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Intitially makes all User Input Fields disabled
        cNameTF.setDisable(true);
        cAddressTF.setDisable(true);
        countryCB.setDisable(true);
        regionCB.setDisable(true);
        cPostalCodeTF.setDisable(true);
        cPhoneTF.setDisable(true);
        cIDTF.setDisable(true);
        
        //Fills the Customer Table
        try {
            customersTV.setItems(getAllCustomers());
        }catch (SQLException ex) {
            Logger.getLogger(ModifyCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Sets Cell values for each column used in Customer table        
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerZipCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        
        countryCB.setItems(getCountries());
        
    }    

        /** This defines the on Action event for the Save Button. This function is called when the Save Button is pressed. The program takes the User's TextField input 
     * and validates before calling the modifyCustomer function. Validation is used through the Alerts Class alert boxes.
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionUpdateCustomer(ActionEvent event) {
        
        String tID = cIDTF.getText();
        String tName = cNameTF.getText();
        String tAddress = cAddressTF.getText();
        String tPost =  cPostalCodeTF.getText();
        String tPhone =  cPhoneTF.getText();
        String tRegion = null;
        String tCountry = null ;

            //Validating user input before creating any objects or queries
            if(!tName.isEmpty() && !tAddress.isEmpty() && !tPost.isEmpty() && !tPhone.isEmpty())
            {
                
                try{
                    //Creates new customer object for SQL INSERT statement
                    Customer tempCust = new Customer(
                                        Integer.parseInt(tID), 
                                        tName, tAddress, 
                                        tPost, 
                                        tPhone, 
                                        tRegion, 
                                        regionCB.getSelectionModel().getSelectedItem().getId(),
                                        tCountry,
                                        countryCB.getSelectionModel().getSelectedItem().getId());
                    modifyCustomer(tempCust, regionCB.getSelectionModel().getSelectedItem().getId());
                
                    //Loads up Main Menu Screen
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }catch(Exception e){
                    //Alert informs user that no country or region was selected
                    emptyCountryRegionCB("Country/Region");
                }
            } else{
                //Used to Display Appropriate Error Messages for User 
                if(tName.isEmpty())
                    blankFieldMessage("Name");
                if(tAddress.isEmpty())
                    blankFieldMessage("Address");
                if(tPost.isEmpty())
                    blankFieldMessage("Postal Code");
                if(tPhone.isEmpty())
                    blankFieldMessage("Phone Number");
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
    /** This defines the on Action event for the Clear Button. This function is called when the Save Button is pressed. The Menu TextFields are cleared out.
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionClearFields(ActionEvent event) {
        
        //Clears the selected Customer from TableView
        customersTV.getSelectionModel().clearSelection();
        
        //Clears out all Text Fields and Combo Boxes 
        cIDTF.clear();
        cNameTF.clear();
        cAddressTF.clear();
        cPostalCodeTF.clear();
        cPhoneTF.clear();
        countryCB.getSelectionModel().clearSelection();
        regionCB.getSelectionModel().clearSelection();
        
        // Resets all User Input Fields back to disabled
        cNameTF.setDisable(true);
        cAddressTF.setDisable(true);
        countryCB.setDisable(true);
        cPostalCodeTF.setDisable(true);
        cPhoneTF.setDisable(true);
        cIDTF.setDisable(true);
        
        //Used to reset Regions Combo Box 
        regionCB.setItems(null);
        regionCB.setPromptText("Region");
        regionCB.setDisable(true);//Used to prevent errors of loading incorrect regions
    }
    
    /** This defines the on Action event for the Country ComboBox. This function is called when the User selects a Country from the Country ComboBox. This will validate and 
     * fill the Region ComboBox with corresponding regions of the Country Selected.
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionCountrySelected(ActionEvent event) {
        
        //Used to fill in Region Combo Box with the appropriate Regions
        if(countryCB.getSelectionModel().getSelectedItem() instanceof Country)
        {
            Country selectedCountry = countryCB.getSelectionModel().getSelectedItem();
            regionCB.setItems(getRegions(selectedCountry));
            regionCB.setPromptText("Choose a Region");
            regionCB.setDisable(false);
        }
    }
    
    /** This defines the on Action event for the Delete Button. This function is called when the Delete Button is pressed. The program takes the User's selection and 
     * validates to make sure a Customer Object was selected from the TableView. After validation, the program will display and Alert message from the Alerts class to confirm with user
     * before calling the deleteCustomerAppointments function and the deleteCustomer function. The program will also reload the items in the TableView with with the getAllCustomers 
     * function.Will catch NullPointerException and display an Alert message from the Alerts class. 
     @param event The mouse click on the Button.
     */
    @FXML
    private void onActionDeleteCustomer(ActionEvent event) throws SQLException {
        
        try
        {
            //Checks to see if an appointment was chosen prior to running function call
            Customer modCust = customersTV.getSelectionModel().getSelectedItem();
            cIDTF.setText(String.valueOf(modCust.getId()));
            
            //If User confirms Selection, then the program continues
            if(confirmSelection(modCust.getName(), modCust.getId(), "Customer") == true) 
            {
                //After Confirmation, Selected All Customer Appointments is Deleted
                deleteCustomerAppointments(modCust);
                //The program then dletes the Customer from the DB
                deleteCustomer(modCust);
                //Reloads Table View with new data from Database
                customersTV.setItems(getAllCustomers());
                //Clears out and disables all User fields
                onActionClearFields(event);
            }
            
            cIDTF.setText(null);

        }catch(NullPointerException e) {
            
            //Used to display appopriate alert for User when trying to push the Edit Appointment button with no Appointment selected
            emptySelection("Appoinment");
        }
    }
    
    /** This defines the on Action event for the Edit Button. This function is called when the Edit Button is pressed. The program takes the User's selection and 
     * validates to make sure a Customer Object was selected from the TableView. After validation, the Menu TextFields and ComboBoxes will be filled with the Customer's values.
     * Will catch NullPointerException and display an Alert message from the Alerts class.
     @param event The mouse click on the Button.
     */
    @FXML
    void onActionEditFields(ActionEvent event) {
        
        try{
        //Creates temporary object
        Customer modCustomer = customersTV.getSelectionModel().getSelectedItem();
        
        //Assigns text values to textfields
        cNameTF.setText(modCustomer.getName());
        cAddressTF.setText(modCustomer.getAddress());
        
        //Used to autofill Country Combo Box
        for(int i = 0; i < countryCB.getItems().size(); i++)
        {
            Country selCountry = countryCB.getItems().get(i);
            if(selCountry.getId() == modCustomer.getCountryID())
            {
             countryCB.setValue(selCountry);
             break;
            }
        
        }
        
        //Used to autofill Region combo Box
        for(int i = 0; i < regionCB.getItems().size(); i++)
        {
            Region selRegion = regionCB.getItems().get(i);
            if(selRegion.getId() == modCustomer.getRegionID())
            {
             regionCB.setValue(selRegion);
             break;
            }
        
        }
        
        
        cPostalCodeTF.setText(modCustomer.getPostalCode());
        cPhoneTF.setText(modCustomer.getPhone());
        cIDTF.setText(String.valueOf(modCustomer.getId()));
                
        cNameTF.setDisable(false);
        cAddressTF.setDisable(false);
        countryCB.setDisable(false);
        regionCB.setDisable(false);
        cPostalCodeTF.setDisable(false);
        cPhoneTF.setDisable(false);
        }catch(NullPointerException e) {
            
            //Used to display appopriate alert for User when trying to push the Edit Customer button with no Customer selected
            emptySelection("Customer");
        }
    }
}
