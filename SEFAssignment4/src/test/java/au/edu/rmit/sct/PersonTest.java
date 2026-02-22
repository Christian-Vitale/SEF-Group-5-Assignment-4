package au.edu.rmit.sct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;



public class PersonTest  {

        //Helper method for addID tests; checks id.txt exists and is empty to ensure tests aren't affecting each other 
        private void resetIDFile() throws IOException { 
                Path idPath = Path.of("src/main/resources/id.txt"); 

                Files.createDirectories(idPath.getParent());

                //Create id.txt if it does not exist 
                if (!Files.exists(idPath)) { 
                        Files.createFile(idPath); 
                }

                //Clear the file so each test starts with a clean slate 
                Files.write(idPath, List.of());
        }

    @Test
    void testAddPerson_validCase() {
        Person p1 = new Person(
                "29ab!@#cDE",
                "Sudhan",
                "Kandel",
                "15|CoomaStreet|Melbourne|Victoria|3000",
                "10-05-1995"
        );
        // This SHOULD work, so we assert it doesn't throw an error
        assertDoesNotThrow(() -> p1.addPerson());
    
    Person p2= new Person(
            "89ab!@#cDE",
            "Amit",
            "Sharma",
            "101|CollinsStreet|Melbourne|Victoria|3000",
            "15-08-1992"
    );
    // Should pass validation
    assertDoesNotThrow(() -> p2.addPerson());


    Person p3 = new Person(
            "99ab!@#cDE",
            "Sarah",
            "Jenkins",
            "202|LygonStreet|Carlton|Victoria|3053",
            "22-11-1988"
    );
    // Should pass validation
    assertDoesNotThrow(() -> p3.addPerson());
    }

    @Test
void testAddPerson_invalidID() {
    Person p1 = new Person(
            "19ab!@#cDE", //this is invalid ID so this data should not store in persons.txt
            "Liam",
            "Vitale",
            "15|QueenStreet|Melbourne|Victoria|3000",
            "10-05-1995"
    );

     assertThrows(IllegalArgumentException.class,
            () -> p1.addPerson());



        Person p2 = new Person(
        "9ab!@#cDE", // invalid ID (only one starting digit)
        "Olivia",
        "Thompson",
        "22|KingStreet|Melbourne|Victoria|3000",
        "12-07-1992"
);
assertThrows(IllegalArgumentException.class,
            () -> p2.addPerson());


            Person p3 = new Person(
        "ab9!@#cDE", // invalid ID (does not start with digits)
        "Noah",
        "Martinez",
        "8|GeorgeStreet|Melbourne|Victoria|3000",
        "03-11-1998"
);
assertThrows(IllegalArgumentException.class,
            () -> p3.addPerson());
}


