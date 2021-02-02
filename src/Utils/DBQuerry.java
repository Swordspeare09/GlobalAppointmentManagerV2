/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Model.Appointments;
import Model.Contact;
import Model.Country;
import Model.Customer;
import Model.Region;
import Model.User;
import Model.Users;
import Model.conversionToUTC;
import Model.conversionToUserLocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class defines the public DBConnection Class. This class is used to define all of the public static methods used
 to query the DB. */
public class DBQuerry {
    
    private static PreparedStatement statement;
    
    //ObservableList variables used in queries functions
    static ObservableList<Country> countryList = FXCollections.observableArrayList();
    static ObservableList<Region> regionList = FXCollections.observableArrayList();
    static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    static ObservableList<Contact> contactList = FXCollections.observableArrayList(); 
    static ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
    static ObservableList<Users> usersList = FXCollections.observableArrayList();
    
    /** Defines the Static Function used to Set the Prepared Statement. Used to create and set a Prepared Statement
     * for Querying the DataBase. 
     * @param conn This connection is used to connect to the DataBase. 
     * @param SQLStatement The String that contains the Statement used to create a query. 
     * @throws SQLException Throws an SQL Exception is there is a problem with the DataBase or the credentials.
     */
    public static void setPreparedStatement(Connection conn, String SQLStatement) throws SQLException
    {
        statement = conn.prepareStatement(SQLStatement);
        
    }
    
    /** Defines the Static Function used to Retrieve the Prepared Statement. Used to retrieve the Prepared Statement
     * for Querying the DataBase. 
     * @return Returns the Prepared Statement that was set previously. 
     */
    public static PreparedStatement getPreparedStatement()
    {
        return statement;
    }
    
