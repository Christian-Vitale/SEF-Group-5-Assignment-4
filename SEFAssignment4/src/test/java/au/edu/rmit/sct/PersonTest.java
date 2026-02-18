package au.edu.rmit.sct;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest  {

    // Test cases for addPerson()
    @Test
    void testAddPerson_case1() {

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
    void testAddPerson_case3() {
      
    }

    @Test
    void testAddPerson_case4() {
      
    }

    @Test
    void testAddPerson_case5() {
      
    }


}