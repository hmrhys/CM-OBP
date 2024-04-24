package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredient for the coffee maker. Ingredient is tied to the database using
 * Hibernate libraries. See IngredientRepository and Ingredient for the other
 * two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Ingredient extends DomainObject {

    /**
     * Ingredient id
     */
    @Id
    @GeneratedValue
    private Long    id;

    // /** IngredientType */
    // @Enumerated ( EnumType.STRING )
    // private IngredientType ingredient;

    /** Ingredient name */
    private String  ingredient;

    /** amount */
    @Min ( 0 )
    private Integer amount;

    /**
     * Creates an Ingredient with the given string as the ingredient name and
     * amount as the amount
     *
     * @param ingredient
     *            the ingredient type
     * @param amount
     *            the amount of the ingredient
     */
    public Ingredient ( final String ingredient, final Integer amount ) {
        setIngredient( ingredient );
        setAmount( amount );
    }

    /**
     * Creates a default ingredient for the coffee maker.
     */
    public Ingredient () {
        // leave empty for Hibernate
    }

    /**
     * Gets the ingredient name
     *
     * @return the ingredient
     */
    public String getIngredient () {
        return ingredient;
    }

    /**
     * Sets the ingredient name
     *
     * @param ingredient
     *            the ingredient to set
     */
    public void setIngredient ( final String ingredient ) {
        this.ingredient = ingredient;
    }

    /**
     * Gets the ingredient amount
     *
     * @return the amount
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets the ingredient amount
     *
     * @param amount
     *            the amount to set
     */
    public void setAmount ( final Integer amount ) {
        this.amount = amount;
        // got rid of throws IAE for testing
    }

    // @Override
    // public Serializable getId () {
    // // TODO Auto-generated method stub
    // return id;
    // }

    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the ingredient's id
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    @Override
    public String toString () {
        // removed id field to match output from M4 - Saving, saving, and more
        // saving section
        return "Ingredient [ingredient=" + ingredient + ", amount=" + amount + "]";
    }

}
