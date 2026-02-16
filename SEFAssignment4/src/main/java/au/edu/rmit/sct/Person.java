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

        if (id == null || id.length() != 10) return false;
        int firstDigit = Character.getNumericValue(id.charAt(0));
        int secondDigit = Character.getNumericValue(id.charAt(1));

        if (firstDigit < 2 || firstDigit > 9) return false;
        if (secondDigit < 2 || secondDigit > 9) return false;

        
        if (!Character.isUpperCase(id.charAt(8)) || !Character.isUpperCase(id.charAt(9))) return false;

        
        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = id.charAt(i);
            if (!Character.isLetterOrDigit(c)) specialCount++;
        }

        return specialCount >= 2;
     }

      private boolean isValidAddress(String addr) {
        if (addr == null) return false;
        String[] parts = addr.split("\\|");

        if (parts.length != 5) return false;

        if (!parts[0].matches("\\d+")) return false;

        if (!parts[3].equals("Victoria")) return false;

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
