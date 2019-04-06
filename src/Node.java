/**
 * Node.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Node object class.
 */

//node class
public class Node<T> { 
    
    //variables
    private T item;
    private Node<T> next;
    private Node<T> previous;
    
    //constructor
    public Node(T item, Node<T> previous){
        this.item = item;
        this.next = null;
        this.previous = previous;
    }
    
    /*getNext method
     *to get next reference
     * @return next, the reference
     */
    public Node<T> getNext(){
        return this.next;
    }
    
    /*getPrevious method
     *to get previous reference
     * @return previous, the reference
     */
    public Node<T> getPrevious(){
        return this.previous; 
    }
    
    /*setPrevious method
     *to set previous reference
     *@param previous, the reference
     */
    public void setPrevious(Node<T> previous){
        this.previous = previous; 
    }
    
    /*setNext method
     *to set next reference
     * @param next, the reference
     */
    public void setNext(Node<T> next){
        this.next = next;
    }
    
    /*getItem method
     *to get item
     * @return item, the item
     */
    public T getItem(){
        return this.item;
    }
    
    /*setItem method
     *to set item
     * @param item, the item
     */
    public void setItem(T item){
        this.item = item;
    }
}
