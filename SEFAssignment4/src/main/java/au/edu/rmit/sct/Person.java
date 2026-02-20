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
    private String passport; 
    private String driversLicence; 
    private String medicare; 
    private String studentCard; 
    private static final String FILE_NAME = "src/main/resources/persons.txt";
    private static final String ID_FILE_NAME = "src/main/resources/id.txt";

    public Person(String personID, String firstName, String lastName,
                  String address, String birthdate) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;


    }

    public void setIdDetails(String passport, String driversLicence, String medicare, String studentCard) { 
        this.passport = passport; 
        this.driversLicence = driversLicence;
        this.medicare = medicare; 
        this.studentCard = studentCard; 
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

    public void addPerson() {

    StringBuilder errors = new StringBuilder();

    if (!isValidPersonID(personID)) errors.append("Invalid Person ID; ");
    if (!isValidAddress(address)) errors.append("Invalid Address; ");
    if (!isValidBirthdate(birthdate)) errors.append("Invalid Birthdate; ");
    if (isDuplicateID(personID)) errors.append("Duplicate Person ID; ");

    if (errors.length() > 0) throw new IllegalArgumentException(errors.toString());


    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
        writer.write(personID + "," + firstName + "," + lastName + "," + address + "," + birthdate);
        writer.newLine();
    } catch (IOException e) {
        throw new RuntimeException("Error writing to file.");
    }
}

    //addID helper function to validate passport 
    private boolean isValidPassport(String passport) { 
        if (passport == null) { 
            return false; 
        }
        passport = passport.trim();
        if (passport.length() != 8) { 
            return false; 
        }
        if (!Character.isUpperCase(passport.charAt(0))) { 
            return false;
        }
        if (!Character.isUpperCase(passport.charAt(1))) { 
            return false; 
        }

        for (int i = 2; i < 8; i++) { 
            if (!Character.isDigit(passport.charAt(i))) { 
                return false; 
            }
        }
        return true; 
    }

    //addID helper function to validate Drivers licence
    private boolean isValidDriversLicence(String licence) { 
        if (licence == null) { 
            return false; 
        }
        licence = licence.trim(); 
        if (licence.length() != 10) { 
            return false; 
        }
        if (!Character.isUpperCase(licence.charAt(0))) { 
            return false; 
        }
        if (!Character.isUpperCase(licence.charAt(1))) { 
            return false; 
        }

        for (int i = 2; i < 10; i++) { 
            if (!Character.isDigit(licence.charAt(i))) { 
                return false; 
            }
        }

        return true;
    }

    //addID helper function to validate medicare
    private boolean isValidMedicare(String medicare) { 
        if (medicare == null) { 
            return false; 
        }
        medicare = medicare.trim(); 
        if (medicare.length() != 9) { 
            return false; 
        }
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(medicare.charAt(i))) {
                return false;
            }
        }
        return true; 
    }

    //addID helper function to validate student id card
    private boolean isValidStudentCard(String studentCard) { 
        if (studentCard == null) { 
            return false; 
        }
        studentCard = studentCard.trim(); 
        if (studentCard.length() != 12) { 
            return false;
        }
        for (int i = 0; i < 12; i++) { 
            if (!Character.isDigit(studentCard.charAt(i))) { 
                return false;
            }
        }
        return true; 
    }

    private int getAge() { 
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
        LocalDate dob = LocalDate.parse(this.birthdate, format);
        return java.time.Period.between(dob, LocalDate.now()).getYears();
    }

    private boolean alreadyHasIdType(String idType) { 
        try { 
            Path path = Path.of(ID_FILE_NAME); 
            if (!Files.exists(path)) return false; 

            List<String> lines = Files.readAllLines(path); 
            for (String line: lines) { 
                //Stored as: personID, idType, idValue 
                String[] parts = line.split(",", 3); 
                if (parts.length != 3) continue; 

                if (parts[0].equals(this.personID) && parts[1].equals(idType)) { 
                    return true; 
                }
            }
            return false; 
        }

        catch (IOException e) { 
            return false;
        }
    }

    //helper function to check if that the user has no other ID types recorded 
    private boolean alreadyHasNonStudentIDS() { 
        return alreadyHasIdType("PASSPORT") || alreadyHasIdType("DRIVERS_LICENCE") || alreadyHasIdType("MEDICARE");
    }

    // helper function for Test use only
    public static void clearIDFileForTesting() { 
        try {
            Files.write(Path.of(ID_FILE_NAME), List.of());
        } 
        catch (IOException e) {
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
        //Must have a valid birthdate as student id cards depends on the age. 
        if (!isValidBirthdate(this.birthdate)) { 
            return false; 
        }

        //Only allows one ID to be provided per call helping to keep tests simple and test case behaviour deterministic 
        int providedCount = 0; 
        if (passport != null && !passport.trim().isEmpty()) { 
            providedCount++;
        }
        if (driversLicence != null && !driversLicence.trim().isEmpty()) { 
            providedCount++;
        }
        if (medicare != null && !medicare.trim().isEmpty()) { 
            providedCount++;
        }
        if (studentCard != null && !studentCard.trim().isEmpty()) { 
            providedCount++;
        }

        if (providedCount != 1) { 
            return false; 
        }

        String idType; 
        String idValue; 

        //Passport 
        if (passport != null && !passport.trim().isEmpty()) { 
            idType = "PASSPORT"; 
            idValue = passport.trim();

            if (!isValidPassport(idValue)) { 
                return false; 
            }
        }

        //Drivers licence 
        else if (driversLicence != null && !driversLicence.trim().isEmpty()) {
            idType = "DRIVERS_LICENCE"; 
            idValue = driversLicence.trim();

            if (!isValidDriversLicence(idValue)){ 
                return false; 
            }
        }

        //Medicare 
        else if (medicare != null && !medicare.trim().isEmpty()) { 
            idType = "MEDICARE"; 
            idValue = medicare.trim();

            if (!isValidMedicare(idValue)){ 
                return false; 
            }
        }

        //Student card 
        else { 
            idType = "STUDENT_CARD"; 
            idValue = studentCard.trim(); 

            //Condition: only under 18 users can add a student id card 
            int age = getAge(); 
            if (age >= 18) { 
                return false; 
            }

            //student ID card is only allowed if other ID types do not exist 
            if (alreadyHasNonStudentIDS()) { 
                return false;
            }

            //Checks student ID's format
            if (!isValidStudentCard(idValue)) { 
                return false; 
            }
        }

        //Prevent duplicate similar id types for the same user 
        if (alreadyHasIdType(idType)) { 
            return false; 
        }

        //Write to file 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ID_FILE_NAME, true))) { 
            writer.write(this.personID + "," + idType + "," + idValue); 
            writer.newLine();
            return true;
        }
        catch (IOException e) { 
            return false; 
        }
    }
}