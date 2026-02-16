package au.edu.rmit.sct;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Person {


    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private static final String FILE_NAME = "src/main/resources/persons.txt";

    public Person(String personID, String firstName, String lastName,
                  String address, String birthdate) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
    

    }

     private boolean isValidPersonID(String id) {

        return true;
     }
      private boolean isValidAddress(String addr) {
        return true;
      }

       private boolean isValidBirthdate(String bd) {
        return true;
       }
        private boolean isDuplicateID(String id) {
            return true;
        }

    public boolean addPerson() {

        if (!isValidPersonID(personID)) return false;

        if (!isValidAddress(address)) return false;
     
        if (!isValidBirthdate(birthdate)) return false;

        if (isDuplicateID(personID)) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(personID + "," + firstName + "," + lastName + "," + address + "," + birthdate);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }




    public boolean updatePersonalDetails() {
   
        return true;
    }

    public boolean addID() {

        return true;
    }
