package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.setIngredient( "COFFEE", 500 );
        ivt.setIngredient( "MILK", 500 );
        ivt.setIngredient( "SUGAR", 500 );
        ivt.setIngredient( "CHOCOLATE", 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        recipe.setIngredient( new Ingredient( "CHOCOLATE", 10 ) );
        recipe.setIngredient( new Ingredient( "MILK", 20 ) );
        recipe.setIngredient( new Ingredient( "SUGAR", 5 ) );
        recipe.setIngredient( new Ingredient( "COFFEE", 1 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, (int) i.getIngredient( "CHOCOLATE" ) );
        Assertions.assertEquals( 480, (int) i.getIngredient( "MILK" ) );
        Assertions.assertEquals( 495, (int) i.getIngredient( "SUGAR" ) );
        Assertions.assertEquals( 499, (int) i.getIngredient( "COFFEE" ) );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients( 5, 3, 7, 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getIngredient( "COFFEE" ),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getIngredient( "MILK" ),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getIngredient( "SUGAR" ),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getIngredient( "CHOCOLATE" ),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( -5, 3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "COFFEE" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "MILK" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "SUGAR" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "CHOCOLATE" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, -3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "COFFEE" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "MILK" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "SUGAR" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "CHOCOLATE" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, -7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "COFFEE" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "MILK" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "SUGAR" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "CHOCOLATE" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, 7, -2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "COFFEE" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "MILK" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "SUGAR" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "CHOCOLATE" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );

        }

    }

    /**
     * [Test 1/3] Test test checkMilk() function in Inventory.java
     *
     * @author Zelda Lu(wlu27)
     */
    @Test
    @Transactional
    public void testCheckMilk () {
        // check inventory is all 500
        final Inventory i = inventoryService.getInventory();
        Assertions.assertEquals( 500, (int) i.getIngredient( "COFFEE" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "MILK" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "SUGAR" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "CHOCOLATE" ) );

        // Test invalid - Not an integer
        try {
            i.checkIngredient( "MILK", "Four" );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( "Units of MILK must be a positive integer", e.getMessage() );
        }

        // Test invalid - Negative Number
        try {
            i.checkIngredient( "MILK", "-2" );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( "Units of MILK must be a positive integer", e.getMessage() );
        }

        // Test valid
        Assertions.assertEquals( 10, i.checkIngredient( "MILK", "10" ) );

    }

    /**
     * [Test 2/3] Test test toString() function in Inventory.java
     *
     * @author Zelda Lu(wlu27)
     */
    @Test
    @Transactional
    public void testToString () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setIngredient( new Ingredient( "COFFEE", 1 ) );
        recipe.setIngredient( new Ingredient( "MILK", 20 ) );
        recipe.setIngredient( new Ingredient( "SUGAR", 5 ) );
        recipe.setIngredient( new Ingredient( "CHOCOLATE", 10 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        final String output = "COFFEE: 499\n" + "MILK: 480\n" + "SUGAR: 495\n" + "CHOCOLATE: 490\n";

        Assertions.assertEquals( output, i.toString() );

    }

    /*
     * [ hmreese2 - Test 2/3 ] Testing Inventory.checkChocolate() functionality
     * with valid and invalid input
     */
    @Test
    @Transactional
    public void testCheckChocolate () {
        // create testable inventory instance and check contents (personal
        // sanity)
        final Inventory i = inventoryService.getInventory();
        Assertions.assertEquals( 500, (int) i.getIngredient( "COFFEE" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "MILK" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "SUGAR" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "CHOCOLATE" ) );

        // test with valid - pos integers
        Assertions.assertEquals( 20, i.checkIngredient( "CHOCOLATE", "20" ) );
        Assertions.assertEquals( 0, i.checkIngredient( "CHOCOLATE", "0" ) );

        // test with invalid - number spelled out
        try {
            i.checkIngredient( "CHOCOLATE", "Twenty" );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( "Units of CHOCOLATE must be a positive integer", e.getMessage() );
        }

        // test with invalid - negative integer
        try {
            i.checkIngredient( "CHOCOLATE", "-8" );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( "Units of CHOCOLATE must be a positive integer", e.getMessage() );
        }

    }

    /*
     * [ hmreese2 - Test 3/3 ] Testing Inventory.checkCoffee() functionality
     * with valid and invalid input
     */
    @Test
    @Transactional
    public void testCheckCoffee () {
        // create testable inventory instance and check contents (personal
        // sanity)
        final Inventory i = inventoryService.getInventory();
        Assertions.assertEquals( 500, (int) i.getIngredient( "COFFEE" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "MILK" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "SUGAR" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "CHOCOLATE" ) );

        // test with valid - pos integers
        Assertions.assertEquals( 20, i.checkIngredient( "COFFEE", "20" ) );
        Assertions.assertEquals( 0, i.checkIngredient( "COFFEE", "0" ) );

        // test with invalid - number spelled out
        try {
            i.checkIngredient( "COFFEE", "Twenty" );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( "Units of COFFEE must be a positive integer", e.getMessage() );
        }

        // test with invalid - negative integer
        try {
            i.checkIngredient( "COFFEE", "-8" );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( "Units of COFFEE must be a positive integer", e.getMessage() );
        }

        // test with invalid - positive floating point number / decimal
        try {
            i.checkIngredient( "COFFEE", "1.0" );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( "Units of COFFEE must be a positive integer", e.getMessage() );
        }
    }

    /**
     * This test checks that checkSugar() works properly
     *
     * Inventory.java line coverage: 63.9% --> 72.2%
     * Inventory.java branch coverage: 67.6% --> 73.5%
     * Inventory.checkSugar() line + branch coverage: 0.0% | 0.0% --> 100.0%
     *
     * @author katie hollowell (krhollow)
     */
    @Test
    @Transactional
    public void testCheckSugar () {
        // check inventory is all 500
        final Inventory i = inventoryService.getInventory();
        Assertions.assertEquals( 500, (int) i.getIngredient( "COFFEE" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "MILK" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "SUGAR" ) );
        Assertions.assertEquals( 500, (int) i.getIngredient( "CHOCOLATE" ) );

        // check non-integer
        Assertions.assertThrows( IllegalArgumentException.class, () -> {
            i.checkIngredient( "SUGAR", "blueberries" );
        } );

        // check negative integer
        Assertions.assertThrows( IllegalArgumentException.class, () -> {
            i.checkIngredient( "SUGAR", "-42" );
        } );

        // check positive integer
        Assertions.assertEquals( 5, i.checkIngredient( "SUGAR", "5" ) );
    }

}
