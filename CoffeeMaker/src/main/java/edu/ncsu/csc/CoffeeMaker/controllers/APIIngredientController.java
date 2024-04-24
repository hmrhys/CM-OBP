package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Ingredients.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService service;

    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific ingredient, as
     * indicated by the path variable provided (the name of the ingredient
     * desired)
     *
     * @param name
     *            ingredient name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable ( "name" ) final String name ) {
        final Ingredient ingredient = service.findByName( name );
        return null == ingredient
                ? new ResponseEntity( errorResponse( "No ingredient found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( ingredient, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Ingredient model. This is
     * used to create a new Ingredient by automatically converting the JSON
     * RequestBody provided to an Ingredient object. Invalid JSON will fail.
     *
     * @param ingredient
     *            The valid Ingredient to be saved.
     * @return ResponseEntity indicating success if the Ingredient could be
     *         saved to the system, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
        if ( null != service.findByName( ingredient.getIngredient() ) ) {
            return new ResponseEntity(
                    errorResponse( "Ingredient with the name " + ingredient.getIngredient() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        // check valid ingredient (positive amount)
        if ( ingredient.getAmount() >= 0 ) {
            service.save( ingredient );
            return new ResponseEntity( successResponse( ingredient.getIngredient() + " successfully created" ),
                    HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse(
                    "Ingredient with name " + ingredient.getIngredient() + " has invalid amount (must be positive)" ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * REST API method to allow deleting an Ingredient from the CoffeeMaker's
     * system, by making a DELETE request to the API endpoint and indicating the
     * ingredient to delete (as a path variable)
     *
     * @param name
     *            The name of the Ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the
     *         ingredient does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
        final Ingredient ingredient = service.findByName( name );
        if ( null == ingredient ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( ingredient );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to an Ingredient. This will
     * update an Ingredient in CoffeeMaker with a new amount given.
     *
     * @param name
     *            The name of the Ingredient to update
     * @param ingredient
     *            The Ingredient to update with
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity updateIngredient ( @PathVariable final String name,
            @RequestBody final Ingredient ingredient ) {
        final Ingredient i = service.findByName( name );

        // ingredient does not exist
        if ( null == i ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
        }

        // update
        i.setAmount( ingredient.getAmount() );

        if ( i.getAmount() >= 0 ) {
            service.save( i );
            return new ResponseEntity( successResponse( i.getIngredient() + " successfully updated" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity(
                    errorResponse(
                            "Ingredient with name " + i.getIngredient() + " has invalid amount (must be positive)" ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
