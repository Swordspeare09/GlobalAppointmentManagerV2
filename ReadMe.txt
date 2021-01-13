Title: Global Appointment Manager Application

Purpose: This application was designed to access my knowledge and comprehension of working with 
information stored on DataBases, primarily SQL DataBases.

Description: This application was designed to simulate accessing and storing information 
in a SQL DataBase for a fictitious company. The application uses Lambda Expressions to convert times stored 
in the database, which are in UTC, and convert them to the Users local timezone according to their system's settings.

Instructions: Upon start up of the application, the user will be greeting with a login in window. Upon successful entry 
of login credentials, the application will record the username, local date and time, and successful login onto a text file.
The user will then be alerted with whether or not an appointment is within 15 minutes of User's login time. Two different alert
prompts will be displayed in either case. The User will then be taken to the main menu to be able to see a calender of all 
appointments in the appointment table. There are three radio buttons on the side that can filter the calender by month and 
week. There are 4 buttons on the main menu that take you to different menus. There is a menu to add an appointment, modify 
an appointment, add a customer, and modify a customer. The modify menus for both the appointments and customers each give 
the ability to delete selected appointments and customers. Deleting a customer however will result in deleting all appointments
with that customer's id due to foreign key restraints. There are another 2 menus buttons that lead to the Appointment Report
page, and the Contact Reports Page. The Appointment Reports page will give the user the ability to check out the total number of 
appointments by each contact, by type, and the total number of appointments in the Appointments table. The Contact Reports page
gives you a schedule of each Contact in the fictitious company. Per the requirements of this assessment, I chose to give a report 
of the total number of Appointments of each Contact that is displayed in the corresponding label in the Appointment reports page.
Next to each Contacts Name, there is a integer displayed that comes from a result set of an SQL query that uses the Count SQL function
filtering by the Contact_ID in the Appointments SQL table. The final button on the Main Menu is the Log out button, which closes the 
connection to the DataBase and returns to the login screen of the application. 

Author Information: 
Francisco J Cortez

Contact Information:
email: fcort16@wgu.edu

Application Version: 2.0

Date: 12/30/2020

IDE Version: Apache NetBeans IDE 11.0

JDK Version: jdk-11.0.9

JavaFX Version: 11.0.0