    /** *  Defines the Static Function used to Query the DB. This SQL query is used return a Boolean value if the 
     * User Name and Password fields match a row in the Users table. 
     * @param username The string used in the SQL Query.
     * @param password The string used in the SQL Query.
     * @return Returns a Boolean Value based on the Result Set of the Query. 
     */
    public static boolean login(String username, String password) {
        
            try{
                Connection conn = DBConnection.getConnection();
                String loginStatement = "SELECT * FROM users WHERE User_Name=? AND Password=?";
                DBQuerry.setPreparedStatement(conn, loginStatement);
                PreparedStatement ps = DBQuerry.getPreparedStatement();
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    
                    User currentUser = new User(rs.getString("User_Name"), rs.getInt("User_ID"), true, ZoneId.systemDefault().toString());
                    return true;
                }
                else {
                    DBConnection.closeConnection();
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                return false;
            }
        
    }
    
    
    /** *  Defines the Static Function used to Query the DB. This SQL query is used return all the rows of the Countries
     * table.
     * @return ObservableList Returns an Observable List of type Country to be used in the Controller classes.
     */
    public static ObservableList<Country> getCountries(){
        
        try{
            //Removed all previously stored countries to prevent duplicate entries
            countryList.removeAll(countryList);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "SELECT Country, Country_ID  FROM countries";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                Country tempCountry = new Country(tempList.getInt("Country_ID"), tempList.getString("Country"));
                countryList.add(tempCountry);
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        
        return countryList;
    }
    
    /** *  Defines the Static Function used to Query the DB. This SQL query is used return all the rows of the First Level 
     * Divisions based on the Country ID. 
     * @param selCountry Country object used to Query the DB. 
     * @return ObservableList Returns an Observable List of type Region to be used in the Controller classes.
     */
    public static ObservableList<Region> getRegions(Country selCountry){
        
        try{
            //Removed all previously stored Regions to prevent duplicate entries
            regionList.removeAll(regionList);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "SELECT Division_ID, Division FROM first_level_divisions where COUNTRY_ID = ?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            
            //Uses Country ID from User selection of Combo Box options
            ps.setString(1, String.valueOf(selCountry.getId()));
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                Region tempRegion = new Region(tempList.getInt("Division_ID"), tempList.getString("Division"));
                regionList.add(tempRegion);
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        
        return regionList;
    }
    
    /** Defines the Static Function used to Query the DB. This SQL query is used return all the rows of the Contacts  in teh 
     * Contacts Table. 
     * @return ObservableList Returns an Observable List of type Contact to be used in the Controller classes.
     */
    public static ObservableList<Contact> getAllContacts() 
    {
        try{
           //Removed all previously stored Contacts to prevent duplicate entries 
           contactList.removeAll(contactList);
           Connection conn = DBConnection.getConnection();
           String queryStatement = "SELECT Contact_ID, Contact_Name FROM contacts";
           DBQuerry.setPreparedStatement(conn, queryStatement);
           PreparedStatement ps = DBQuerry.getPreparedStatement();
           ResultSet tempList = ps.executeQuery();
           while(tempList.next())
           {
               Contact tempContact = new Contact(tempList.getInt("Contact_ID"), tempList.getString("Contact_Name"));
               contactList.add(tempContact);
           }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return contactList;
    }
    
    /** Defines the Static Function used to Query the DB. This SQL query is used return all the rows of the Users in teh 
     * Users Table. 
     * @return ObservableList Returns an Observable List of type Users to be used in the Controller classes.
     * @throws java.sql.SQLException Throws an SQL Exception if there is a problem with the DB.
     */
    public static ObservableList<Users> getAllUsers() throws SQLException 
    {
        try
        {
            //Remove all previously stored Users to prevent duplicate entries
            usersList.removeAll(usersList);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "SELECT User_ID, User_Name FROM WJ07uZq.users Order By User_ID";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                Users tempUsers = new Users(
                        tempList.getInt("User_ID"),
                        tempList.getString("User_Name")
                );
                usersList.add(tempUsers);
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return usersList;
    }
    
    /** Defines the Static Function used to Query the DB. This SQL query is used return all the rows of teh Customers
     * Table joined on the First Level Divisions and the Countries Table.  
     * @return ObservableList Returns an Observable List of type Customer to be used in the Controller classes.
     * @throws java.sql.SQLException Throws an SQL Exception if there is a problem with the DB.
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException
    {
        try
        {
            //Removed all previously stored Customers to prevent duplicate entries
            customerList.removeAll(customerList);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "select c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, "
                                    + "c.Phone, r.Division, r.Division_ID, co.Country, co.Country_ID from customers "
                                    + "as c inner join  first_level_divisions as r  on r.Division_ID = c.Division_ID "
                                    + "inner join countries as co on r.Country_ID = co.Country_ID;";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                Customer tempCustomer = new Customer(
                        tempList.getInt("Customer_ID"), 
                        tempList.getString("Customer_Name"),
                        tempList.getString("Address"),
                        tempList.getString("Postal_Code"),
                        tempList.getString("Phone"),
                        tempList.getString("Division"),
                        tempList.getInt("Division_ID"),
                        tempList.getString("Country"),
                        tempList.getInt("Country_ID")
                        );
                customerList.add(tempCustomer);
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        
        return customerList;
    }
    
    /** This defines the Static Function for preforming the query used to get all Appointments by Month. The function takes a LocalDateTime object and converts the month using a Lambda
     * to UTC in order to retrieve a certain result set using an SQL query.
     @param date The date sent used to preform the SQL Query.
     * @return ObservableList Returns an Observable List of type Appointments to be used in the Controller classes.
     */
    public static ObservableList<Appointments>getMonthAppointments(LocalDate date)
    {
        
         conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the zonedDateTime in LocalDattime form 
            ZonedDateTime zonedDateTimeInUTC = dateTime.atZone(ZoneId.of("UTC"));
            ZonedDateTime userZonedDateTime = zonedDateTimeInUTC.withZoneSameInstant(ZoneId.of(User.getZoneID()));
            LocalDateTime ldt = userZonedDateTime.toLocalDateTime();
            return ldt;
            };
        
        try{
            //Gets the LocalDate and gets the beginning and the end of the month to filter search
            LocalDate start = date.withDayOfMonth(1);
            LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String newStart = dtf.format(start);
            String newEnd = dtf.format(end);
            //Clears out appointments to remove duplicate listings
            appointmentList.removeAll(appointmentList);
            
            Connection conn = DBConnection.getConnection();
            String queryStatement = "SELECT a.Appointment_ID, a.Type, a.Title, a.Description, a.Location, a.Start, a.End, cus.Customer_ID, a.User_ID, a.Contact_ID " 
                    + "from appointments as a inner join customers as cus on a.Customer_ID = cus.Customer_ID"
                    + " inner join contacts as con on a.Contact_ID = con.Contact_ID Where a.Start >= ? and a.End <= ?"; 
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            //Key-value mapping
            ps.setString(1, newStart);
            ps.setString(2, newEnd);
            
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                //Converts UTC Dates and Times to Users local date and times
                LocalDateTime zonedStartDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("Start").toLocalDate(), tempList.getTime("Start").toLocalTime()));
                LocalDateTime zonedEndDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("End").toLocalDate(), tempList.getTime("End").toLocalTime()));
                
                Appointments tempApp = new Appointments( 
                        tempList.getInt("Appointment_ID"),
                        tempList.getString("Title"),
                        tempList.getString("Type"),
                        tempList.getString("Description"),
                        tempList.getString("Location"),
                        zonedStartDateTime,
                        zonedEndDateTime,
                        tempList.getInt("Customer_ID"),
                        tempList.getInt("User_ID"),
                        tempList.getInt("Contact_ID")
                );
                appointmentList.add(tempApp);
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return appointmentList;
    }
    
