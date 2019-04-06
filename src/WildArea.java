public class WildArea extends Entity {

    private DoubleLinkedList<String> pokemon;
    private int maxLvl;
    private int minLvl;
    private static final int APPEAR_RATE = 1;

    public WildArea(Game game, int x, int y, int width, int height, int minLvl, int maxLvl, String pokemon) {
        super(game, x, y, width, height);
        this.maxLvl = maxLvl;
        this.minLvl = minLvl;
        this.pokemon = new DoubleLinkedList<>();
        setPokemon(pokemon);
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public int getMinLvl() {
        return minLvl;
    }

    public DoubleLinkedList<String> getPokemon() {
        return pokemon;
    }

    public void setPokemon(String pokemon) {
        for (String p : pokemon.split(",")) {
            this.pokemon.add(p);
        }
    }

    public static boolean doesEncounter() {
        return PokemonDawn.generateRandom(100, 0) <= APPEAR_RATE;
    }

}
