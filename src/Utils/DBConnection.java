/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** This class defines the public DBConnection Class. This Public class is used to create, retrieve and close
 connections to the DataBase. */
public class DBConnection {
    
    //DataBase Connection variables
    private static final String protocol = "jdbc";
    private static final String venderName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ07uZq";
    
    //JDBC URL
    private static final String jdbcURL = protocol + venderName + ipAddress;
    
    //Driver Interface Reference 
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    
    //Credentials for Accessing DB
    private static final String username = "U07uZq"; // Username
    private static final String password = "53689138987"; // Password
    
    private static Connection conn = null;
    
    /** The Static Function used to Create a Connection to DataBase. Creates a connection to the SQL DataBase using 
     * predefined credentials. 
     * @return Returns a Connection to the DataBase.
     */
    public static Connection startConnection()
    {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful!");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return conn;
    }
    
    /** The Static Function used to Close a Connection to the DataBase. Closes the Connection to the DataBase. 
     */
    public static void closeConnection ()
    {
        try {
            conn.close();
            System.out.println("Connection Closed!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /** The Static Function used to Retrieve a Connection to the DataBase. Used to retrieve a previously made connection
     * to the DataBase. 
     * @return Returns a connection. 
     * @throws SQLException Throws an SQL Exception if there is a problem with the DataBase or credentials. 
     */
    public static Connection getConnection() throws SQLException 
    {
        Connection connection = DriverManager.getConnection(jdbcURL, username, password);
        return connection;

    }
}
