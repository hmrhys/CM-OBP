package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {

    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    @Test
    @Transactional
    public void testRecipes () {

        final Recipe r = new Recipe();

        /* set fields here */
        r.setName( "Mocha" );
        r.setPrice( 350 );
        r.setIngredient( new Ingredient( "COFFEE", 2 ) );
        r.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r.setIngredient( new Ingredient( "MILK", 1 ) );
        r.setIngredient( new Ingredient( "CHOCOLATE", 1 ) );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        /* Test all of the fields! You can also us assertAll. */
        Assertions.assertEquals( "Mocha", r.getName() );
        Assertions.assertEquals( 350, r.getPrice() );
        Assertions.assertEquals(
                "[Ingredient [ingredient=COFFEE, amount=2], Ingredient [ingredient=SUGAR, amount=1], Ingredient [ingredient=MILK, amount=1], Ingredient [ingredient=CHOCOLATE, amount=1]]",
                r.getIngredients().toString() );
    }

    @Test
    @Transactional
    public void testGetRecipeByName () {
        final Recipe r = new Recipe();

        /* set fields here */
        r.setName( "Mocha" );
        r.setPrice( 350 );
        r.setIngredient( new Ingredient( "COFFEE", 2 ) );
        r.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r.setIngredient( new Ingredient( "MILK", 1 ) );
        r.setIngredient( new Ingredient( "CHOCOLATE", 1 ) );

        recipeService.save( r );

        assertEquals( r, recipeService.findByName( "Mocha" ) );
    }

    @Test
    @Transactional
    public void testEditRecipe () {
        final Recipe r = new Recipe();

        /* set fields here */
        r.setName( "Mocha" );
        r.setPrice( 350 );
        r.setIngredient( new Ingredient( "COFFEE", 2 ) );
        r.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r.setIngredient( new Ingredient( "MILK", 1 ) );
        r.setIngredient( new Ingredient( "CHOCOLATE", 1 ) );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        dbRecipe.setPrice( 15 );
        dbRecipe.setIngredient( new Ingredient( "SUGAR", 12 ) );
        recipeService.save( dbRecipe );

        assertEquals( 1, recipeService.findAll().size() );
        Assertions.assertEquals( "Mocha", r.getName() );
        Assertions.assertEquals( 15, r.getPrice() );
        Assertions.assertEquals(
                "[Ingredient [ingredient=COFFEE, amount=2], Ingredient [ingredient=SUGAR, amount=12], Ingredient [ingredient=MILK, amount=1], Ingredient [ingredient=CHOCOLATE, amount=1]]",
                r.getIngredients().toString() );
    }

    @Test
    @Transactional
    public void testDeleteRecipe () {
        // check that there are no recipes in the database
        assertEquals( 0, recipeService.count() );

        final Recipe r = new Recipe();

        /* set fields here */
        r.setName( "Mocha" );
        r.setPrice( 350 );
        r.setIngredient( new Ingredient( "COFFEE", 2 ) );
        r.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r.setIngredient( new Ingredient( "MILK", 1 ) );
        r.setIngredient( new Ingredient( "CHOCOLATE", 1 ) );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );
        assertEquals( r, recipeService.findByName( "Mocha" ) );

        // mocha recipe is in the db
        final Recipe dbRecipe = dbRecipes.get( 0 );
        recipeService.delete( dbRecipe );

        // check that it was deleted
        assertEquals( 0, recipeService.count() );
        Assertions.assertNull( recipeService.findByName( "Mocha" ) );
    }

    @Test
    @Transactional
    public void testDeleteNARecipe () {
        // check that there are no recipes in the database
        assertEquals( 0, recipeService.count() );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        r1.setIngredient( new Ingredient( "COFFEE", 3 ) );
        r1.setIngredient( new Ingredient( "MILK", 1 ) );
        r1.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r1.setIngredient( new Ingredient( "CHOCOLATE", 0 ) );
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 50 );
        r2.setIngredient( new Ingredient( "COFFEE", 3 ) );
        r2.setIngredient( new Ingredient( "MILK", 1 ) );
        r2.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r2.setIngredient( new Ingredient( "CHOCOLATE", 2 ) );
        recipeService.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 60 );
        r3.setIngredient( new Ingredient( "COFFEE", 3 ) );
        r3.setIngredient( new Ingredient( "MILK", 2 ) );
        r3.setIngredient( new Ingredient( "SUGAR", 2 ) );
        r3.setIngredient( new Ingredient( "CHOCOLATE", 0 ) );
        // do NOT save this recipe to the system

        // check latte not in system
        Assertions.assertNull( recipeService.findByName( "Latte" ) );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 2, dbRecipes.size() );
        assertEquals( r1, recipeService.findByName( "Coffee" ) );
        assertEquals( r2, recipeService.findByName( "Mocha" ) );

        // try to delete recipe not in system
        recipeService.delete( r3 );

        // check that nothing was deleted
        assertEquals( 2, dbRecipes.size() );
        assertEquals( r1, recipeService.findByName( "Coffee" ) );
        assertEquals( r2, recipeService.findByName( "Mocha" ) );
    }

    @Test
    @Transactional
    public void testDeleteRecipes () {
        // check that there are no recipes in the database
        assertEquals( 0, recipeService.count() );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        r1.setIngredient( new Ingredient( "COFFEE", 3 ) );
        r1.setIngredient( new Ingredient( "MILK", 1 ) );
        r1.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r1.setIngredient( new Ingredient( "CHOCOLATE", 0 ) );
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 50 );
        r2.setIngredient( new Ingredient( "COFFEE", 3 ) );
        r2.setIngredient( new Ingredient( "MILK", 1 ) );
        r2.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r2.setIngredient( new Ingredient( "CHOCOLATE", 2 ) );
        recipeService.save( r2 );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 2, dbRecipes.size() );
        assertEquals( r1, recipeService.findByName( "Coffee" ) );
        assertEquals( r2, recipeService.findByName( "Mocha" ) );

        // coffee and mocha recipes are in the db
        final Recipe dbRecipe = recipeService.findByName( "Mocha" );
        recipeService.delete( dbRecipe );

        // check that it was deleted
        assertEquals( 1, recipeService.count() );
        Assertions.assertNull( recipeService.findByName( "Mocha" ) );
        // check that coffee was not deleted
        assertEquals( r1, recipeService.findByName( "Coffee" ) );
    }

    @Test
    @Transactional
    public void testDeleteAllRecipes () {
        // check that there are no recipes in the database
        assertEquals( 0, recipeService.count() );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        r1.setIngredient( new Ingredient( "COFFEE", 3 ) );
        r1.setIngredient( new Ingredient( "MILK", 1 ) );
        r1.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r1.setIngredient( new Ingredient( "CHOCOLATE", 0 ) );
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 50 );
        r2.setIngredient( new Ingredient( "COFFEE", 3 ) );
        r2.setIngredient( new Ingredient( "MILK", 1 ) );
        r2.setIngredient( new Ingredient( "SUGAR", 1 ) );
        r2.setIngredient( new Ingredient( "CHOCOLATE", 2 ) );
        recipeService.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 60 );
        r3.setIngredient( new Ingredient( "COFFEE", 3 ) );
        r3.setIngredient( new Ingredient( "MILK", 2 ) );
        r3.setIngredient( new Ingredient( "SUGAR", 2 ) );
        r3.setIngredient( new Ingredient( "CHOCOLATE", 0 ) );
        recipeService.save( r3 );

        final List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 3, dbRecipes.size() );
        assertEquals( r1, recipeService.findByName( "Coffee" ) );
        assertEquals( r2, recipeService.findByName( "Mocha" ) );
        assertEquals( r3, recipeService.findByName( "Latte" ) );

        // delete all
        recipeService.deleteAll();
        // check that they were all deleted
        assertEquals( 0, recipeService.count() );
        Assertions.assertNull( recipeService.findByName( "Coffee" ) );
        Assertions.assertNull( recipeService.findByName( "Mocha" ) );
        Assertions.assertNull( recipeService.findByName( "Latte" ) );
    }
}
