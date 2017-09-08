//bcb140130
//CS 2336.002
//Blake Bambico
package tickets;

public class OrderList{
  //members
  private OrderNode head;
  private int length = 0;
  
  //constructors
  public OrderList() {head = null;}
  public OrderList(OrderNode h) 
  {
    head = h;
    if(h != null) {length++;}
  }
  
  //accessors
  public OrderNode getHead() {return head;}
  public int getLength() {return length;}
  
  //method for adding an order to the list
  public void addNode(OrderNode add)
  {
    if(add == null) {return;}
    if(head == null) {head = add;}
    else
    {
      OrderNode temp = head;
      while(temp.getNext() != null) {temp = temp.getNext();}
      add.setPrev(temp);
      temp.setNext(add);
    }
    length++;
  }
  
  //method for deleting an order from the list
  public void delNode(int place)
  {
    //make sure the requested place to delete from is in the list
    if(place < 1 || place > length) {return;}
    //special case for deleting the head
    if(place == 1)
    {
      if(head.getNext() != null) 
      {
	//remove all the seats from the order then remove from the list
	head.getOrder().delOrder();
	head = head.getNext();
	head.getPrev().setNext(null);
	head.setPrev(null);
      }
      else {head = null;}
      length--;
      return;
    }
    //loop to the node to delete
    OrderNode temp = head;
    for(int i = 1; i < place; i++) {temp = temp.getNext();}
    temp.getOrder().delOrder();
    //remove from the list completely
    temp.getPrev().setNext(temp.getNext());
    if(temp.getNext() != null) {temp.getNext().setPrev(temp.getPrev());}
    temp.setNext(null);
    temp.setPrev(null);
    length--;
  }
  
  //method for getting an order node at a certain place
  public OrderNode getNode(int place)
  {
    if(place < 1 || place > length) {return null;}
    OrderNode temp = head;
    for(int i = 1; i < place; i++) {temp = temp.getNext();}
    return temp;
  }
  
  //method for displaying the detailed information of the orders
  public void displayOrdersDetail()
  {
    OrderNode temp = head;
    int count = 1;
    while(temp != null)
    {
      System.out.print("Order " + count + ". ");
      temp.getOrder().displayOrderDetail();
      temp = temp.getNext();
      count++;
    }
  }
  
  //method for displaying the basic information of the orders
  public void displayOrders()
  {
    OrderNode temp = head;
    int count = 1;
    while(temp != null)
    {
      System.out.print("Order " + count + ". ");
      temp.getOrder().displayOrder();
      temp = temp.getNext();
      count++;
    }
  }
  
  //method for displaying the orders to the user as well as the cost of each one
  public void displayReceipt()
  {
    //make sure there is an order to display
    if(head == null) {System.out.println("No orders currently booked so total is $0\n");}
    else
    {
      //loop through each order and display their cost then sum it up and display grand total
      OrderNode temp = head;
      double total = 0;
      while(temp != null)
      {
	total += temp.getOrder().displayTotal();
	temp = temp.getNext();
      }
      System.out.println("Your total purchase amount is: $" + total);
    }
  }
}