/**
 * Type.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * type enumeration
 */

//enum type
public enum Type {
    
    //pokemon types
    PSYCHIC,
        DARK,
        FIGHTING,
        GROUND,
        FLYING,
        ELECTRIC,
        NORMAL,
        BUG,
        ICE,
        WATER,
        FIRE,
        ROCK,
        POISON,
        GHOST,
        DRAGON,
        GRASS,
        STEEL;
    
    //create lists
    private DoubleLinkedList<Type> superEffective;
    private DoubleLinkedList<Type> noEffect;
    
    /*getSuperEffective method
     * @return superEffective, the list of effective types against your pokemon
     */
    public DoubleLinkedList<Type> getSuperEffective(){
        return this.superEffective;
    }
    
    /*getNoEffect method
     * @return noEffect, the list of not effect types against your pokemon
     */
    public DoubleLinkedList<Type> getNoEffect(){
        return this.noEffect;
    }
    
    /*setSuperEffective method
     * @param list, the list of effective types to use to set list
     */
    public void setSuperEffective(DoubleLinkedList<Type> list){
        this.superEffective = list;
    }
    
    /*setNoEffect method
     * @param list, the list of effect types to use to set list
     */
    public void setNoEffect(DoubleLinkedList<Type> list){
        this.noEffect = list;
    }
    
    /*getType method
     * @param type, the type of your pokemon
     */
    public static Type getType(String type){
        return valueOf(type);
    }
    
}