    @Test
    void testAddPerson_invalidAddress() {

        Person p1 = new Person(
                "39ab!@#cDE",
                "Barsha",
                "Chapagain",
                "45|StationStreet|Melbourne|NSW|3000", //This address is invalid because state should have Victoria
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

            Person p2 = new Person(
        "59ab!@#cDE",
        "Emma",
        "Robinson",
        "18|LonsdaleStreet|Melbourne|Victoria|3000",
        "15-13-1995"  // Invalid month (13 does not exist)
);
 assertThrows(IllegalArgumentException.class,
            () -> p2.addPerson());

            Person p3 = new Person(
        "69ab!@#cDE",
        "James",
        "Walker",
        "42|CollinsStreet|Melbourne|Victoria|3000",
        "29-02-1993"  // Invalid date (1993 is not a leap year)
);
assertThrows(IllegalArgumentException.class,
            () -> p3.addPerson());

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
            "59ab!@#cDE",// same Id already exist in the file
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
        @Test
        void addID_validPassport_returnsTrue_andWritesLine() throws IOException { 
                //resets file to ensure test is independent 
                resetIDFile();

                Person p = new Person("56s_d%&fAB", "Nyan", "Test", "15|QueenStreet|Melbourne|Victoria|3000", "10-05-2012");

                //Provide passport type ID 
                p.setIdDetails("AB123456", null, null, null);

                //Should pass test
                assertTrue(p.addID());

                //Verify file output 
                List<String> lines = Files.readAllLines(Path.of("src/main/resources/id.txt"));
                assertEquals(1, lines.size()); 
                assertEquals("56s_d%&fAB,PASSPORT,AB123456", lines.get(0));
        }

        @Test
        void addID_invalidPassport_lowercase_returnsFalse_andDoesNotWrite() throws IOException { 
                resetIDFile();

                Person p = new Person("56s_d%&fAB", "Nyan", "Test", "15|QueenStreet|Melbourne|Victoria|3000", "10-05-2012");

                //Invalid as first two characters must be uppercase letters 
                p.setIdDetails("aB123456", null, null, null);

                assertFalse(p.addID());

                //File should remain empty 
                List<String> lines = Files.readAllLines(Path.of("src/main/resources/id.txt"));
                assertTrue(lines.isEmpty()); 
        }

        @Test 
        void addID_validMedicare_returnsTrue_andWritesLine() throws IOException { 
                resetIDFile(); 

                Person p = new Person("56s_d%&fAB", "Nyan", "Test", "15|QueenStreet|Melbourne|Victoria|3000", "10-05-2012");

                //Medicare must be exactly 9 digits
                p.setIdDetails(null, null, "123456789", null);

                assertTrue(p.addID());

                List<String> lines = Files.readAllLines(Path.of("src/main/resources/id.txt"));
                assertEquals(1, lines.size()); 
                assertEquals("56s_d%&fAB,MEDICARE,123456789", lines.get(0));
        }

        @Test
        void addID_studentCard_under18_noOtherIDs_returnsTrue() throws IOException { 
                resetIDFile();

                Person p = new Person("56s_d%&fAB", "Nyan", "Test", "15|QueenStreet|Melbourne|Victoria|3000", "10-05-2012");
                
                //Student card must be exactly 12 digits
                p.setIdDetails(null, null, null, "123456789012");

                assertTrue(p.addID());

                List<String> lines = Files.readAllLines(Path.of("src/main/resources/id.txt"));
                assertEquals(1, lines.size()); 
                assertEquals("56s_d%&fAB,STUDENT_CARD,123456789012", lines.get(0));
        }

        @Test
        void addID_studentCard_rejectedIfNonStudentIdExists_returnsFalse() throws IOException { 
                resetIDFile();

                Person p = new Person("56s_d%&fAB", "Nyan", "Test", "15|QueenStreet|Melbourne|Victoria|3000", "10-05-2012");

                //Add a non-student ID first 
                p.setIdDetails(null, null, "123456789", null);
                assertTrue(p.addID());

                //add student id card, and should fail because alreadyHasNonStudentIDS() should becomes true 
                p.setIdDetails(null, null, null, "123456789012");
                assertFalse(p.addID()); 

                //File should still only contain the Medicare entry 
                List<String> lines = Files.readAllLines(Path.of("src/main/resources/id.txt"));
                assertEquals(1, lines.size());
                assertEquals("56s_d%&fAB,MEDICARE,123456789", lines.get(0)); 
        }

        @Test 
        void addID_validDriversLicence_returnsTrue_OneLineWritten() throws IOException { 
                //start from empty id.txt 
                resetIDFile(); 

                Person p = new Person("56s_d%&fAB", "Nyan", "Test", "15|QueenStreet|Melbourne|Victoria|3000","10-05-2012");

                //Driver's licence ID should be 10 chars total, first 2 are uppercase, remaining should be 8 digits
                p.setIdDetails(null, "AB12345678", null, null);
 
                assertTrue(p.addID());

                //Verify it wrote exactly one line in the correct format 
                List<String> lines = Files.readAllLines(Path.of("src/main/resources/id.txt"));
                assertEquals(1, lines.size()); 
                assertEquals("56s_d%&fAB,DRIVERS_LICENCE,AB12345678", lines.get(0));
        }




   @Test
    void Validpassport_returnTrue() {
        Person.clearIDFileForTesting();


        //Create person
       Person p = new Person(
               "49ab!@#zRS",
               "Dan",
               "Carter",
               "15|QueenStreet|Melbourne|Victoria|3000",
               "07-12-1995"
       );

       //Set Passport details
       //Exactly 8 characters long
       //First two characters should be uppercase
       //Rest should be numbers 0-9
       p.setIdDetails("AC135792", null, null, null );

       boolean result = p.addID();

       //Must return true
       assertTrue(result);


   }

}





