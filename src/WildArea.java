/**
 * WildArea.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Wild Area object class
 */

public class WildArea extends Entity {

    // Private variables for the wild area
    private DoubleLinkedList<String> pokemon;
    private int maxLvl;
    private int minLvl;
    private static final int APPEAR_RATE = 1;

    /**
     * Wild area constructor
     * @param game the game instance
     * @param x the x coordinate of the wild area
     * @param y the y coordinate of the wild area
     * @param width the width of box
     * @param height the height of box
     * @param minLvl the minimum level
     * @param maxLvl the maximum level
     * @param pokemon the pokemon that can spawn
     */
    public WildArea(Game game, int x, int y, int width, int height, int minLvl, int maxLvl, String pokemon) {
        // Super for the entity class
        super(game, x, y, width, height);
        // Set all the private variables
        this.maxLvl = maxLvl;
        this.minLvl = minLvl;
        this.pokemon = new DoubleLinkedList<>();
        // Split up the pokemon into a list of pokemon.
        setPokemon(pokemon);
    }

    /**
     * Method to get the max spawn level of the pokemon.
     * @return the max spawn level
     */
    public int getMaxLvl() {
        return maxLvl;
    }

    /**
     * Method to get the minimum spawn level of the pokemon
     * @return the minimum level
     */
    public int getMinLvl() {
        return minLvl;
    }

    /**
     * Method to get the pokemon that can spawn
     * @return the double linked list of pokemon names
     */
    public DoubleLinkedList<String> getPokemon() {
        return pokemon;
    }

    /**
     * Method to split up a string into a double linked list
     * @param pokemon the string of pokemon
     */
    public void setPokemon(String pokemon) {
        // Split the string of pokemon
        for (String p : pokemon.split(",")) {
            // Add each pokemon name to the double linked list of names.
            this.pokemon.add(p);
        }
    }

    /**
     * Method to determine if the player should encounter pokemon or not.
     * @return true if an encounter should occur, false if not.
     */
    public static boolean doesEncounter() {
        return PokemonDawn.generateRandom(100, 0) <= APPEAR_RATE;
    }

}
