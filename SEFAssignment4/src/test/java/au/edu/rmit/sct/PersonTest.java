package au.edu.rmit.sct;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class PersonTest  {

    // Test cases for addPerson()
    @Test
    void testAddPerson_validCase() {

        Person p1 = new Person(
                "29ab!@#cDE",
                "Christian",
                "Vitale",
                "15|QueenStreet|Melbourne|Victoria|3000",
                "10-05-1995"
        );

        assertThrows(IllegalArgumentException.class,
            () -> p1.addPerson());
    }

    @Test
void testAddPerson_invalidID() {
    Person p1 = new Person(
            "19ab!@#cDE",
            "Christian",
            "Vitale",
            "15|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1995"
    );

     assertThrows(IllegalArgumentException.class,
            () -> p1.addPerson());
}


    @Test
    void testAddPerson_invalidAddress() {

        Person p1 = new Person(
                "39ab!@#cDE",
                "Christian",
                "Vitale",
                "15|QueenStreet|Melbourne|NSW|3000",
                "10-05-1995"
        );

        assertThrows(IllegalArgumentException.class,
            () -> p1.addPerson());
    }

    @Test
    void testAddPerson_invalidBirthdate() {

        Person p1 = new Person(
                "49ab!@#cDE",
                "Christian",
                "Vitale",
                "15|QueenStreet|Melbourne|Victoria|3000",
                "32-05-1995"  // Invalid date
        );

         assertThrows(IllegalArgumentException.class,
            () -> p1.addPerson());
    }



    @Test
    void testAddPerson_duplicateID() {

        Person p1 = new Person(
                "59ab!@#cDE",
                "Christian12",
                "Vitale",
                "15|QueenStreet|Melbourne|Victoria|3000",
                "10-05-1995"
        );

        assertThrows(IllegalArgumentException.class,
            () -> p1.addPerson());

        Person p2 = new Person(
                "59ab!@#cDE",
                "John",
                "Smith",
                "20|KingStreet|Melbourne|Victoria|3000",
                "11-06-1990"
        );

        assertThrows(IllegalArgumentException.class,
            () -> p2.addPerson());
    }

    private static final Path FILE_PATH = Path.of("src/main/resources/persons.txt");
    //Test cases for updatePersonalDetails()
    @Test
    void update_under_18_cannotChangeAddress_returnFalse() throws IOException {

        String oldId = "39ab!@#cDE";
        String line = oldId + ",Christian,Vitale,15|QueenStreet|Melbourne|Victoria|3000,10-05-2012";

        Files.write(FILE_PATH, List.of(line));

        //Create person object that matches the original existent record
        Person p = new Person(
                oldId,
                "Christian",
                "Vitale",
                "15|QueenStreet|Melbourne|Victoria|3000",
                "10-05-2012"
        );

        //Attempt to change Address (should reject)
        String newAddress = "99|NewStreet|Melbourne|Victoria|3000";
        boolean result = p.updatePersonalDetails(
                oldId,
                "Christian",
                "Vitale",
                newAddress,
                "10-05-2012"
        );

        assertFalse(result);

    }



    @Test
    void update_over_18_ChangeAddress_returnTrue() throws IOException {
        String oldId = "39ab!@#cDE";
        String originalAddress = "15|QueenStreet|Melbourne|Victoria|3000";
        String line = oldId + ",Christian,Vitale," + originalAddress + ",10-05-1995";
        Files.write(FILE_PATH, List.of(line));

        Person p = new Person(
                oldId,
                "Christian",
                "Vitale",
                originalAddress,
                "10-05-1995"
        );

        //Change to new address
        String newAddress = "99|NewStreet|Melbourne|Victoria|3000";
        boolean result = p.updatePersonalDetails(
                oldId,
                "Christian",
                "Vitale",
                newAddress,
                "10-05-1995"
        );

        //Return True
        assertTrue(result);

        List<String> after = Files.readAllLines(FILE_PATH);
        assertEquals(1, after.size());
        assertTrue(after.get(0).contains(newAddress));
    }




    @Test
    void verifyBirthdateChanged_returnFalse() throws IOException {
        String oldId = "39ab!@#cDE";
        String address = "15|QueenStreet|Melbourne|Victoria|3000";
        String line = oldId + ",Christian,Vitale," + address + ",10-05-1995";
        Files.write(FILE_PATH, List.of(line));

        Person p = new Person(
                oldId,
                "Christian",
                "Vitale",
                address,
                "10-05-1995"
        );

        boolean result = p.updatePersonalDetails(
                oldId,
                "Chris",              //first name changed
                "Vitale",
                address,
                "10-05-1994"          //birthdate changed
        );

        //Return False
        assertFalse(result);


        List<String> after = Files.readAllLines(FILE_PATH);
        assertEquals(1, after.size());
        assertEquals(line, after.get(0));

    }



    @Test
    void update_evenFirstDigitId_cannotChangeId_returnFalse() throws IOException {

        //Old ID starts with even digit '2'
        String oldId = "29ab!@#cDE";
        String address = "15|QueenStreet|Melbourne|Victoria|3000";
        String line = oldId + ",Christian,Vitale," + address + ",10-05-1995";
        Files.write(FILE_PATH, List.of(line));

        Person p = new Person(
                oldId,
                "Christian",
                "Vitale",
                address,
                "10-05-1995"
        );


        String newId = "39ab!@#cDE";
        boolean result = p.updatePersonalDetails(
                newId,
                "Christian",
                "Vitale",
                address,
                "10-05-1995"
        );

        //Return false
        assertFalse(result);

        List<String> after = Files.readAllLines(FILE_PATH);
        assertEquals(1, after.size());
        assertEquals(line, after.get(0));
    }



    @Test
    void update_firstNameChanged_returnTrue() throws IOException {


        String oldId = "39ab!@#cDE";
        String address = "15|QueenStreet|Melbourne|Victoria|3000";
        String line = oldId + ",Christian,Vitale," + address + ",10-05-1995";
        Files.write(FILE_PATH, List.of(line));

        Person p = new Person(
                oldId,
                "Christian",
                "Vitale",
                address,
                "10-05-1995"
        );

        //Aim is to Change first name only
        boolean result = p.updatePersonalDetails(
                oldId,
                "Chris",        //change first name
                "Vitale",
                address,
                "10-05-1995"
        );

        //Should return true
        assertTrue(result);

        List<String> after = Files.readAllLines(FILE_PATH);
        assertEquals(1, after.size());
        assertTrue(after.get(0).contains("Chris"));
    }

}





