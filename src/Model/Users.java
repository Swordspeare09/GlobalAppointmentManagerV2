/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * This class defines the public Users Class.
 */
public class Users {
    
    private Integer userID;
    private String userName;
    
    /** 
     * Constructor method used to define the Users objects.
     * @param userID The Id used for that User in the DataBase.
     * @param userName The name for that User in the DataBase.
     */
    public Users (Integer userID, String userName)
    {
        this.userID = userID;
        this.userName = userName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userID
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    
    /**This function overrides the toString method of the Users class.
    * @return the name
    */
    @Override
    public String toString()
    {
        return (userName);
    }
    
}
