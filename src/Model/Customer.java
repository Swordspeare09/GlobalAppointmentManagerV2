/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/** This class defines the public Customer Class. */
public class Customer {
    
    //Priavte class variables
    private Integer id;
    private String name;
    private String postalCode;
    private String phone;
    private String address;
    private String region;
    private Integer regionID;
    private String country;
    private Integer countryID;
    
    //Condensed version of Customer to be able to use for Customer table defined in AddCustomerController and ModifyCustomerController files
    public Customer(Integer id, String name, String address, String postalCode, String phone, String region, Integer regionID, String country, Integer countryID)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.region = region;
        this.regionID = regionID;
        this.country = country;
        this.countryID = countryID;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the countryCode
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the regionCode
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the regionCode to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the regionID
     */
    public Integer getRegionID() {
        return regionID;
    }

    /**
     * @param regionID the regionID to set
     */
    public void setRegionID(Integer regionID) {
        this.regionID = regionID;
    }

    /**
     * @return the countryID
     */
    public Integer getCountryID() {
        return countryID;
    }

    /**
     * @param countryID the countryID to set
     */
    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }
    
    /**This function overrides the toString method of the Country class.
    * @return the name
    */
    @Override
    public String toString()
    {
        return (name);
    }
    
}
