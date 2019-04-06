/**
 * Player.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Player object class
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Player extends Entity {

    // Variables relating to the player.
    private boolean visible;
    private boolean isMoving;
    private boolean canMove;
    // The global move speed of the player.
    private static final int MOVE_SPEED = 4;
    private BufferedImage sprite;
    private String image;
    private boolean inDialog;
    private Game game;
    private Timer animator;
    private int xVel;
    private int yVel;
    private int money;

    // The players current party of pokemon.
    private DoubleLinkedList<Pokemon> party;

    /**
     * Constructor for the player object.
     * @param game the game instance
     * @param x the x position of the player
     * @param y the y position of the player
     * @param images the direction image that will be shown
     * @param width the width of the player sprite
     * @param height the height of the player sprite
     */
    public Player(Game game, int x, int y, String images, int width, int height) {
        // Call the entity super class.
        super(game, x, y, width, height);
        // Initialize all private variables
        this.game = game;
        this.xVel = 0;
        this.yVel = 0;
        this.isMoving = false;
        this.inDialog = false;
        this.canMove = true;
        this.image = images;
        this.visible = true;
        this.party = new DoubleLinkedList<>();
        this.money = 500;
        // Display the player sprite with proper direction.
        setImage(image);

        // Timer to control animating the players movement.
        animator = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make sure that the player is currently moving
                if (isMoving) {
                    // Alternate between different image files to animate the player sprite.
                    if (image.contains("-1")) {
                        image = image.substring(0, image.indexOf("-")) + "-2";
                    } else if (image.contains("-2")) {
                        image = image.substring(0, image.indexOf("-")) + "-1";
                    } else {
                        image += "-1";
                    }
                    // Set the player sprite to the new image
                    setImage(image);
                }
            }
        });
        // Start the timer/loop to animate the players movement.
        animator.start();
    }

    /**
     * Tick method for the player. Called from the game loop.
     * Handles collision, movement, warping.
     */
    public void tick() {
        // Get the current map of the game.
        Map map = getGame().getMapPanel().getMap();
        // Make sure the map is not null.
        if (map != null) {
            // Loop through all the NPCs in the map
            for (int i = 0; i < map.getPeople().size(); i++) {
                // Get the npc at the index
                NPC npc = map.getPeople().get(i);
                // Check if the player intersects the NPC
                if (this.getBounds(xVel, yVel).intersects(npc.getBounds(0, 0))) {
                    // Return to stop movement.
                    return;
                }
            }
            // Loop through all the warps on the map.
            for (int i = 0; i < map.getWarps().size(); i++) {
                // Get the warp at the index
                Warp warp = map.getWarps().get(i);
                // Check if the player is facing the warps direction
                if (this.image.contains(warp.getDirection()) || warp.getDirection().equalsIgnoreCase("all")) {
                    // Check if the player is interacting with the warp.
                    if (getBounds(xVel, yVel).intersects(warp.getBounds(0, 0))) {
                        // Warp the player to the new map.
                        warp.warp(this);
                    }
                }
            }
            // Loop through all the walls in the map
            for (int i = 0; i < map.getWalls().size(); i++) {
                // Get the wall at the index
                Entity wall = map.getWalls().get(i);
                // Check if the player would intersect the wall when moving
                if (getBounds(xVel, yVel).intersects(wall.getBounds(0, 0))) {
                    // Return to avoid moving the player
                    return;
                }
            }

            // Loop through all the wild areas in the map
            for (int i = 0; i < map.getWildAreas().size(); i++) {
                // Get the wild area at the index
                WildArea wa = map.getWildAreas().get(i);
                // Check if the player is on the grass
                if (getBounds(0, 0).intersects(wa.getBounds(0, 0))) {
                    // Check if the player is moving, and if the player should encounter a pokemon.
                    if (isMoving() && wa.doesEncounter() && canMove() && getParty().size() != 0) {
                        // Get a random pokemon from the possible spawns in that wild area
                        String randomPokemon = wa.getPokemon()
                                .get(PokemonDawn.generateRandom(wa.getPokemon().size(), 0));
                        // Create an array to hold the stats for that pokemon.
                        String[] stats = PokemonDawn.getPokemonStats(randomPokemon);
                        // Make sure the stats are not null
                        if (stats != null) {
                            // Get the health of the pokemon
                            int health = Integer.parseInt(stats[0]);
                            // Get the attack of the pokemon
                            int attack = Integer.parseInt(stats[1]);
                            // get the speed of the pokemon
                            int speed = Integer.parseInt(stats[2]);
                            // get the type of the pokemon
                            Type type = Type.valueOf(stats[3]);
                            // get a random level from the wild area's range of levels
                            int level = PokemonDawn.generateRandom(wa.getMaxLvl() + 1, wa.getMinLvl());
                            // Display a battle between the pokemon
                            game.setContent(new Battle(game, new Pokemon(randomPokemon, level, 0, health, attack, speed, type)));
                            // Disable player movement during battle.
                            this.setCanMove(false);
                        }
                    }
                }
            }

        }

        // Set the x position of the player
        setX(getX() + xVel);
        // Set the y position of the player
        setY(getY() + yVel);
    }

    /**
     * Method to set the direction of the player.
     * @param file the file name of the direction that the player will be set to.
     */
    public void setDirection(String file) {
        setImage(file);
    }

    /**
     * Method to get the direction of the player
     * @return
     */
    public String getDirection() {
        // Return just the file name of the direction, without animation numbers.
        if (this.image.contains("-")) {
            return image.substring(0, image.indexOf("-"));
        } else {
            return image;
        }
    }

    /**
     * Method to check if the player can move.
     * @return true if the player can move, false if he can't
     */
    public boolean canMove() {
        return canMove;
    }

    /**
     * Method to set if the player can move
     * @param bool the value that the canMove variable will be set to.
     */
    public void setCanMove(boolean bool) {
        this.setxVelocity(0);
        this.setyVelocity(0);
        this.canMove = bool;
    }

    /**
     * Method to check if the player is visible.
     * @return a boolean of whether or not the player is visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Method to get the game instance
     * @return the game instance
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Method to get the player sprite image
     * @return the image object containing player sprite
     */
    public BufferedImage getSprite() {
        return this.sprite;
    }

    /**
     * Method to get the player's party of pokemon.
     * @return a double linked list of all the player's pokemon in the party.
     */
    public DoubleLinkedList<Pokemon> getParty() {
        return party;
    }

    /**
     * Method to get the image string of the player
     * @return the image string
     */
    public String getImage() {return this.image;}

    /**
     * Method to check if the player is currently moving
     * @return true if player is moving, false if not
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * Method to set if the player is currently moving.
     * @param moving whether or not the player is moving
     */
    public void setMoving(boolean moving) {
        // if the player is not moving...
        if (!moving) {
            // if the image contains an animation number
            if (image.contains("-")) {
                // remove the animation number to set player to still image.
                setImage(image.substring(0, image.indexOf("-")));
            }
        }
        // set whether or not the player is moving.
        this.isMoving = moving;
    }

    /**
     * Method to set the x velocity of the player
     * @param vel the x velocity
     */
    public void setxVelocity(int vel) {
        this.xVel = vel;
    }

    /**
     * Method to set the y velocity of the player
     * @param vel the y velocity
     */
    public void setyVelocity(int vel) {
        this.yVel = vel;
    }

    /**
     * Method to set the current image of the player
     * @param image the path to the image
     */
    public void setImage(String image) {
        // Save the current path to the image
        this.image = image;
        try {
            // Get the image from the files
            sprite = ImageIO.read(new File("player" + File.separator + image
                    + ".png"));
        } catch (IOException e) {
        }
    }

    /**
     * Method to get the move speed of the player
     * @return the move speed of the player
     */
    public static int getMoveSpeed() {
        return MOVE_SPEED;
    }

    /**
     * Method to check if the player is currently in dialog
     * @return true if in dialog, false if not in dialog
     */
    public boolean isInDialog() {
        return inDialog;
    }

    /**
     * Method to set if the player is in dialog
     * @param dialog true if is in dialog, false if not.
     */
    public void setDialog(boolean dialog) {
        this.inDialog = dialog;
    }

    /**
     * Method to get the amount of money the player has
     * @return the money player has
     */
    public int getMoney() {return money;}

    /**
     * Method to set the amount of money player has
     * @param amount the amount of money player will have
     */
    public void setMoney(int amount){
        this.money = amount;
    }

    /**
     * Method to heal the player's entire party
     */
    public void healParty(){
        // Loop through the entire party
        for(int i = 0; i < getParty().size(); i++){
            // Get the pokemon at that party index, and set it's health to the max health it has.
            getParty().get(i).setHealth(getParty().get(i).getMaxHealth());
        }
    }

    /**
     * Method to load the players party from either a file, or from random if at the beginning of the game.
     */
    public void loadParty() {
        // Check if there is a saved party in the data file.
        if (PokemonDawn.getValue("Party0") == null) {
            // If there is no saved data file, pick a random number between 1-3.
            int random = PokemonDawn.generateRandom(4, 1);
            // Create a variable to hold the name of the random pokemon
            String name;
            // Determine the name of the random pokemon
            if (random == 1) {
                // the random pokemon is pikachu
                name = "Pikachu";
            } else if (random == 2) {
                // the random pokemon is deoxys
                name = "Deoxys";
            } else {
                // the random pokemon is rayquaza
                name = "Rayquaza";
            }
            // Create a string array to hold the stats for the pokemon.
            String[] stats = PokemonDawn.getPokemonStats(name);
            // Get the initial health of the pokemon
            int initialHealth = Integer.parseInt(stats[0]);
            // Get the attack of the pokemon
            int attack = Integer.parseInt(stats[1]);
            // Get the speed of the pokemon
            int speed = Integer.parseInt(stats[2]);
            // Get the type of the pokemon
            Type type = Type.valueOf(stats[3]);
            // Create the pokemon variable for the starter pokemon.
            Pokemon starter = new Pokemon(name, 5, 0, initialHealth, attack, speed, type);
            // Add the pokemon to the players party.
            getParty().add(starter);
            // return to avoid loading non-existent pokemon.
            return;
        }
        // Loop through the 6 possible party pokemon.
        for (int i = 0; i < 6; i++) {
            // Get the pokemon at the index.
            String pokeData = PokemonDawn.getValue("Party" + i);
            // if the pokemon exists at that party index, add it to the party.
            if (pokeData != null) {
                // Get the name of the pokemon.
                String name = pokeData.substring(0, pokeData.indexOf("~"));
                // Substring the text to get more data
                pokeData = pokeData.substring(pokeData.indexOf("~") + 1);
                // Get the level of the pokemon
                int level = Integer.parseInt(pokeData.substring(0, pokeData.indexOf("~")));
                // Substring the text to get more data
                pokeData = pokeData.substring(pokeData.indexOf("~") + 1);
                // Get the current experience of the pokemon
                int exp = Integer.parseInt(pokeData.substring(0, pokeData.indexOf("~")));
                // Substring the text to get more data
                pokeData = pokeData.substring(pokeData.indexOf("~") + 1);
                // get the health of the pokemon
                int health = Integer.parseInt(pokeData);
                // Create an array to hold the stats of the pokemon
                String[] stats = PokemonDawn.getPokemonStats(name);
                // Get initial health of pokemon
                int initialHealth = Integer.parseInt(stats[0]);
                // Get the attack of pokemon
                int attack = Integer.parseInt(stats[1]);
                // Get the speed of the pokemon
                int speed = Integer.parseInt(stats[2]);
                // Get the type of the pokemon
                Type type = Type.valueOf(stats[3]);
                // Create a variable for the pokemon using data values
                Pokemon pokemon = new Pokemon(name, level, exp, initialHealth, attack, speed, type);
                // set the health of the pokemon to the saved health
                pokemon.setHealth(health);
                // add the pokemon to the party
                getParty().add(pokemon);
            }
        }
    }

}
