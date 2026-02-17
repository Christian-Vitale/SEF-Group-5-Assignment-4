package au.edu.rmit.sct;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonFunctionTests {

    // Test cases for addPerson()
    @Test
    void testAddPerson_case1() {

    }

    @Test
    void testAddPerson_case2() {
        Person p1=new Person("29ab!@#cdE", "Christian", "Vitale",
 "15|QueenStreet|Melbourne|Victoria|3000",
 "10-05-1995");
 assertTrue(p1.addPerson(), "Valid person should be added successfully");
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