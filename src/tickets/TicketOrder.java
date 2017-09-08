//bcb140130
//CS 2336.002
//Blake Bambico
package tickets;

public class TicketOrder {
  //members
  private Ticket seat = null;
  private TicketOrder next = null;
  private TicketOrder prev = null;
  
  //constructors
  public TicketOrder(){}
  public TicketOrder(Ticket s) {seat = s;}
  
  //mutators
  public void setSeat(Ticket s) {seat = s;}
  public void setNext(TicketOrder n) {next = n;}
  public void setPrev(TicketOrder p) {prev = p;}
  
  //accessors
  public Ticket getSeat() {return seat;}
  public TicketOrder getNext() {return next;}
  public TicketOrder getPrev() {return prev;}
}