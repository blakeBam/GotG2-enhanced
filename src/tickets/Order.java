//bcb140130
//CS 2336.002
//Blake Bambico
package tickets;

public class Order {
  private TicketOrder head = null;
  private int auditorium = 0;
  private int adult = 0;
  private int senior = 0;
  private int child = 0;
  private int length = 0;
  
  //constructors
  public Order() {}
  public Order(int a) {auditorium = a;}
  public Order(TicketOrder h) 
  {
    head = h;
    if(h != null) 
    {
      length++;
      addType(h.getSeat());
    }
  }
  public Order(TicketOrder h, int a)
  {
    head = h;
    auditorium = a;
    if(h != null) 
    {
      length++;
      addType(h.getSeat());
    }
  }
  
  //mutators
  public void setHead(TicketOrder h) {head = h;}
  public void setAuditorium(int a) {auditorium = a;}
  public void setAdult(int a) {adult = a;}
  public void setSenior(int s) {senior = s;}
  public void setChild(int c) {child = c;}
  
  //accessors
  public TicketOrder getHead() {return head;}
  public int getAuditorium() {return auditorium;}
  public int getAdult() {return adult;}
  public int getSenior() {return senior;}
  public int getChild() {return child;}
  public int getLength() {return length;}
  
  //method for adding the corresponding seat type to the total
  private void addType(Ticket type)
  {
    if(type.getType().equals("Adult")) {adult++;}
    else if(type.getType().equals("Senior")) {senior++;}
    else {child++;}
  }
  
  //method for removing the correspoind type of seat from the total
  private void delType(Ticket type)
  {
    if(type.getType().equals("Adult")) {adult--;}
    else if(type.getType().equals("Senior")) {senior--;}
    else {child--;}
  }
  
  //method for adding a node to the order
  public void addNode(TicketOrder add)
  {
    //check if our head is null or not
    if(add == null) {return;}
    if(head == null) {head = add;}
    else
    {
      //loop to the place where the new node belongs
      TicketOrder temp = head;
      //crazy while loop to keep the tickets added in numerical order for sanity
      while(temp.getNext() != null && (temp.getSeat().getRow() < add.getSeat().getRow() || (temp.getSeat().getRow() == add.getSeat().getRow() && temp.getSeat().getSeat() < add.getSeat().getSeat()))) {temp = temp.getNext();}
      //depending on why we broke out of the loop put the node in the appropriate place
      if(temp == head && head.getNext() != null)
      {
	add.setNext(head);
	head.setPrev(add);
	head = add;
      }
      else if(temp == head && (temp.getSeat().getRow() < add.getSeat().getRow() || (temp.getSeat().getRow() == add.getSeat().getRow() && temp.getSeat().getSeat() < add.getSeat().getSeat(
      ))))
      {
	head.setNext(add);
	add.setPrev(head);
      }
      else if(temp == head)
      {
	add.setNext(head);
	head.setPrev(add);
	head = add;
      }
      else if(temp.getNext() != null)
      {
	
	add.setPrev(temp.getPrev());
	add.setNext(temp);
	temp.setPrev(add);
	add.getPrev().setNext(add);
      }
      else if(temp.getSeat().getRow() < add.getSeat().getRow() || (temp.getSeat().getRow() == add.getSeat().getRow() && temp.getSeat().getSeat() < add.getSeat().getSeat()))
      {
	add.setPrev(temp);
	temp.setNext(add);
      }
      else
      {
	add.setPrev(temp.getPrev());
	add.setNext(temp);
	temp.setPrev(add);
	add.getPrev().setNext(add);
      }
    }
    addType(add.getSeat());
    //increase the length
    length++;
  }
  
  //method for deleting a node at a place
  public void delNode(int place)
  {
    //check that the requested place is in the list
    if(place < 1 || place > length) {return;}
    //if the user wants to delete the head then replace the head with its next
    if(place == 1)
    {
      delType(head.getSeat());
      head.getSeat().unreserveSeat();
      if(head.getNext() != null) 
      {
	head = head.getNext();
	head.getPrev().setNext(null);
	head.setPrev(null);
      }
      else {head = null;}
      length--;
      return;
    }
    //loop to the place that the user wants to delete
    TicketOrder temp = head;
    for(int i = 1; i < place; i++) {temp = temp.getNext();}
    //check what kind of seat we are removing from the order
    delType(temp.getSeat());
    temp.getSeat().unreserveSeat();
    //start moving pointers away from the node to be deleted
    temp.getPrev().setNext(temp.getNext());
    //check if the node is the last node in the list
    if(temp.getNext() != null) {temp.getNext().setPrev(temp.getPrev());}
    //remove the node entirely and decrease the length
    temp.setNext(null);
    temp.setPrev(null);
    length--;
  }
  
  //method for deleteing the entire order
  public void delOrder()
  {
    //delete the head until there is no head left to delete
    while(length > 0) {delNode(1);}
  }
  
  //method for displaying detailed information about the order to the user
  public void displayOrderDetail()
  {
    //make sure the order exists
    if(head != null)
    {
      TicketOrder temp = head;
      displayOrder();
      System.out.print(" Seats(row,seat): ");
      while(temp != null)
      {
	System.out.print("(" + temp.getSeat().getRow() + "," + temp.getSeat().getSeat() + ") ");
	temp = temp.getNext();
      }
      System.out.println();
    }
  }
  
  //method for displaying general information to the user
  public void displayOrder()
  {
    if(head != null)
    {
      //display the information to the user
      System.out.println("Auditorium: " + auditorium + " # of Adults: " + adult 
	      + " # of Seniors: " + senior + " # of Childs: " + child);
    }
  }
  
  //method for displaying each seat individually along with what kind of seat it is
  public void ticketDisplay()
  {
    TicketOrder temp = head;
    int count = 1;
    while(temp != null)
    {
      System.out.println(count + ". " + " row: " + temp.getSeat().getRow() + " seat: "
      + temp.getSeat().getSeat() + " type: " + temp.getSeat().getType());
      temp = temp.getNext();
      count++;
    }
  }
  
  //method for displaying the tickets and the total cost for the order
  public double displayTotal()
  {
    displayOrderDetail();
    //add the total to the next line
     double total = adult*10+senior*7.5+child*5.25;
    System.out.println("The total spent for this order is: $" + total);
    return total;
  }
}