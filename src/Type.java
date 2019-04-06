
public enum Type {
	
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
	
	private DoubleLinkedList<Type> superEffective;
	private DoubleLinkedList<Type> noEffect;

	public DoubleLinkedList<Type> getSuperEffective(){
		return this.superEffective;
	}
	
	public DoubleLinkedList<Type> getNoEffect(){
		return this.noEffect;
	}
	
	
	public void setSuperEffective(DoubleLinkedList<Type> list){
		this.superEffective = list;
	}
	
	public void setNoEffect(DoubleLinkedList<Type> list){
		this.noEffect = list;
	}
	
	public static Type getType(String type){
		return valueOf(type);
	}
	
}
