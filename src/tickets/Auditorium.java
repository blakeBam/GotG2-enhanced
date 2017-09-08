//bcb140130
//Blake Bambico
//CS 2336.002
package tickets;

public class Auditorium{
  //members
  Ticket head;
  int seats;
  int rows;
  
  //constructors
  public Auditorium()
  {
    head = null;
    seats = 0;
    rows = 0;
  }
  
  public Auditorium(Ticket h)
  {
    head = h;
    seats = 0;
    rows = 0;
  }
  
  //accessors
  public void setHead(Ticket h) {head = h;}
  public void setRows(int r) {rows = r;}
  public void setSeats(int s) {seats = s;}
  
  //mutators
  public Ticket getHead() {return head;}
  public int getRows() {return rows;}
  public int getSeats() {return seats;}
  
  //mathod for adding tickets to the auditorium
  public void addTicket(Ticket t)
  {
    //determine if there is already a head and if not set the new one as head
    if(head == null) {head = t;}
    else
    {
      //go to the end of the linked list and add in the new seat
      Ticket temp = head;
      while(temp.getNext() != null) {temp = temp.getNext();}
      temp.setNext(t);
      t.setPrev(temp);
    }
  }
}