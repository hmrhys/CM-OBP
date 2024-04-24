package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    // /** HashMap for inventory */
    // private HashMap<String, Ingredient> ingredients;

    /** The Ingredients in the inventory */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     */
    public Inventory ( final Integer coffee, final Integer milk, final Integer sugar, final Integer chocolate ) {
        this();
        setIngredient( "COFFEE", coffee );
        setIngredient( "MILK", milk );
        setIngredient( "SUGAR", sugar );
        setIngredient( "CHOCOLATE", chocolate );
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the current number of ingredient units in the inventory.
     *
     * @param name
     *            the name of the ingredient to get
     * @return amount of ingredient
     *
     * @throws IllegalArgumentException
     *             if the ingredient does not exist in the inventory
     */
    public Integer getIngredient ( final String name ) throws IllegalArgumentException {
        final int index = getIndex( name );

        if ( index != -1 ) {
            return ingredients.get( index ).getAmount();
        }
        else {
            throw new IllegalArgumentException( "Ingredient " + name + " does not exist in the inventory" );
        }
    }

    // /**
    // * Updates the ingredient with the given name to the given amount
    // *
    // * @param name
    // * the name of the ingredient to update
    // * @param amount
    // * the amount to set
    // *
    // * @throws IllegalArgumentException
    // * if the amount < 0
    // */
    // public void setIngredient ( final String name, final Integer amount )
    // throws IllegalArgumentException {
    // final Ingredient i = ingredients.get( name );
    // if ( i != null ) {
    // if ( amount < 0 ) {
    // throw new IllegalArgumentException( "Ingredient units must be positive
    // integers" );
    // }
    // else {
    // i.setAmount( amount );
    // // make sure the map is updated
    // ingredients.put( name, i );
    // }
    // }
    // else {
    // throw new IllegalArgumentException( "Ingredient " + name + " does not
    // exist in the inventory" );
    // }
    // }

    /**
     * Sets the ingredient with the given name to the given amount, if the
     * ingredient does not already exist, adds it to the inventory
     *
     * @param name
     *            the name of the ingredient to update
     * @param amount
     *            the amount to set
     *
     * @throws IllegalArgumentException
     *             if the amount < 0
     */
    public void setIngredient ( final String name, final Integer amount ) throws IllegalArgumentException {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Ingredient units must be positive integers" );
        }

        final int index = getIndex( name );

        if ( index == -1 ) {
            final Ingredient ingredient = new Ingredient( name, amount );
            // not found, add to list
            ingredients.add( ingredient );
        }
        else {
            // found in list, update amount
            ingredients.get( index ).setAmount( amount );
            return;
        }
    }

    /**
     * Gets the list of ingredients
     *
     * @return the ingredients list
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Converts the string amount of ingredient to an Integer
     *
     * @param name
     *            name of ingredient
     * @param amount
     *            amount of ingredient
     * @return checked amount of ingredient
     * @throws IllegalArgumentException
     *             if the given amount isn't a positive integer
     */
    public Integer checkIngredient ( final String name, final String amount ) throws IllegalArgumentException {
        Integer amt = 0;
        try {
            amt = Integer.parseInt( amount );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of " + name + " must be a positive integer" );
        }

        // check positive
        if ( amt < 0 ) {
            throw new IllegalArgumentException( "Units of " + name + " must be a positive integer" );
        }

        return amt;

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
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;

        for ( final Ingredient ingredient : r.getIngredients() ) {
            final Ingredient inventoryIngredient = ingredients.get( getIndex( ingredient.getIngredient() ) );
            if ( inventoryIngredient.getAmount() < ingredient.getAmount() ) {
                isEnough = false;
            }
        }
        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( final Ingredient ingredient : r.getIngredients() ) {
                final String name = ingredient.getIngredient();
                setIngredient( name, getIngredient( name ) - ingredient.getAmount() );
            }
            return true;
        }
        else {
            return false;
        }
    }

    // /**
    // * Creates an ingredient in the inventory
    // *
    // * @param ingredient
    // * the ingredient to create
    // * @return true if successful, false if not
    // * @throws IllegalArgumentException
    // * if ingredient is already in the inventory or it is invalid
    // */
    // public boolean createIngredient ( final Ingredient ingredient ) throws
    // IllegalArgumentException {
    // final String name = ingredient.getIngredient();
    //
    // // check unique
    // if ( ingredients.get( name ) != null ) {
    // throw new IllegalArgumentException( name + " already exists" );
    // }
    //
    // // check amount
    // if ( ingredient.getAmount() < 0 ) {
    // throw new IllegalArgumentException( "Ingredient units must be positive
    // integers" );
    // }
    //
    // ingredients.put( name, ingredient );
    // return true;
    // }

    /**
     * Adds the given amount of the given ingredient to the inventory, if the
     * ingredient does not exist, add it to the inventory
     *
     * @param name
     *            the name of the ingredient
     * @param amount
     *            the amount of the ingredient to add
     * @return true if successful, false if not
     * @throws IllegalArgumentException
     *             if the amount to add is invalid
     */
    public boolean addIngredient ( final String name, final Integer amount ) throws IllegalArgumentException {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Ingredient units must be positive integers" );
        }

        final int index = getIndex( name );
        // check in the system
        if ( index == -1 ) {
            final Ingredient ingredient = new Ingredient( name, amount );
            // not found, add to list
            ingredients.add( ingredient );
            return true;
        }

        final Integer prev = ingredients.get( index ).getAmount();

        ingredients.get( index ).setAmount( prev + amount );
        return true;
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final Integer coffee, final Integer milk, final Integer sugar,
            final Integer chocolate ) {
        if ( coffee < 0 || milk < 0 || sugar < 0 || chocolate < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }

        addIngredient( "COFFEE", coffee );
        addIngredient( "MILK", milk );
        addIngredient( "SUGAR", sugar );
        addIngredient( "CHOCOLATE", chocolate );

        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient i : ingredients ) {
            buf.append( i.getIngredient() );
            buf.append( ": " );
            buf.append( i.getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
