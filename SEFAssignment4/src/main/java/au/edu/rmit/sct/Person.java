package au.edu.rmit.sct;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;

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

    private boolean isValidBirthdate(String date) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(date, format);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDuplicateID(String id) {
        try {
            Path path = Path.of(FILE_NAME);
            if (!Files.exists(path)) return false;
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.startsWith(id + ",")) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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





    /*2. updatePersonalDetails function. This method allows updating a given person's ID, firstName, lastName, address and birthday in the TXT file.
    Changing personal details will not affect their demerit points or the suspension status. All relevant conditions discussed for the addPerson function
    also need to be considered and checked in the updatePersonalDetails function. If the Person's updated information meets the below conditions and any other
    conditions you may want to consider, the Person's information should be updated in the TXT file with the updated information, and the updatePersonalDetails
    function should return true. Otherwise, the Person's information should not be updated in the TXT file, and the updatePersonalDetails function should return false.

    Condition 1: If a person is under 18, their address cannot be changed.
    Condition 2: If a person's birthday is going to be changed, then no other personal detail (i.e, person's ID, firstName, lastName, address) can be changed.
    Condition 3: If the first character/digit of a person's ID is an even number, then their ID cannot be changed. */


    public boolean updatePersonalDetails(String newPersonID, String newFirstName, String newLastName, String newAddress, String newBirthdate) {

        //Validating new values using same rules as adddPerson()
        //For any new input that is invalid, update must be declined/rejected.
        if (!isValidPersonID(newPersonID)) return false;
        if (!isValidAddress(newAddress)) return false;
        if (!isValidBirthdate(newBirthdate)) return false;


        try {
            //Load all existing persons from the TXT file
            Path path = Path.of(FILE_NAME);
            if (!Files.exists(path)) return false;

            List<String> lines = Files.readAllLines(path);

            int targetIndex = -1;
            String[] oldParts = null;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                //Each line to be stored as: personID, firstName, lastName, address, birthdate
                String[] parts = line.split(",", 5);
                if (parts.length != 5) continue;

                if (parts[0].equals(this.personID)) {
                    targetIndex = i;
                    oldParts = parts;
                    break;
                }
            }

            //If person does not exist in the file, return false - cannot update them.
            if (targetIndex == -1) return false;

            //Extract old detail fields
            String oldPersonID = oldParts[0];
            String oldFirstName = oldParts[1];
            String oldLastName = oldParts[2];
            String oldAddress = oldParts[3];
            String oldBirthdate = oldParts[4];

            //Determine what fields will be altered
            boolean idChanged = !newPersonID.equals(oldPersonID);
            boolean firstnameChanged = !newFirstName.equals(oldFirstName);
            boolean lastnameChanged = !newLastName.equals(oldLastName);
            boolean addressChanged = !newAddress.equals(oldAddress);
            boolean birthdateChanged = !newBirthdate.equals(oldBirthdate);

            //Condition 1: If under 18 address cannot change
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

            LocalDate dob = LocalDate.parse(oldBirthdate, format);
            int age = java.time.Period.between(dob, LocalDate.now()).getYears();

            if (age < 18 && addressChanged) return false;


            //Condition 2: If birthdate going to be changed, no other personal detail cannot be changed
            if (birthdateChanged && (idChanged || firstnameChanged || lastnameChanged || addressChanged)) return false;

            //Condition 3: If the first character/digit of a person's ID is an even number, then their ID cannot be changed.
            int firstDigit = Character.getNumericValue(oldPersonID.charAt(0));
            if (firstDigit % 2 == 0 && idChanged) return false;

            //If changing ID, it is ensured that the new ID does not exist in the file (avoid duplicates)
            if (idChanged) {
                for (String line : lines) {
                    String[] parts = line.split(",", 5);
                    if (parts.length != 5) continue;

                    if (parts[0].equals(newPersonID)) return false;
                }
            }

            //Replace old line with the updated line and write back to the file
            String updatedLine = newPersonID + "," + newFirstName + "," + newLastName + "," + newAddress + "," + newBirthdate;
            lines.set(targetIndex, updatedLine);

            //Write back to the file
            //Overwrites old file content with modified details
            Files.write(path, lines);

            //If execution is successful, the update is successful
            return true;


        } catch (IOException e) {
            //For any IO error, could mean the update could not be completed
            e.printStackTrace();

            return false;
        }
    }


    public boolean addID() {

        return true;
    }
}