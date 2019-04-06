import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class Pokemon {

    private String name;
    private int level, exp, health, speed, attack, maxHealth;
    private Type type;
    private BufferedImage[] sprites;

    public Pokemon(String name, int level, int exp, int initialHealth, int initialSpeed, int initialAttack, Type type) {
        // Set the key variables of the pokemon.
        this.name = name;
        this.level = level;
        this.exp = exp;
        // Calculate the current stats based on level.
        this.health = initialHealth + 2 * level;
        this.maxHealth = initialHealth + 2 * level;
        this.speed = initialSpeed + 1 * level;
        this.attack = initialAttack + 2 * level;
        this.type = type;
        this.sprites = new BufferedImage[2];
        // Load the first sprite for the pokemon from the sprite website
        try {
            // Create urls
            URL front = new URL("http://www.pokestadium.com/sprites/emerald/" + this.name.toLowerCase() + ".png");
            URL back = new URL("http://www.pokestadium.com/sprites/emerald/back/" + this.name.toLowerCase() + ".png");
            // Front sprite
            this.sprites[0] = ImageIO.read(front);
            // Back sprite
            this.sprites[1] = ImageIO.read(back);
        } catch (Exception e) {
        }
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public int getExp() {
        return this.exp;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int hp) {
        this.health = hp;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getAttack() {
        return this.attack;
    }

    public Type getType() {
        return this.type;
    }

    public boolean levelUp() {
        if (!(getExp() >= expToLevel())) {
            return false;
        }
        // Boost all the stats.
        this.health += 2;
        this.attack += 2;
        this.speed += 1;
        // Increase the level
        this.level += 1;

        int remainingExp = expToLevel() - getExp();
        this.exp = remainingExp;
        return true;
    }

    public int expToLevel() {
        return (level * speed) / 4;
    }

    public BufferedImage[] getSprites() {
        return sprites;
    }

    public boolean willLevelUp() {
        return false;
    }

    public boolean isFainted() {
        return this.health == 0;
    }

    public boolean giveExp(int amt) {
        this.exp += amt;
        return levelUp();
    }

}