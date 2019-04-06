import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.io.File;
import javax.sound.sampled.*;

public class PokemonDawn {

 public static Game game;
 public static String[][] data = null;
 public static String[][] pokemonData = null;

 public static void initializeTypes() {

  DoubleLinkedList<Type> superEffective = new DoubleLinkedList<>();
  DoubleLinkedList<Type> noEffect = new DoubleLinkedList<>();

  // Loop through all the types
  for (Type type : Type.values()) {
   superEffective.clear();
   noEffect.clear();
   switch (type) {
   case ELECTRIC:
    superEffective.add(Type.WATER);
    superEffective.add(Type.FLYING);
    superEffective.add(Type.ROCK);
    noEffect.add(Type.GROUND);
    break;
   case NORMAL:
    noEffect.add(Type.GHOST);
    break;
   case FIRE:
    superEffective.add(Type.GRASS);
    superEffective.add(Type.ICE);
    superEffective.add(Type.BUG);
    superEffective.add(Type.STEEL);
    break;
   case WATER:
    superEffective.add(Type.FIRE);
    superEffective.add(Type.GROUND);
    superEffective.add(Type.ROCK);
    break;
   case GRASS:
    superEffective.add(Type.WATER);
    superEffective.add(Type.GROUND);
    superEffective.add(Type.ROCK);
    break;
   case ICE:
    superEffective.add(Type.GRASS);
    superEffective.add(Type.GROUND);
    superEffective.add(Type.FLYING);
    superEffective.add(Type.DRAGON);
    break;
   case FIGHTING:
    superEffective.add(Type.NORMAL);
    superEffective.add(Type.ICE);
    superEffective.add(Type.ROCK);
    superEffective.add(Type.DARK);
    superEffective.add(Type.STEEL);
    noEffect.add(Type.GHOST);
    break;
   case POISON:
    superEffective.add(Type.GRASS);
    noEffect.add(Type.STEEL);
    break;
   case GROUND:
    superEffective.add(Type.FIRE);
    superEffective.add(Type.ELECTRIC);
    superEffective.add(Type.POISON);
    superEffective.add(Type.ROCK);
    superEffective.add(Type.STEEL);
    noEffect.add(Type.FLYING);
    break;
   case FLYING:
    superEffective.add(Type.GRASS);
    superEffective.add(Type.FIGHTING);
    superEffective.add(Type.BUG);
    break;
   case PSYCHIC:
    superEffective.add(Type.FIGHTING);
    superEffective.add(Type.POISON);
    noEffect.add(Type.DARK);
    break;
   case BUG:
    superEffective.add(Type.GRASS);
    superEffective.add(Type.PSYCHIC);
    superEffective.add(Type.DARK);
    break;
   case ROCK:
    superEffective.add(Type.FIRE);
    superEffective.add(Type.ICE);
    superEffective.add(Type.FLYING);
    superEffective.add(Type.BUG);
    break;
   case GHOST:
    superEffective.add(Type.PSYCHIC);
    superEffective.add(Type.GHOST);
    noEffect.add(Type.NORMAL);
    break;
   case DRAGON:
    superEffective.add(Type.DRAGON);
    break;
   case DARK:
    superEffective.add(Type.PSYCHIC);
    superEffective.add(Type.GHOST);
    break;
   case STEEL:
    superEffective.add(Type.ICE);
    superEffective.add(Type.ROCK);
    break;
   }
   type.setSuperEffective(superEffective);
   type.setNoEffect(noEffect);
  }
 }

 public static void saveData(Game game) {

  if (game.getMapPanel().getMap() == null) {
   return;
  }

  File dataFile = new File("save" + File.separator + "data.txt");
  if (dataFile.exists()) {
   dataFile.delete();
  }

  /* Print Player Settings & Location */
  PrintWriter out = null;
  try {
   dataFile.createNewFile();
   out = new PrintWriter(new FileWriter(dataFile));
  } catch (Exception e) {
  }
  out.println("Location:" + game.getMapPanel().getMap().getName() + "~"
    + game.getPlayer().getX() + "~" + game.getPlayer().getY());
  // Save the player's party.
  for (int i = 0; i < game.getPlayer().getParty().size(); i++) {
   Pokemon pokemon = game.getPlayer().getParty().get(i);
   out.println("Party" + i + ":" + pokemon.getName() + "~"
     + pokemon.getLevel() + "~" + pokemon.getExp() + "~" + pokemon.getHealth());
  }
  out.println("TextSpeed:" + game.getOptions().getTextSpeed());
  out.println("AutoSave:" + game.getOptions().getAutoSave());
  out.println("Hitboxes:" + game.getOptions().getHitboxes());
  out.println("Money:" + game.getPlayer().getMoney());
  // print bag information
  out.println("Pokeball:"
    + Bag.balls[0].substring(Bag.balls[0].indexOf("x") + 1));
  out.close();
  /* -------------------------------- */
 }