    /** This defines the Static Function for preforming the query used to get all Appointments by Week. The function takes a LocalDateTime object and converts the week using a Lambda
     * to UTC in order to retrieve a certain result set using an SQL query.
     @param date The date sent used to preform the SQL Query.
     * @return ObservableList Returns an Observable List of type Appointments to be used in the Controller classes.
     */
    public static ObservableList<Appointments>getWeekAppointments(LocalDate date)
    {
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
            ZoneId sourceTimeZone = ZoneId.of("UTC");
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        try{
            //Gets the LocalDate and gets the beginning and the end of the week to filter search
            LocalDate start = date.with(DayOfWeek.MONDAY);
            LocalDate end = date.with(DayOfWeek.SATURDAY);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String newStart = dtf.format(start);
            String newEnd = dtf.format(end);
            //Clears out appointments to remove duplicate listings
            appointmentList.removeAll(appointmentList);
            
            Connection conn = DBConnection.getConnection();
            String queryStatement = "SELECT a.Appointment_ID, a.Type, a.Title, a.Description, a.Location, a.Start, a.End, cus.Customer_ID, a.User_ID, a.Contact_ID " 
                    + "from appointments as a inner join customers as cus on a.Customer_ID = cus.Customer_ID"
                    + " inner join contacts as con on a.Contact_ID = con.Contact_ID Where a.Start >= ? and a.End <= ?"; 
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            //Key-value mapping
            ps.setString(1, newStart);
            ps.setString(2, newEnd);
            
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                //Converts UTC Dates and Times to Users local date and times
                LocalDateTime zonedStartDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("Start").toLocalDate(), tempList.getTime("Start").toLocalTime()));
                LocalDateTime zonedEndDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("End").toLocalDate(), tempList.getTime("End").toLocalTime()));
                
                Appointments tempApp = new Appointments( 
                        tempList.getInt("Appointment_ID"),
                        tempList.getString("Title"),
                        tempList.getString("Type"),
                        tempList.getString("Description"),
                        tempList.getString("Location"),
                        zonedStartDateTime,
                        zonedEndDateTime,
                        tempList.getInt("Customer_ID"),
                        tempList.getInt("User_ID"),
                        tempList.getInt("Contact_ID")
                );
                appointmentList.add(tempApp);
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return appointmentList;
    }
    
    /** This defines the Static Function for preforming the query used to get all Appointments by Contact ID. The function takes a Integer and preforms an SQL query filtering 
     * by Contact_ID. The lambda used converts UTC to User's Local Time According to their System's Settings.
     @param i The Contact Id sent used to preform the SQL Query.
     * @return ObservableList Returns an Observable List of type Appointments to be used in the Controller classes.
     */
    public static ObservableList<Appointments>getAllAppointmentsByContact(Integer i)
    {
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
            ZoneId sourceTimeZone = ZoneId.of("UTC");
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        try{
            //Clears out appointments to remove duplicate listings
            appointmentList.removeAll(appointmentList);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "SELECT a.Appointment_ID, a.Type, a.Title, a.Description, a.Location, a.Start, a.End, "
                    + "cus.Customer_ID, a.User_ID, a.Contact_ID from appointments as a inner join customers as cus on a.Customer_ID = cus.Customer_ID "
                    + "inner join contacts as con on a.Contact_ID = con.Contact_ID where a.Contact_ID =?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            
            //Key-value mapping
            ps.setString(1, String.valueOf(i));
            
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                //Converts UTC Dates and Times to Users local date and times
                LocalDateTime zonedStartDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("Start").toLocalDate(), tempList.getTime("Start").toLocalTime()));
                LocalDateTime zonedEndDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("End").toLocalDate(), tempList.getTime("End").toLocalTime()));
                
                Appointments tempApp = new Appointments( 
                        tempList.getInt("Appointment_ID"),
                        tempList.getString("Title"),
                        tempList.getString("Type"),
                        tempList.getString("Description"),
                        tempList.getString("Location"),
                        zonedStartDateTime,
                        zonedEndDateTime,
                        tempList.getInt("Customer_ID"),
                        tempList.getInt("User_ID"),
                        tempList.getInt("Contact_ID")
                );
                appointmentList.add(tempApp);
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return appointmentList;
    }
    
     /** This defines the Static Function for preforming the query used to get all Appointments by Customer ID. The function takes a Customer object and preforms an SQL query filtering 
     * by Customer_ID. The lambda used converts UTC to User's Local Time According to their System's Settings.
     @param cust The Customer object used to retrieve the Customer_ID need for the SQL query.
     * @return ObservableList Returns an Observable List of type Appointments to be used in the Controller classes.
     */
    public static ObservableList<Appointments>getAllAppointmentsByCustomer(Customer cust)
    {
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
            ZoneId sourceTimeZone = ZoneId.of("UTC");
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        try{
            //Clears out appointments to remove duplicate listings
            appointmentList.removeAll(appointmentList);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "SELECT a.Appointment_ID, a.Type, a.Title, a.Description, a.Location, a.Start, a.End, "
                    + "cus.Customer_ID, a.User_ID, a.Contact_ID from appointments as a inner join customers as cus on a.Customer_ID = cus.Customer_ID "
                    + "inner join contacts as con on a.Contact_ID = con.Contact_ID where a.Customer_ID = ?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            
            //Key-value mapping
            ps.setString(1, String.valueOf(cust.getId()));
            
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                //Converts UTC Dates and Times to Users local date and times
                LocalDateTime zonedStartDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("Start").toLocalDate(), tempList.getTime("Start").toLocalTime()));
                LocalDateTime zonedEndDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("End").toLocalDate(), tempList.getTime("End").toLocalTime()));
                
                Appointments tempApp = new Appointments( 
                        tempList.getInt("Appointment_ID"),
                        tempList.getString("Title"),
                        tempList.getString("Type"),
                        tempList.getString("Description"),
                        tempList.getString("Location"),
                        zonedStartDateTime,
                        zonedEndDateTime,
                        tempList.getInt("Customer_ID"),
                        tempList.getInt("User_ID"),
                        tempList.getInt("Contact_ID")
                );
                appointmentList.add(tempApp);
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return appointmentList;
    }
    
    /** This defines the Static Function for preforming the query used to get all Appointments. The function uses a Lambda Expression in order to convert the Result Set's
     * UTC to the User's Local Time Zone according to their System's Default Settings. The Lambda used 
        will convert UTC to User's Local time according to their System's Default setting.
     * @return ObservableList Returns an Observable List of type Appointments to be used in the Controller classes. 
     */
    public static ObservableList<Appointments> getAllAppointments()
    {

        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
            ZoneId sourceTimeZone = ZoneId.of("UTC");
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        try{
            //Clears out appointments to remove duplicate listings
            appointmentList.removeAll(appointmentList);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "SELECT a.Appointment_ID, a.Type, a.Title, a.Description, a.Location, a.Start, a.End, "
                    + "cus.Customer_ID, a.User_ID, a.Contact_ID from appointments as a inner join customers as cus on a.Customer_ID = cus.Customer_ID "
                    + "inner join contacts as con on a.Contact_ID = con.Contact_ID";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                //Converts UTC Dates and Times to Users local date and times
                LocalDateTime zonedStartDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("Start").toLocalDate(), tempList.getTime("Start").toLocalTime()));
                LocalDateTime zonedEndDateTime = convert.conversionToUserLocalDate(LocalDateTime.of(tempList.getDate("End").toLocalDate(), tempList.getTime("End").toLocalTime()));
                
                Appointments tempApp = new Appointments( 
                        tempList.getInt("Appointment_ID"),
                        tempList.getString("Title"),
                        tempList.getString("Type"),
                        tempList.getString("Description"),
                        tempList.getString("Location"),
                        zonedStartDateTime,
                        zonedEndDateTime,
                        tempList.getInt("Customer_ID"),
                        tempList.getInt("User_ID"),
                        tempList.getInt("Contact_ID")
                );
                appointmentList.add(tempApp);
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return appointmentList;
    }
   
    /** *  Defines the Static Function for preforming the query used to Update. This query is used to insert a new Customer in the DB. The Lambda used 
        will convert UTC to User's Local time according to their System's Default setting.
     * @param newCust The Customer object used to insert a new row in the Customer table. 
     * @param regionID Integer value used to insert a  new row in the Customer table.
     * @throws java.sql.SQLException Throws an SQL Exception if there is a problem with the DB. 
     */
    public static void addCustomer(Customer newCust, int regionID) throws SQLException
    {
            conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form 
            ZonedDateTime zonedDateTimeInUTC = dateTime.atZone(ZoneId.of("UTC"));
            LocalDateTime ldt = zonedDateTimeInUTC.toLocalDateTime();
            return ldt;
            };
        
        try{
        
           //Used to get time of new Customer creation 
            LocalDateTime now = convert.conversionToUserLocalDate(LocalDateTime.now());
            //Formatting LDT object
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String newTimeNow = dtf.format(now);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "INSERT into customers(Customer_Name, Address, Postal_Code, Phone, "
                                + "Create_Date, Created_By, Last_Updated_By, Division_ID)" +
                                    " Values( ?, ?, ?, ?, ?, ?, ?, ?)";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
        
            //Key-value mapping
            ps.setString(1, newCust.getName());
            ps.setString(2, newCust.getAddress());
            ps.setString(3, newCust.getPostalCode());
            ps.setString(4, newCust.getPhone());
            ps.setString(5, newTimeNow);
            ps.setString(6, User.getUserName());
            ps.setString(7, User.getUserName());
            ps.setString(8, String.valueOf(regionID));
            
            Integer rs = ps.executeUpdate();
            System.out.println(rs);
        
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /** Defines the Static Function for preforming the query used to Update. This query is used to update a Customer in the DB. The Lambda used 
     * will convert UTC to User's Local time according to their System's Default setting. 
     * @param modCust Customer object used to update a Customer. 
     * @param regionID Integer value used to update a Customer.
     */
    public static void modifyCustomer(Customer modCust, int regionID)
    {
        
            conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form 
            ZonedDateTime zonedDateTimeInUTC = dateTime.atZone(ZoneId.of("UTC"));
            LocalDateTime ldt = zonedDateTimeInUTC.toLocalDateTime();
            return ldt;
            };
        try{
            //Used to get time of new Customer creation 
            LocalDateTime now = convert.conversionToUserLocalDate(LocalDateTime.now());
            
            //Formatting LDT object
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String newTimeNow = dtf.format(now);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "UPDATE customers SET Customer_Name = ?, Address = ?,"
                                    + "Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?,"
                                    + " Division_ID = ? WHERE Customer_ID = ?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
        
            //Key-value mapping
            ps.setString(1, modCust.getName());
            ps.setString(2, modCust.getAddress());
            ps.setString(3, modCust.getPostalCode());
            ps.setString(4, modCust.getPhone());
            ps.setString(5, newTimeNow);
            ps.setString(6, User.getUserName());
            ps.setString(7, String.valueOf(regionID));
            ps.setString(8, String.valueOf(modCust.getId()));
            
            Integer rs = ps.executeUpdate();
            System.out.println(rs);
        
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /** Defines the Static function used to Delete. This query is used to preform a Delete on the Customer Table in the 
     * DataBase. 
     * @param modCustomer The Customer object used to delete from the DB. 
     */
    public static void deleteCustomer(Customer modCustomer)
    {
        try
        {
            //Connects to DB
            Connection conn = DBConnection.getConnection();
            String queryStatement = "Delete from customers where Customer_ID = ?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            
            //Key-value mapping
            ps.setString(1, String.valueOf(modCustomer.getId()));
            
            //Executes Update statement
            Integer rs = ps.executeUpdate();
            System.out.println(rs);
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    
    }
    
    /** *  This defines the Static Function for preforming the query used to insert an Appointment into the DB. The lambda used converts UTC to User's Local Time According 
        to their System's Settings. 
        @param newApp The Appointment object sent used to preform the SQL Query. 
     *  @param tUser The User object sent that is used to preform the SQL Query. 
     */
     public static void addAppointment(Appointments newApp, Users tUser)
    {
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        try
        {
            //Used to get time of new Appointments creation 
            LocalDateTime now = LocalDateTime.now();
            
            //Converts UTC Dates and Times to Users local date and times
            LocalDateTime zonedStartDateTime = convert.conversionToUserLocalDate(newApp.getStart());
            LocalDateTime zonedEndDateTime = convert.conversionToUserLocalDate(newApp.getEnd());
            
            //Used to format and convert LocalDateTime Objects into string pattern
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String newStart = dtf.format(zonedStartDateTime);
            String newEnd = dtf.format(zonedEndDateTime);
            String newNow = dtf.format(now);
            
            Connection conn = DBConnection.getConnection();
            String queryStatement = "insert into appointments(Title, Description, Location, Type, "
                                    + "Start, End, Create_Date, Created_By, Last_Updated_By, Customer_ID, "
                                    + "User_ID, Contact_ID) Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
        
            //Key-value mapping
            ps.setString(1, newApp.getTitle());
            ps.setString(2, newApp.getDescription());
            ps.setString(3, newApp.getLocation());
            ps.setString(4, newApp.getType());
            ps.setString(5, newStart);
            ps.setString(6, newEnd);
            ps.setString(7, newNow);
            ps.setString(8, tUser.getUserName());
            ps.setString(9, tUser.getUserName());
            ps.setString(10, String.valueOf(newApp.getCustomerID()));
            ps.setString(11, String.valueOf(tUser.getUserID()));
            ps.setString(12, String.valueOf(newApp.getContactID()));
            
            Integer rs = ps.executeUpdate();
            System.out.println(rs);
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    
    } 
    /** Defines the Static Function for preforming the an Update. This query used to update an Appointment into the DB. The lambda used converts UTC to User's Local Time According 
 to their System's Settings. 
     @param modApp The Appointment object used to preform the SQL Query.
     * @param tUser The User Object used to update SQL DataBase
     */ 
    public static void modifyAppointment(Appointments modApp, Users tUser )
    {
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        try{
            //Used to get time of modification of customer 
            LocalDateTime now = LocalDateTime.now();
            
            //Converts UTC Dates and Times to Users local date and times
            LocalDateTime zonedStartDateTime = convert.conversionToUserLocalDate(modApp.getStart());
            LocalDateTime zonedEndDateTime = convert.conversionToUserLocalDate(modApp.getEnd());
            
            //Formatting LDT object
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String newTimeNow = dtf.format(now);
            String newStart = dtf.format(zonedStartDateTime);
            String newEnd = dtf.format(zonedEndDateTime);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "UPDATE appointments SET Title = ?, Description = ?,"
                                    + "Location = ?, Type = ?, Start = ?, End = ?,"
                                    + " Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?,"
                                    + " User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
        
            //Key-value mapping
            ps.setString(1, modApp.getTitle());
            ps.setString(2, modApp.getDescription());
            ps.setString(3, modApp.getLocation());
            ps.setString(4, modApp.getType());
            ps.setString(5, newStart);
            ps.setString(6, newEnd);
            ps.setString(7, newTimeNow);
            ps.setString(8, tUser.getUserName());
            ps.setString(9, String.valueOf(modApp.getCustomerID()));
            ps.setString(10, String.valueOf(tUser.getUserID()));
            ps.setString(11, String.valueOf(modApp.getContactID()));
            ps.setString(12, String.valueOf(modApp.getId()));
            
            Integer rs = ps.executeUpdate();
            System.out.println(rs);
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    
    }
    
    /** *  Defines the Static Function used to Query the Appointments Table. This query is to delete a row in 
  the Appointments table based on a certain Appointment Id.  
     * @param modApp The Appointment object used in the SQL Query.
     */
    public static void deleteAppointment(Appointments modApp)
    {
        try
        {   
            //Retrieves DB connection
            Connection conn = DBConnection.getConnection();
            String queryStatement = "Delete from appointments where Appointment_ID = ?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            
            //Key-value mapping
            ps.setString(1, String.valueOf(modApp.getId()));
            
            //Executes Update statement
            Integer rs = ps.executeUpdate();
            System.out.println(rs);
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    
    }
    
    /** Defines the Static Function used to Query the Appointments Table. This query is to delete any rows in 
     *  the Appointments table based on a certain Customer Id. 
     * @param modCustomer The Customer object used in the SQL query. 
     */
    public static void deleteCustomerAppointments(Customer modCustomer)
    {
        try
        {
            //Connects to DB
            Connection conn = DBConnection.getConnection();
            String queryStatement = "Delete from appointments where Customer_ID = ?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
            
            //Key-value mapping
            ps.setString(1, String.valueOf(modCustomer.getId()));
            
            //Executes Update statement
            Integer rs = ps.executeUpdate();
            System.out.println(rs);
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    
    
    }
    
    /** Defines the Static Function used to Query the Appointments table. This query is used to retrieve the next 
     * Auto Increment value from the Customers Table.
     * @return Returns an Integer value. 
     * @throws java.sql.SQLException Throws an SQL Exception if there is a problem with the DB. 
     */
    public static int getNewID() throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        String queryStatement = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'customers' and table_schema = 'WJ07uZq'";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        ResultSet rs = ps.executeQuery();
        
        //Used for troubleshooting
        System.out.println(rs.first());

        return rs.getInt("AUTO_INCREMENT");
    }
    
    /** Defines the Static Function used to Query the Appointments table. This query is used to retrieve the next 
     * Auto Increment value from the Appointments Table.
     * @return Returns an Integer value. 
     * @throws java.sql.SQLException Throws an SQL Exception if there is a problem with the DB. 
     */
    public static int getNewAppID() throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        String queryStatement = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'appointments' and table_schema = 'WJ07uZq'";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        ResultSet rs = ps.executeQuery();

        //Used for troubleshooting
        System.out.println(rs.first());
        
        return rs.getInt("AUTO_INCREMENT");
    }
    
    /** Defines the Static Function used to Query the Appointments table. This query is used to retrieve the total number
     *  of rows in the Appointments Table. 
     * @return Returns an Integer value. 
     * @throws java.sql.SQLException Throws an SQL Exception if there is a problem with the DB. 
     */
    public static int getAllAppointmentReport() throws SQLException 
    {
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select COUNT(*) From WJ07uZq.appointments";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        ResultSet rs = ps.executeQuery();
        //Advances the result set to the next row to retrieve the Int
        rs.next();
        return rs.getInt("COUNT(*)");
        
    }
    /** Defines the Static Function used to Query the Appointments table. This query is used to retrieve the number
     *  of rows in the Appointments Table that match the Contact ID. 
     * @param i The Integer used in the SQL Query. 
     * @return Returns an Integer value. 
     * @throws java.sql.SQLException Throws an SQL Exception if there is a problem with the DB. 
     */
    public static int getAppointmentByContactReport(Integer i) throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select COUNT(*) From WJ07uZq.appointments where Contact_ID = ?";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        
        ps.setString(1, String.valueOf(i));
        ResultSet rs = ps.executeQuery();
        //Advances the result set to the next row to retrieve the Int
        rs.next();
        return rs.getInt("COUNT(*)");
    }
    
    /** Defines the static Function used to Query the Appointments Table. This query is used to retrieve the number
     *  of rows in the Appointments Table that match the string.
     * @param s The String used to query by Type.
     * @return This function returns an Integer value. 
     * @throws java.sql.SQLException The Exception thrown if there is a problem with DB. 
     */
    public static int getAppointmentByTypeReport(String s) throws SQLException 
    {
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select COUNT(*) From WJ07uZq.appointments Where Type Like ?";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        
        ps.setString(1, String.valueOf(s));
        ResultSet rs = ps.executeQuery();
        //Advances the result set to the next row to retrieve the Int
        rs.next();
        return rs.getInt("COUNT(*)");
    }
    /** This defines the Static Function for preforming the query used to check for Appointment Times with 15 minutes of logging in. The lambda used converts UTC to User's Local Time According 
     * to their System's Settings. There are two different Lambda Expressions used in function. One is used to convert from UTC to
     * the User's Local Time according to their Zone Id. The other is used to do the opposite.
     @param userID The Integer used to preform the SQL Query. 
     * @return Boolean Returns a Boolean value. 
     * @throws SQLException Throws an SQL Exception if there is a DataBase Error. 
     */ 
    public static ObservableList<Appointments> appointmentLoginChecker(Integer userID) throws SQLException
    {
        // The Lambda Expression defined in this function is used to convert User's Local Time to UTC. This is needed to keep all times stored in DB 
        // in UTC for uniformity. The Lambda will convert User's Local time according to their System's Default setting to UTC.
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        // The Lambda Expression defined in this function is used to convert UTC to Users Local Time. This is needed to keep all times stored in DB 
        // in UTC for uniformity. The Lambda will convert UTC to User's Local time according to their System's Default setting.
        conversionToUTC convertBack = (LocalDateTime dateTime) -> { 
           
            ZoneId targetTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
            ZoneId sourceTimeZone = ZoneId.of("UTC");
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime secondNow = now.plusMinutes(15);
        
        //Converts to UTC Dates and Times to Check the DB
        LocalDateTime newFirstNow = convert.conversionToUserLocalDate(now);
        LocalDateTime newSecondNow = convert.conversionToUserLocalDate(secondNow);
        
        //Used to format and convert LocalDateTime Objects into string pattern
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String newStart = dtf.format(newFirstNow);
        String newEnd = dtf.format(newSecondNow);
        
        try{
            //Clears out appointments to remove duplicate listings
            appointmentList.removeAll(appointmentList);
            Connection conn = DBConnection.getConnection();
            String queryStatement = "Select * From WJ07uZq.appointments where Start Between ? AND ? AND User_ID = ?";
            DBQuerry.setPreparedStatement(conn, queryStatement);
            PreparedStatement ps = DBQuerry.getPreparedStatement();
        
            //Key-value mapping
            ps.setString(1, newStart);
            ps.setString(2, newEnd);
            ps.setString(3, String.valueOf(userID));
        
            ResultSet tempList = ps.executeQuery();
            while(tempList.next())
            {
                //Converts UTC Dates and Times to Users local date and times
                LocalDateTime zonedStartDateTime = convertBack.conversionToUTC (LocalDateTime.of(tempList.getDate("Start").toLocalDate(), tempList.getTime("Start").toLocalTime()));
                LocalDateTime zonedEndDateTime = convertBack.conversionToUTC (LocalDateTime.of(tempList.getDate("End").toLocalDate(), tempList.getTime("End").toLocalTime()));
                
                Appointments tempApp = new Appointments( 
                        tempList.getInt("Appointment_ID"),
                        tempList.getString("Title"),
                        tempList.getString("Type"),
                        tempList.getString("Description"),
                        tempList.getString("Location"),
                        zonedStartDateTime,
                        zonedEndDateTime,
                        tempList.getInt("Customer_ID"),
                        User.getUserID(),
                        tempList.getInt("Contact_ID")
                );
                appointmentList.add(tempApp);
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return appointmentList;
    }
    
    /** This defines the Static Function for preforming the query used to check for Appointment Times Overlap before inserting. The lambda used converts UTC to User's Local Time According 
     * to their System's Settings. This functions uses a Lambda to convert UTC to User's Local time according to their System's Default setting.
     @param start The LocalDateTime object used to preform the SQL Query.
     * @param end The LocalDateTime object used to preform the SQL Query.
     * @param customerID The Integer used to preform the SQL Query.
     * @return Returns a Boolean Value.
     * @throws SQLException Throws an SQL Exception if there is a DataBase Error.
     */ 
    public static boolean appointmentOverlapCheckByCustomer(LocalDateTime start, LocalDateTime end, Integer customerID) throws SQLException
    {
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        //Converts to UTC Dates and Times to Check the DB
        LocalDateTime newFirstNow = convert.conversionToUserLocalDate(start);
        LocalDateTime newSecondNow = convert.conversionToUserLocalDate(end);
        
        //Used to format and convert LocalDateTime Objects into string pattern
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String newStart = dtf.format(newFirstNow);
        String newEnd = dtf.format(newSecondNow);
        
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select * From WJ07uZq.appointments where Start Between ? AND ? AND Customer_ID = ?";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        
        //Key-value mapping
        ps.setString(1, newStart);
        ps.setString(2, newEnd);
        ps.setString(3, String.valueOf(customerID));
        
        ResultSet rs = ps.executeQuery();
        
        //Returns true or false depending on if the query returns appointments
        return rs.next();
    }
    
    /** * This defines the Static Function for preforming the query used to check for Appointment Times Overlap before updating. The lambda used converts UTC to User's Local Time According 
 to their System's Settings.This functions uses a Lambda to convert UTC to User's Local time according to their System's Default setting.
     * @param start The LocalDateTime object used to preform the SQL Query.
     * @param end The LocalDateTime object used to preform the SQL Query.
     * @param customerID The Integer used to preform the SQL Query.
     * @param appointmentId The Integer used to preform the SQL Query.
     * @return Returns a Boolean Value. 
     * @throws SQLException Throws an SQL Exception if there is a DataBase Error.
     */
    public static boolean appointmentModifyOverlapByCustomer(LocalDateTime start, LocalDateTime end, Integer customerID, Integer appointmentId) throws SQLException
    {
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        //Converts to UTC Dates and Times to Check the DB
        LocalDateTime newFirstNow = convert.conversionToUserLocalDate(start);
        LocalDateTime newSecondNow = convert.conversionToUserLocalDate(end);
        
        //Used to format and convert LocalDateTime Objects into string pattern
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String newStart = dtf.format(newFirstNow);
        String newEnd = dtf.format(newSecondNow);
        
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select * From WJ07uZq.appointments where Start Between ? AND ? AND Customer_ID = ?";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        
        //Key-value mapping
        ps.setString(1, newStart);
        ps.setString(2, newEnd);
        ps.setString(3, String.valueOf(customerID));
        
        ResultSet rs = ps.executeQuery();
        
        //Checks and Moves the Result Set pointer to the 1st row to check the appointment ID
        if(rs.next())
        {
        //Returns true or false depending on whether the Ids match
        return Integer.parseInt(rs.getString("Appointment_ID")) == appointmentId;
        }
        else{
            return true;
        }
            
    }
    
    /** This defines the Static Function for preforming the query used to check for Appointment Times Overlap before inserting. The lambda used converts UTC to User's Local Time According 
     * to their System's Settings. This functions uses a Lambda to convert UTC to User's Local time according to their System's Default setting.
     @param start The LocalDateTime object used to preform the SQL Query.
     * @param end The LocalDateTime object used to preform the SQL Query.
     * @param userId The Integer used to preform the SQL Query.
     * @return Boolean Returns a Boolean Value.
     * @throws SQLException Throws an SQL Exception if there is a DataBase Error.
     */ 
    public static boolean appointmentOverlapCheckByUser(LocalDateTime start, LocalDateTime end, Integer userId) throws SQLException{
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        //Converts to UTC Dates and Times to Check the DB
        LocalDateTime newFirstNow = convert.conversionToUserLocalDate(start);
        LocalDateTime newSecondNow = convert.conversionToUserLocalDate(end);
        
        //Used to format and convert LocalDateTime Objects into string pattern
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String newStart = dtf.format(newFirstNow);
        String newEnd = dtf.format(newSecondNow);
        
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select * From WJ07uZq.appointments where Start Between ? AND ? AND User_ID = ?";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        
        //Key-value mapping
        ps.setString(1, newStart);
        ps.setString(2, newEnd);
        ps.setString(3, String.valueOf(userId));
        
        ResultSet rs = ps.executeQuery();
        
        //Returns true or false depending on if the query returns appointments
        return rs.next();
    }
    
    /** *  This defines the Static Function for preforming the query used to check for Appointment Times Overlap before updating.The lambda used converts UTC to User's Local Time According 
 to their System's Settings. This functions uses a Lambda to convert UTC to User's Local time according to their System's Default setting.
     @param start The LocalDateTime object used to preform the SQL Query.
     * @param end The LocalDateTime object used to preform the SQL Query.
     * @param userId The Integer used to preform the SQL Query.
     * @param appointmentId The Integer used to preform the Boolean check based on Appointment Id. 
     * @return Boolean Returns a Boolean Value.
     * @throws SQLException Throws an SQL Exception if there is a DataBase Error.
     */ 
    public static boolean appointmentModifyOverlapCheckByUser(LocalDateTime start, LocalDateTime end, Integer userId, Integer appointmentId) throws SQLException{
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        //Converts to UTC Dates and Times to Check the DB
        LocalDateTime newFirstNow = convert.conversionToUserLocalDate(start);
        LocalDateTime newSecondNow = convert.conversionToUserLocalDate(end);
        
        //Used to format and convert LocalDateTime Objects into string pattern
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String newStart = dtf.format(newFirstNow);
        String newEnd = dtf.format(newSecondNow);
        
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select * From WJ07uZq.appointments where Start Between ? AND ? AND User_ID = ?";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        
        //Key-value mapping
        ps.setString(1, newStart);
        ps.setString(2, newEnd);
        ps.setString(3, String.valueOf(userId));
        
        ResultSet rs = ps.executeQuery();
        
        //Checks and Moves the Result Set pointer to the 1st row to check the appointment ID
        if(rs.next())
        {
        //Returns true or false depending on whether the Ids match
        return Integer.parseInt(rs.getString("Appointment_ID")) == appointmentId;
        }
        else{
            return true;
        }
    }
    
    /** This defines the Static Function for preforming the query used to check for Appointment Times Overlap before inserting. The lambda used converts UTC to User's Local Time According 
     * to their System's Settings. This functions uses a Lambda to convert UTC to User's Local time according to their System's Default setting.
     @param start The LocalDateTime object used to preform the SQL Query.
     * @param end The LocalDateTime object used to preform the SQL Query.
     * @param contactId The Integer used to preform the SQL Query.
     * @return Boolean Returns a Boolean Value.
     * @throws SQLException Throws an SQL Exception if there is a DataBase Error.
     */ 
    public static boolean appointmentOverlapCheckByContact(LocalDateTime start, LocalDateTime end, Integer contactId) throws SQLException{
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        //Converts to UTC Dates and Times to Check the DB
        LocalDateTime newFirstNow = convert.conversionToUserLocalDate(start);
        LocalDateTime newSecondNow = convert.conversionToUserLocalDate(end);
        
        //Used to format and convert LocalDateTime Objects into string pattern
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String newStart = dtf.format(newFirstNow);
        String newEnd = dtf.format(newSecondNow);
        
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select * From WJ07uZq.appointments where Start Between ? AND ? AND Contact_ID = ?";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        
        //Key-value mapping
        ps.setString(1, newStart);
        ps.setString(2, newEnd);
        ps.setString(3, String.valueOf(contactId));
        
        ResultSet rs = ps.executeQuery();
        
        //Returns true or false depending on if the query returns appointments
        return rs.next();
    }
    
    /** *  This defines the Static Function for preforming the query used to check for Appointment Times Overlap before inserting.The lambda used converts UTC to User's Local Time According 
 to their System's Settings.This functions uses a Lambda to convert UTC to User's Local time according to their System's Default setting.
     @param start The LocalDateTime object used to preform the SQL Query.
     * @param end The LocalDateTime object used to preform the SQL Query.
     * @param contactId The Integer used to preform the SQL Query.
     * @param appointmentId The Integer used to preform the Boolean check based on Appointment Id.
     * @return Boolean Returns a Boolean Value.
     * @throws SQLException Throws an SQL Exception if there is a DataBase Error.
     */ 
    public static boolean appointmentModifyOverlapCheckByContact(LocalDateTime start, LocalDateTime end, Integer contactId, Integer appointmentId) throws SQLException{
        
        conversionToUserLocalDate convert = (LocalDateTime dateTime) -> { 
            //Lambda used to convert to Users zonedDateTime with offset and then return an instant of the ZonedDateTime in LocalDateTime form             
            ZoneId targetTimeZone = ZoneId.of("UTC");
            ZoneId sourceTimeZone = ZoneId.of(ZoneId.systemDefault().toString());
           
            ZonedDateTime currentISTZoneDateTime = dateTime.atZone(sourceTimeZone);
            
            ZonedDateTime zonedAppDateTime = currentISTZoneDateTime.withZoneSameInstant(targetTimeZone);        
            LocalDateTime ldt = zonedAppDateTime.toLocalDateTime(); 
            
            return ldt;
            };
        
        //Converts to UTC Dates and Times to Check the DB
        LocalDateTime newFirstNow = convert.conversionToUserLocalDate(start);
        LocalDateTime newSecondNow = convert.conversionToUserLocalDate(end);
        
        //Used to format and convert LocalDateTime Objects into string pattern
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String newStart = dtf.format(newFirstNow);
        String newEnd = dtf.format(newSecondNow);
        
        Connection conn = DBConnection.getConnection();
        String queryStatement = "Select * From WJ07uZq.appointments where Start Between ? AND ? AND Contact_ID = ?";
        DBQuerry.setPreparedStatement(conn, queryStatement);
        PreparedStatement ps = DBQuerry.getPreparedStatement();
        
        //Key-value mapping
        ps.setString(1, newStart);
        ps.setString(2, newEnd);
        ps.setString(3, String.valueOf(contactId));
        
        ResultSet rs = ps.executeQuery();
        
        //Checks and Moves the Result Set pointer to the 1st row to check the appointment ID
        if(rs.next())
        {
        //Returns true or false depending on whether the Ids match
        return Integer.parseInt(rs.getString("Appointment_ID")) == appointmentId;
        }
        else{
            return true;
        }
    }
}