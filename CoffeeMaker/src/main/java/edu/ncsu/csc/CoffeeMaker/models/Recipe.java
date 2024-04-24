package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long                          id;

    /** Recipe name */
    private String                        name;

    /** Recipe price */
    @Min ( 0 )
    private Integer                       price;

    /** the Ingredients in the recipe */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<@Valid Ingredient> ingredients;

    // ^ need the @Valid to make sure that the amount field of the Ingredient is
    // not < 0

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    // /**
    // * Check if all ingredient fields in the recipe are 0
    // *
    // * @return true if all ingredient fields are 0, otherwise return false
    // */
    // public boolean checkRecipe () {
    // return coffee == 0 && milk == 0 && sugar == 0 && chocolate == 0;
    // }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
        boolean allZero = true;
        for ( final Ingredient i : ingredients ) {
            if ( i.getAmount() != 0 ) {
                allZero = false;
                break;
            }
        }
        return allZero;
    }

    /**
     * Check if all ingredient fields in the recipe are positive integers
     *
     * @param r
     *            the recipe to check
     *
     * @return true if all ingredient fields are positive integers, otherwise
     *         return false
     */
    public boolean checkPositive ( final Recipe r ) {
        boolean positive = true;
        for ( final Ingredient i : r.getIngredients() ) {
            if ( i.getAmount() < 0 ) {
                positive = false;
                break;
            }
        }
        return positive;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Adds the given ingredient to the list of ingredients
     *
     * @param ingredient
     *            the ingredient to add to the ingredients list
     * @throws IllegalArgumentException
     *             if the ingredient is already in the list
     */
    public void addIngredient ( final Ingredient ingredient ) throws IllegalArgumentException {
        final int index = getIndex( ingredient.getIngredient() );
        if ( index != -1 ) {
            // already in the list
            throw new IllegalArgumentException( ingredient.getIngredient() + " already exists" );
        }

        ingredients.add( ingredient );
    }

    //
    // if ( ingredient.getAmount() < 0 ) {
    // throw new IllegalArgumentException( "Ingredient units must be positive
    // integers" );
    // }
    // else {
    // ingredients.add( ingredient );
    // }
    // }

    /**
     * Updates the amount of the given ingredient in the list
     * If the ingredient cannot be found in the list, an
     * IllegalArgumentException is thrown
     *
     * @param ingredient
     *            an ingredient with an amount to update the already existing
     *            ingredient with
     */
    public void setIngredient ( final Ingredient ingredient ) {
        final int index = getIndex( ingredient.getIngredient() );

        if ( index == -1 ) {
            // new ingredient
            ingredients.add( ingredient );
        }
        else {
            ingredients.get( index ).setAmount( ingredient.getAmount() );
        }
    }

    /**
     * Gets the ingredients list
     *
     * @return the ingredients list
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Gets the amount of the ingredient with the given name
     *
     * @param name
     *            the name of the ingredient
     * @return the amount of the given ingredient
     *
     * @throws IllegalArgumentException
     *             if the ingredient does not exist in the recipe
     */
    public Integer getIngredient ( final String name ) throws IllegalArgumentException {
        for ( final Ingredient i : ingredients ) {
            if ( i.getIngredient().equals( name ) ) {
                return i.getAmount();
            }
        }
        throw new IllegalArgumentException( "The ingredient " + name + " does not exist in this recipe" );
    }

    /**
     * Checks the ingredients list for an ingredient with the given name,
     * returns -1 if ingredient with name does not exist
     *
     * @param name
     *            the name of the ingredient
     * @return the ingredient with the given name, -1 if the ingredient cannot
     *         be found
     */
    public int getIndex ( final String name ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            final Ingredient ingredient = ingredients.get( i );
            if ( ingredient.getIngredient().equals( name ) ) {
                // the ingredient has been found
                return i;
            }
        }

        return -1;
    }

    /**
     * Updates the current recipe ingredients to the given recipe's ingredients
     *
     * @param r
     *            the recipe to update to
     * @throws IllegalArgumentException
     *             if the recipe is invalid
     */
    public void updateRecipe ( final Recipe r ) throws IllegalArgumentException {
        if ( !checkPositive( r ) ) {
            throw new IllegalArgumentException( "Invalid recipe" );
        }
        else {
            // clear the list of ingredients in the current recipe
            ingredients.clear();
            // set ingredients to the ingredients of the other recipe
            ingredients.addAll( r.getIngredients() );
            // update the price
            setPrice( r.getPrice() );
        }
    }

    /**
     * Prints out the ingredients in the ingredients list
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuilder str = new StringBuilder();
        str.append( name );
        str.append( " with ingredients [" );
        for ( int i = 0; i < ingredients.size() - 1; i++ ) {
            str.append( ingredients.get( i ).toString() );
            str.append( ", " );
        }
        str.append( ingredients.get( ingredients.size() - 1 ) );
        str.append( "]" );
        return str.toString();
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
