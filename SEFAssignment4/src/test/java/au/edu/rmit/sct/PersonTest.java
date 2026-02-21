package au.edu.rmit.sct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;



public class PersonTest  {
    @BeforeEach
    void resetFile() throws IOException {
        Path path = Path.of("src/main/resources/persons.txt");
        Files.write(path, List.of());   //Overwrites file with empty content to avoid clash when running independently
    }

    @Test
    void testAddPerson_validCase() {
        Person p1 = new Person(
                "29ab!@#cDE",
                "Sudhan",
                "Kandel",
                "15|QueenStreet|Melbourne|Victoria|3000",
                "10-05-1995"
        );
        // This SHOULD work, so we assert it doesn't throw an error
        assertDoesNotThrow(() -> p1.addPerson());
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
void testAddPerson_duplicateID() throws IOException {

    Path path = Path.of("src/main/resources/persons.txt");

    // Ensure file exists
    if (!Files.exists(path)) {
        Files.createFile(path);
    }

    // Add a known record first
    Files.write(path, List.of(
        "59ab!@#cDE,Christian,Vitale,25|QueenStreet|Melbourne|Victoria|3000,10-05-1995"
    ));

    Person duplicate = new Person(
            "59ab!@#cDE",
            "John",
            "Smith",
            "20|KingStreet|Melbourne|Victoria|3000",
            "11-06-1990"
    );

    assertThrows(IllegalArgumentException.class,
            duplicate::addPerson);
}


    @Test
void update_under_18_cannotChangeAddress_returnFalse() throws IOException {

    //Create person (under 18)
    Person p = new Person(
            "88pq!@#tUV",
            "Barsha",
            "Ramzi",
            "15|QueenStreet|Melbourne|Victoria|3000",
            "10-05-2012"
    );


    p.addPerson();   

    //Attempt to change address
    String newAddress = "999|NewStreet|Melbourne|Victoria|3000";

    boolean result = p.updatePersonalDetails(
            "88pq!@#tUV",
            "Barsha",
            "Ramzi",
            newAddress,
            "10-05-2012"   //Should Reject
    );

    assertFalse(result);
}



    @Test
void update_over_18_ChangeAddress_returnTrue() throws IOException {

    //Create Person (over 18)
    Person p = new Person(
            "77xy!@#kLM",
            "Binisha",
            "Chapagain",
            "2000|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1995"
    );

    p.addPerson();  

    String newAddress = "99|NewStreet|Melbourne|Victoria|3000";

    boolean result = p.updatePersonalDetails(
            "77xy!@#kLM",
            "Binisha",
            "Chapagain",
            newAddress,
            "10-05-1995"  //Update should be successful
    );

    assertTrue(result);
}





    @Test
void verifyBirthdateChanged_returnFalse() throws IOException {

    // Create and add person (existing data)
    Person p = new Person(
            "43ab!@#cDE",
            "Peter",
            "Parker",
            "199|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1995"
    );

    p.addPerson();  

    // Attempt to update with different birthdate
    boolean result = p.updatePersonalDetails(
            "43ab!@#cDE",
            "Benjamin",              // First name changed
            "Parker",
            "2000|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1994"          // Birthdate changed → should fail
    );

    // Should return false
    assertFalse(result);

   
}




   @Test
void update_evenFirstDigitId_cannotChangeId_returnFalse() throws IOException {

    // Create and add person whose ID starts with even digit '2'
    Person p = new Person(
            "23ab!@#cDE",
            "Anthony",
            "Gristwood",
            "15|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1995"
    );

    p.addPerson();  

    // Attempt to change ID (should fail because old ID starts with even digit)
    String newId = "39ab!@#cDE";

    boolean result = p.updatePersonalDetails(
            newId,
            "Anthony",
            "Gristwood",
            "15|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1995"
    );

    // Should return false
    assertFalse(result);


}



   @Test
void update_firstNameChanged_returnTrue() throws IOException {

    // Create and add person
    Person p = new Person(
            "95ab!@#zRT",
            "Christian",
            "Vitale",
            "15|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1995"
    );

    p.addPerson();  

    // Change only first name
    boolean result = p.updatePersonalDetails(
            "95ab!@#zRT",
            "Chris",        // new first name
            "Vitale",
            "15|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1995"
    );


    assertTrue(result);

}

}





