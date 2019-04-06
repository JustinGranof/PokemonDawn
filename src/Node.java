/**
 * Node.java
 * Justin Granofsky
 * 4/6/2018
 * Node object class.
 */
public class Node<T> { 
  
  private T item;
  private Node<T> next;
  private Node<T> previous;
   
  public Node(T item, Node<T> previous){
   this.item = item;
   this.next = null;
   this.previous = previous;
  }
 
  public Node<T> getNext(){
    return this.next;
  }
  
  public Node<T> getPrevious(){
   return this.previous; 
  }
  
  public void setPrevious(Node<T> previous){
   this.previous = previous; 
  }
  
  public void setNext(Node<T> next){
    this.next = next;
  }
  
  public T getItem(){
    return this.item;
  }
  
  public void setItem(T item){
    this.item = item;
  }
}
