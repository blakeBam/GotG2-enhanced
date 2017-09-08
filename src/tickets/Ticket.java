//bcb140130
//Blake Bambico
//CS 2336.002
package tickets;

public class Ticket {
  //members for each ticket
  private int row;
  private int seat;
  private Ticket prev = null;
  private Ticket next = null;
  private String type = "default";
  private boolean reserved;
  
  //Constructors for tickets
  public Ticket()
  {
    row = 0;
    seat = 0;
    reserved = false;
  }
  public Ticket(int r, int s)
  {
    row = r;
    seat = s;
    reserved = false;
  }
  public Ticket(int r, int s, boolean res)
  {
    row = r;
    seat = s;
    reserved = res;
  }
  
  //accesors
  public int getRow() {return row;}
  public int getSeat() {return seat;}
  public Ticket getPrev() {return prev;}
  public Ticket getNext() {return next;}
  public String getType() {return type;}
  public boolean isReserved() {return reserved;}
  
  //mutators
  public void setRow(int r) {row = r;}
  public void setSeat(int s) {seat = s;}
  public void setPrev(Ticket p) {prev = p;}
  public void setNext(Ticket n) {next = n;}
  public void setType(String t) {type = t;}
  
  //methods for reserving and unreserving a seat
  public void reserveSeat(String t)
  {
    type = t;
    reserved = true;
  }
  
  public void unreserveSeat()
  {
    type = "default";
    reserved = false;
  }
  
  public boolean hasNext() {return next != null;}
  public boolean hasPrev() {return prev != null;}
}