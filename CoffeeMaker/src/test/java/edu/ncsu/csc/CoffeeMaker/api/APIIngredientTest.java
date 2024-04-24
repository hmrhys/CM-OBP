package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureIngredient () throws Exception {
        service.deleteAll();

        // COFFEE
        // SUGAR
        // MILK
        // CHOCOLATE
        // PUMPKIN_SPICE
        // ESPRESSO
        // CREAM

        final Ingredient i = new Ingredient();
        i.setIngredient( "COFFEE" );
        i.setAmount( 3 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testIngredientAPI () throws Exception {

        service.deleteAll();

        final Ingredient i = new Ingredient();
        i.setIngredient( "COFFEE" );
        i.setAmount( 3 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) );

        Assertions.assertEquals( 1, (int) service.count() );
    }

    @Test
    @Transactional
    public void testAddIngredient2 () throws Exception {

        /*
         * Tests a ingredient with a duplicate name to make sure it's rejected
         */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
        final Ingredient i1 = createIngredient( "COFFEE", 3 );
        service.save( i1 );

        final Ingredient i2 = createIngredient( "COFFEE", 1 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one ingredient in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteIngredient () throws Exception {
        Assertions.assertEquals( 0, service.count(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = createIngredient( "COFFEE", 3 );
        service.save( i1 );

        Assertions.assertEquals( 1, service.count(), "There should be one ingredient in the database" );

        mvc.perform( delete( String.format( "/api/v1/ingredients/%s", "COFFEE" ) ) ).andDo( print() )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteNAIngredient () throws Exception {
        Assertions.assertEquals( 0, service.count(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = createIngredient( "COFFEE", 3 );
        service.save( i1 );
        final Ingredient i2 = createIngredient( "MILK", 1 );
        // do NOT save this ingredient to the system

        // check that it's not in the system
        Assertions.assertNull( service.findByName( "MILK" ) );

        Assertions.assertEquals( 1, service.count(), "There should be one ingredient in the database" );
        Assertions.assertEquals( i1, service.findByName( "COFFEE" ) );

        mvc.perform( delete( String.format( "/api/v1/ingredients/%s", "MILK" ) ) ).andDo( print() )
                .andExpect( status().is( 404 ) );

        // check that nothing was deleted
        Assertions.assertEquals( 1, service.count(), "There should be one ingredient in the database" );
    }

    @Test
    @Transactional
    public void testDeleteAllIngredients () throws Exception {
        Assertions.assertEquals( 0, service.count(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = createIngredient( "COFFEE", 3 );
        service.save( i1 );
        final Ingredient i2 = createIngredient( "MILK", 2 );
        service.save( i2 );
        final Ingredient i3 = createIngredient( "CHOCOLATE", 2 );
        service.save( i3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three ingredients should result in three ingredients in the database" );

        mvc.perform( delete( String.format( "/api/v1/ingredients/%s", "COFFEE" ) ) ).andDo( print() )
                .andExpect( status().isOk() );
        mvc.perform( delete( String.format( "/api/v1/ingredients/%s", "MILK" ) ) ).andDo( print() )
                .andExpect( status().isOk() );
        mvc.perform( delete( String.format( "/api/v1/ingredients/%s", "CHOCOLATE" ) ) ).andDo( print() )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testUpdateExistingIngredient () throws Exception {
        // Create and save a test ingredient
        final Ingredient testIngredient = createIngredient( "COFFEE", 3 );
        service.save( testIngredient );

        // Update the ingredient
        testIngredient.setAmount( 5 );
        mvc.perform( put( "/api/v1/ingredients/COFFEE" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( testIngredient ) ) ).andExpect( status().isOk() );

        // Retrieve and assert the updated ingredient
        final Ingredient updatedIngredient = service.findByName( "COFFEE" );
        Assertions.assertNotNull( updatedIngredient, "Updated ingredient should not be null" );
        Assertions.assertEquals( 5, updatedIngredient.getAmount(), "The amount should be updated to 5" );
    }

    @Test
    @Transactional
    public void testUpdateNonExistingIngredient () throws Exception {
        // Create an ingredient object, but don't save it to the database
        final Ingredient testIngredient = new Ingredient();
        testIngredient.setIngredient( "Not_Real_Coffee" );
        testIngredient.setAmount( 10 );

        // Attempt to update the non-existing ingredient and should give us 404
        mvc.perform( put( "/api/v1/ingredients/Not_Real_Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( testIngredient ) ) ).andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void testUpdateIngredientWithInvalidAmount () throws Exception {
        // create and save a valid ingredient
        final Ingredient validIngredient = new Ingredient();
        validIngredient.setIngredient( "Fake_Coffee" );
        validIngredient.setAmount( 10 );
        service.save( validIngredient );

        // create an ingredient with the same name but an invalid amount
        final Ingredient invalidUpdate = new Ingredient();
        invalidUpdate.setIngredient( "Fake_Coffee" );
        invalidUpdate.setAmount( -5 );

        // Attempt to update the ingredient with an invalid amount and should
        // give us bad request response
        mvc.perform( put( "/api/v1/ingredients/Fake_Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( invalidUpdate ) ) ).andExpect( status().isBadRequest() );
    }

    private Ingredient createIngredient ( final String name, final Integer amount ) {
        final Ingredient ingredient = new Ingredient();
        ingredient.setIngredient( name );
        ingredient.setAmount( amount );

        return ingredient;
    }

}
