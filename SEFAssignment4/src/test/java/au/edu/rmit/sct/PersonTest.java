package au.edu.rmit.sct;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> p1.addPerson());

    System.out.println("Error message: " + ex.getMessage());
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

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> p1.addPerson());

        System.out.println("Error message: " + ex.getMessage());
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

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> p1.addPerson());

         System.out.println("Error message: " + ex.getMessage());
    }
    @Test
    void testAddPerson_case5() {
      
    }


}