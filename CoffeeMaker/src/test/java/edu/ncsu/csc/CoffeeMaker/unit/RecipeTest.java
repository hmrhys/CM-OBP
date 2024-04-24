package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setIngredient( new Ingredient( "CHOCOLATE", 1 ) );
        r1.setIngredient( new Ingredient( "MILK", 0 ) );
        r1.setIngredient( new Ingredient( "SUGAR", 0 ) );
        r1.setIngredient( new Ingredient( "COFFEE", 0 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.setIngredient( new Ingredient( "CHOCOLATE", 1 ) );
        r2.setIngredient( new Ingredient( "MILK", 1 ) );
        r2.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r2.setIngredient( new Ingredient( "COFFEE", 1 ) );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        r1.setIngredient( new Ingredient( "CHOCOLATE", -12 ) );
        r1.setIngredient( new Ingredient( "MILK", 0 ) );
        r1.setIngredient( new Ingredient( "SUGAR", 0 ) );
        r1.setIngredient( new Ingredient( "COFFEE", 0 ) );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.setIngredient( new Ingredient( "CHOCOLATE", 1 ) );
        r2.setIngredient( new Ingredient( "MILK", 1 ) );
        r2.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r2.setIngredient( new Ingredient( "COFFEE", 1 ) );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, (int) retrieved.getIngredient( "COFFEE" ) );
        Assertions.assertEquals( 1, (int) retrieved.getIngredient( "MILK" ) );
        Assertions.assertEquals( 1, (int) retrieved.getIngredient( "SUGAR" ) );
        Assertions.assertEquals( 0, (int) retrieved.getIngredient( "CHOCOLATE" ) );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    /**
     * [ Added by hmreese2 ] Explores paths for Recipe.checkRecipe() boolean
     * return value
     */
    @Test
    @Transactional
    public void testCheckRecipe () {
        // ensure nothing initially
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // test with recipe set to 0
        final Recipe r0 = createRecipe( "Coffee", 10, 0, 0, 0, 0 );
        Assertions.assertTrue( r0.checkRecipe() );

        // test with recipe all 0 but coffee
        final Recipe r1 = createRecipe( "Coffee", 10, 1, 0, 0, 0 );
        Assertions.assertFalse( r1.checkRecipe() );

        // test with recipe all 0 but milk
        final Recipe r2 = createRecipe( "Coffee", 10, 0, 1, 0, 0 );
        Assertions.assertFalse( r2.checkRecipe() );

        // test with recipe all 0 but sugar
        final Recipe r3 = createRecipe( "Coffee", 10, 0, 0, 1, 0 );
        Assertions.assertFalse( r3.checkRecipe() );

        // test with recipe all 0 but chocolate
        final Recipe r4 = createRecipe( "Coffee", 10, 0, 0, 0, 1 );
        Assertions.assertFalse( r4.checkRecipe() );

        // test with all 0 except 2
        final Recipe r5 = createRecipe( "Coffee", 10, 0, 0, 1, 1 );
        Assertions.assertFalse( r5.checkRecipe() );

        // test with only 1 zero
        final Recipe r6 = createRecipe( "Coffee", 10, 1, 1, 0, 1 );
        Assertions.assertFalse( r6.checkRecipe() );
    }

    /**
     * This test checks that update recipe works properly
     *
     * Recipe.java line coverage: 51.0% --> 63.3%
     * Recipe.java branch coverage: 40.9% --> 40.9%
     * Recipe.updateRecipe() coverage: 0.0% --> 100.0%
     *
     * @author katie hollowell (krhollow)
     */
    @Test
    @Transactional
    public void testUpdateRecipe () {
        // create a recipe named "Mocha"
        final Recipe r = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        // save it to the system
        service.save( r );

        // check that it's in the system and we can find it
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );
        Assertions.assertEquals( r, service.findByName( "Mocha" ) );

        // create the recipe we want Mocha to be updated to
        final Recipe update = createRecipe( "Mocha 2.0", 55, 4, 2, 2, 3 );

        // update mocha to new recipe
        r.updateRecipe( update );
        // save it to the system
        service.save( r );

        // there should still only be one recipe in the system
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );
        // get the recipe from the service again (same name because update
        // doesn't change the name)
        final Recipe retrieved = service.findByName( "Mocha" );

        // check that all of the values were updated
        Assertions.assertEquals( 55, (int) retrieved.getPrice() );
        Assertions.assertEquals( 4, (int) retrieved.getIngredient( "COFFEE" ) );
        Assertions.assertEquals( 2, (int) retrieved.getIngredient( "MILK" ) );
        Assertions.assertEquals( 2, (int) retrieved.getIngredient( "SUGAR" ) );
        Assertions.assertEquals( 3, (int) retrieved.getIngredient( "CHOCOLATE" ) );
    }

    /**
     * This test checks that the equals() method works properly
     *
     * Recipe.java line coverage: 63.3% --> 85.7%
     * Recipe.java branch coverage: 40.9% --> 90.9%
     * Recipe.equals() line + branch coverage: 15.4% | 8.3% --> 100.0%
     *
     * @author katie hollowell (krhollow)
     */
    @Test
    @Transactional
    public void testRecipeEquals () {
        // create a recipe named "Coffee"
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // save it to the system
        service.save( r1 );

        // create another recipe named "Coffee"
        final Recipe r2 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // don't save it to the system

        // check exact same
        Assertions.assertTrue( r1.equals( r1 ) );

        // check null
        Assertions.assertFalse( r1.equals( null ) );

        // check not same class
        final String r3 = "Coffee";
        Assertions.assertFalse( r1.equals( r3 ) );

        // check null names
        final Recipe r4 = createRecipe( null, 50, 3, 1, 1, 0 );
        final Recipe r5 = createRecipe( null, 50, 3, 1, 1, 0 );
        Assertions.assertTrue( r4.equals( r5 ) );

        // check null names
        Assertions.assertFalse( r4.equals( r1 ) );

        // check names not equal
        final Recipe r6 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        Assertions.assertFalse( r1.equals( r6 ) );

        // check names equals
        Assertions.assertTrue( r1.equals( r2 ) );
    }

    private Recipe createRecipe ( final String name, @Min ( 0 ) final Integer price, @Min ( 0 ) final Integer coffee,
            @Min ( 0 ) final Integer milk, @Min ( 0 ) final Integer sugar, @Min ( 0 ) final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient( new Ingredient( "COFFEE", coffee ) );
        recipe.addIngredient( new Ingredient( "MILK", milk ) );
        recipe.addIngredient( new Ingredient( "SUGAR", sugar ) );
        recipe.addIngredient( new Ingredient( "CHOCOLATE", chocolate ) );

        return recipe;
    }

    /**
     * [Test 3[1]/3] Test test toString() function in Recipe.java
     *
     * @author Zelda Lu(wlu27)
     */
    @Test
    @Transactional
    public void testToString () {
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setIngredient( new Ingredient( "COFFEE", 1 ) );
        r1.setIngredient( new Ingredient( "MILK", 0 ) );
        r1.setIngredient( new Ingredient( "SUGAR", 0 ) );
        r1.setIngredient( new Ingredient( "CHOCOLATE", 0 ) );
        service.save( r1 );

        Assertions.assertEquals(
                "Black Coffee with ingredients [Ingredient [ingredient=COFFEE, amount=1], Ingredient [ingredient=MILK, amount=0], Ingredient [ingredient=SUGAR, amount=0], Ingredient [ingredient=CHOCOLATE, amount=0]]",
                r1.toString() );

    }

    /**
     * [Test 3[2]/3] Test test getName() function in Recipe.java
     *
     * @author Zelda Lu(wlu27)
     */
    @Test
    @Transactional
    public void testGetName () {
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setIngredient( new Ingredient( "COFFEE", 1 ) );
        r1.setIngredient( new Ingredient( "MILK", 0 ) );
        r1.setIngredient( new Ingredient( "SUGAR", 0 ) );
        r1.setIngredient( new Ingredient( "CHOCOLATE", 0 ) );
        service.save( r1 );

        Assertions.assertEquals( "Black Coffee", r1.getName() );

    }

}
