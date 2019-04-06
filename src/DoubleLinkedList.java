/**
 * DoubleLinkedList.java
 * Justin Granofsky
 * 4/6/2018
 * Double Linked list object class.
 */
public class DoubleLinkedList<E> { 
  
  private Node<E> head;
  
  public void swap(E item1, E item2){
    int index1 = indexOf(item1);
    int index2 = indexOf(item2);
    
    Node<E> node1 = getNode(index1);
    Node<E> node2 = getNode(index2);
    
    E temp = node1.getItem();
    node1.setItem(node2.getItem());
    node2.setItem(temp);
    
  }
  
  public void add(E item) { 
    Node<E> tempNode = head;
    
    if (tempNode == null) {
      head = new Node<E>(null, null);
      head.setNext(new Node<E>(item, head));
      return;
    }
    
    while(tempNode.getNext() != null) { 
      tempNode = tempNode.getNext();
    }
    
    tempNode.setNext(new Node<E>(item, tempNode));
  }
  
  
  public Node<E> getNode(int index) { 
    int nodeIndex = 0;
    // Loop through all the nodes.
    Node<E> currentNode = head;
    if(currentNode == null){
      return null; 
    }
    if(index >= this.size()){
      return null; 
    }
    boolean found = false;
    while(!found){
      currentNode = currentNode.getNext();
      if(nodeIndex == index){
        found = true; 
      }
      nodeIndex++;
    }
    
    return currentNode;
  }
  
  public E get(int index) { 
    int nodeIndex = 0;
    // Loop through all the nodes.
    Node<E> currentNode = head;   
    // This means the list is null, and has no values.
    if(currentNode == null){
      return null; 
    }   
    // The index was too big, and out of range.
    if(index >= this.size()){
      return null; 
    }
    // Loop until you find the object.
    boolean found = false;
    while(!found){
      currentNode = currentNode.getNext();
      if(nodeIndex == index){
        found = true; 
      }
      nodeIndex++;
    }
    
    return currentNode.getItem();
  }
  
  public int indexOf(E item) { 
    for(int i = 0; i < this.size(); i++){
      if(this.get(i).equals(item)){
        return i;
      }
    }
    
    return -1;
  }
  
  /**
   * Remove a certain element from the array
   * @param index, the index where the element is being stored.
   */
  public boolean remove(int index) { 
    // Find the node to be removed.
    Node<E> removeNode = this.getNode(index);
    
    if(removeNode == null){
      return false; 
    }
    
    // Check if you're removing the head
    if(removeNode.equals(head.getNext())){
      head.setNext(removeNode.getNext());
      if(removeNode.getNext() != null) {
        removeNode.getNext().setPrevious(head);
      }
      return true;
    }
    
    // Get the previous node before the one you're trying to remove.
    Node<E> previousNode = removeNode.getPrevious();
    // Set the next node of the previous node to the next node of the one you're trying to remove.
    previousNode.setNext(removeNode.getNext());  
    if(removeNode.getNext() != null){
      removeNode.getNext().setPrevious(previousNode);
    }
    return true;
  }
  
  /**
   * Clears the entire array.
   */
  public void clear() { 
    head = null;
  }
  
  /**
   * Checks to see if the array is empty.
   * @return true if the array is empty, false if the array has 1 or more elements.
   */
  public boolean isEmpty(){
    if(this.size() == 0){
      return true; 
    }
    return false;
  }
  
  /**
   * Checks if the list contains a certain element.
   * @param item, the object to be checked if it's in the array.
   * @return true if the object is found, false if it is not found.
   */
  public boolean contains(E item){
    if(this.indexOf(item) == -1){
      return false; 
    }else{
      return true; 
    }
  }
  
  public int size() { 
    int count = 0;
    Node<E> currentNode = head;
    if(head != null) {
      while (currentNode.getNext() != null) {
        count++;
        currentNode = currentNode.getNext();
      }
    }
    return count;
  }
}