/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author corte
 */
public class Contact {
    
    //Priavte class variables
    private int id;
    private String name;
    
    //Constructor method for creating part objects
    public Contact (int id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    //Public mutator methods
    public void setId(int id) {
	this.id = id;
    }
	
    public void setName(String name) {
	this.name = name;
    }
    
    //Public accessor methods
    public int getId() {
	return this.id;
    }
	
    public String getName() {
	return this.name;
    }
    
    //Overides the toString method used to display the Contacts in the ComboBox
    @Override
    public String toString()
    {
        return (name);
    }

}
