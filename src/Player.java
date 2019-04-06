import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Player extends Entity {

    private boolean visible;
    private boolean isMoving;
    private boolean canMove;
    private static final int MOVE_SPEED = 4;
    private BufferedImage sprite;
    private String image;
    private boolean inDialog;
    private Game game;
    private Timer animator;
    private int xVel;
    private int yVel;
    private int money;

    private DoubleLinkedList<Pokemon> party;

    public Player(Game game, int x, int y, String images, int width, int height) {
        super(game, x, y, width, height);
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
        setImage(image);

        // Animate the players movement
        animator = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Animate...
                if (isMoving) {
                    if (image.contains("-1")) {
                        image = image.substring(0, image.indexOf("-")) + "-2";
                    } else if (image.contains("-2")) {
                        image = image.substring(0, image.indexOf("-")) + "-1";
                    } else {
                        image += "-1";
                    }

                    setImage(image);
                }
            }
        });
        animator.start();
    }

    public void tick() {

        // Check collision...
        Map map = getGame().getMapPanel().getMap();
        if (map != null) {

            // Check NPC collision
            for (int i = 0; i < map.getPeople().size(); i++) {
                NPC npc = map.getPeople().get(i);
                if (this.getBounds(xVel, yVel).intersects(npc.getBounds(0, 0))) {
                    return;
                }
            }
            // Check for warps
            for (int i = 0; i < map.getWarps().size(); i++) {
                Warp warp = map.getWarps().get(i);
                if (this.image.contains(warp.getDirection()) || warp.getDirection().equalsIgnoreCase("all")) {
                    if (getBounds(xVel, yVel).intersects(warp.getBounds(0, 0))) {
                        warp.warp(this);
                    }
                }
            }
            // Check for walls.
            for (int i = 0; i < map.getWalls().size(); i++) {
                Entity wall = map.getWalls().get(i);
                if (getBounds(xVel, yVel).intersects(wall.getBounds(0, 0))) {
                    return;
                }
            }

            // Check for grass
            for(int i = 0; i < map.getWildAreas().size(); i++){
                WildArea wa = map.getWildAreas().get(i);
                if(getBounds(0, 0).intersects(wa.getBounds(0, 0))){
                    // Player is in grass.
                    if(isMoving() && wa.doesEncounter() && canMove() && getParty().size() != 0){
                        // Player encounters a pokemon!
                        // Create a random opponent.
                        String randomPokemon = wa.getPokemon()
                                .get(PokemonDawn.generateRandom(wa.getPokemon().size(), 0));
                        String[] stats = PokemonDawn.getPokemonStats(randomPokemon);
                        if(stats != null){
                        int health = Integer.parseInt(stats[0]);
                        int attack = Integer.parseInt(stats[1]);
                        int speed = Integer.parseInt(stats[2]);
                        Type type = Type.valueOf(stats[3]);

                        // Get a random level
                        int level = PokemonDawn.generateRandom(wa.getMaxLvl()+1, wa.getMinLvl());
                        game.setContent(new Battle(game, new Pokemon(randomPokemon, level, 0, health, attack, speed, type)));
                        this.setCanMove(false);
                        }
                    }
                }
            }

        }

        setX(getX() + xVel);
        setY(getY() + yVel);
    }

    public void setDirection(String file) {
        setImage(file);
    }

    public String getDirection() {
        if (this.image.contains("-")) {
            return image.substring(0, image.indexOf("-"));
        } else {
            return image;
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean bool) {
        this.setxVelocity(0);
        this.setyVelocity(0);
        this.canMove = bool;
    }

    public boolean isVisible() {
        return visible;
    }

    public Game getGame() {
        return this.game;
    }

    public BufferedImage getSprite() {
        return this.sprite;
    }

    public DoubleLinkedList<Pokemon> getParty() {
        return party;
    }

    public String getImage() {
        return this.image;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        if (!moving) {
            if (image.contains("-")) {
                setImage(image.substring(0, image.indexOf("-")));
            }
        }
        this.isMoving = moving;
    }

    public void setxVelocity(int vel) {
        this.xVel = vel;
    }

    public void setyVelocity(int vel) {
        this.yVel = vel;
    }

    public int getxVelocity() {
        return xVel;
    }

    public int getyVelocity() {
        return yVel;
    }

    public void setImage(String image) {
        this.image = image;
        try {
            sprite = ImageIO.read(new File("player" + File.separator + image
                    + ".png"));
        } catch (IOException e) {
        }
    }

    public static int getMoveSpeed() {
        return MOVE_SPEED;
    }

    public boolean isInDialog() {
        return inDialog;
    }

    public void setDialog(boolean dialog) {
        this.inDialog = dialog;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int amount){
        this.money = amount;
    }

    public void healParty(){
        for(int i = 0; i < getParty().size(); i++){
            getParty().get(i).setHealth(getParty().get(i).getMaxHealth());
        }
    }

    public void loadParty(){
     
     if(PokemonDawn.getValue("Party0") == null){
      int random = PokemonDawn.generateRandom(4, 1);
      String name;
      if(random == 1){
       name = "Pikachu";
      }else if(random == 2){
       name = "Deoxys";
      }else{
       name = "Rayquaza";
      }
      String[] stats = PokemonDawn.getPokemonStats(name);
      int initialHealth = Integer.parseInt(stats[0]);
            int attack = Integer.parseInt(stats[1]);
            int speed = Integer.parseInt(stats[2]);
            Type type = Type.valueOf(stats[3]);
            Pokemon starter = new Pokemon(name, 5, 0, initialHealth, attack, speed, type);
            getParty().add(starter);
      return;
     }
     
        for(int i = 0; i < 6; i++){
         // Get the pokemon at the index.
         String pokeData = PokemonDawn.getValue("Party" + i);
         if(pokeData != null){
          // Get the level of the pokemon.
          String name = pokeData.substring(0, pokeData.indexOf("~"));
          pokeData = pokeData.substring(pokeData.indexOf("~")+1);
          int level = Integer.parseInt(pokeData.substring(0, pokeData.indexOf("~")));
          pokeData = pokeData.substring(pokeData.indexOf("~")+1);
          int exp = Integer.parseInt(pokeData.substring(0, pokeData.indexOf("~")));
          pokeData = pokeData.substring(pokeData.indexOf("~")+1);
          int health = Integer.parseInt(pokeData);
          String[] stats = PokemonDawn.getPokemonStats(name);
          int initialHealth = Integer.parseInt(stats[0]);
                int attack = Integer.parseInt(stats[1]);
                int speed = Integer.parseInt(stats[2]);
                Type type = Type.valueOf(stats[3]);
          Pokemon pokemon = new Pokemon(name, level, exp, initialHealth, attack, speed, type);
          pokemon.setHealth(health);
          getParty().add(pokemon);
         }
        }
    }

}