 public static void loadData(File dataFile) {
  if (!dataFile.exists()) {
   JOptionPane
     .showMessageDialog(
       null,
       "Error: Unable to load data.\n Please delete the save directory, and try again.");
   System.exit(0);
   return;
  }
  int dataCount = 0;
  Scanner file = null;
  try {
   file = new Scanner(dataFile);
  } catch (FileNotFoundException e) {
  }

  while (file.hasNext()) {
   String line = file.nextLine();
   if (!line.isEmpty()) {
    dataCount++;
   }
  }

  data = new String[dataCount][2];

  try {
   file = new Scanner(dataFile);
  } catch (FileNotFoundException e) {
  }

  int index = 0;
  while (file.hasNext()) {
   String line = file.nextLine();
   if (!line.isEmpty()) {
    String key = line.substring(0, line.indexOf(":"));
    String value = line.substring(line.indexOf(":") + 1);
    data[index][0] = key;
    data[index][1] = value;
    index++;
   }
  }
  file.close();
 }

 public static String getValue(String key) {
  // Loop through all the data.
  if (data != null) {
   // Loop through data array
   for (int row = 0; row < data.length; row++) {
    if (data[row][0] != null && data[row][0].equalsIgnoreCase(key)) {
     return data[row][1];
    }
   }
  }
  return null;
 }

 public static void loadPokemonData(File dataFile) {
  if (!dataFile.exists()) {
   JOptionPane.showMessageDialog(null,
     "Error: Unable to load Pokemon values!");
   System.exit(0);
   return;
  }
  int dataCount = 0;
  Scanner file = null;
  try {
   file = new Scanner(dataFile);
  } catch (FileNotFoundException e) {
  }

  while (file.hasNext()) {
   String line = file.nextLine();
   if (!line.isEmpty()) {
    if(line.indexOf("~") != -1){
    dataCount++;
    }
   }
  }

  pokemonData = new String[dataCount][5];

  try {
   file = new Scanner(dataFile);
  } catch (FileNotFoundException e) {
  }

  int index = 0;
  while (file.hasNext()) {
   String line = file.nextLine();
   if (!line.isEmpty()) {
    if (line.indexOf("~") != -1) {
     String name = line.substring(0, line.indexOf("~"));
     line = line.substring(line.indexOf("~") + 1);
     String health = line.substring(0, line.indexOf("~"));
     line = line.substring(line.indexOf("~") + 1);
     String attack = line.substring(0, line.indexOf("~"));
     line = line.substring(line.indexOf("~") + 1);
     String speed = line.substring(0, line.indexOf("~"));
     line = line.substring(line.indexOf("~") + 1);
     String type = line.substring(0);

     pokemonData[index][0] = name;
     pokemonData[index][1] = health;
     pokemonData[index][2] = attack;
     pokemonData[index][3] = speed;
     pokemonData[index][4] = type;

     index++;
    }
   }
  }
  file.close();
 }

 public static int generateRandom(int max, int min) {
  return (int) (Math.random() * (max - min)) + min;
 }

 public static String[] getPokemonStats(String pokemon) {
  String[] stats = new String[4];

  // Loop through the data array
  for (int row = 0; row < pokemonData.length; row++) {
   // Loop through columns
   if (pokemonData[row][0].equalsIgnoreCase(pokemon)) {
    for (int col = 1; col < pokemonData[row].length; col++) {
     stats[col-1] = pokemonData[row][col];
    }
   }
  }

  // If no stats were found for the pokemon, return null.
  if (stats[0] == "") {
   return null;
  } else {
   // Stats were found for the pokemon, return the pokemon's stats.
   return stats;
  }
 }

 public static void main(String[] args) {
  // Load all the types.
  initializeTypes();
  // Create all the sounds
     Sounds.init();

  // Create a data file.
  File dataFile = new File("save");
  if (dataFile.exists()) {
   // Load all the data from the file.
   loadData(new File("save" + File.separator + "data.txt"));
  } else {
   dataFile.mkdir();
   try {
    new File("save" + File.separator + "data.txt").createNewFile();
   } catch (IOException e) {
   }
  }

  // Load all the pokemon
  File pokemonFile = new File("pokemon.txt");
  loadPokemonData(pokemonFile);

  // Start the game
  game = new Game();
 }

}
