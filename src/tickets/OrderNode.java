//bcb140130
//CS 2336.002
//Blake Bambico
package tickets;

public class OrderNode{
  //members
  private Order order;
  private OrderNode next;
  private OrderNode prev;
  
  //constructors
  public OrderNode()
  {
    order = null;
    next = null;
    prev = null;
  }
  public OrderNode(Order o)
  {
    order = o;
    next = null;
    prev = null;
  }
  
  //mutators
  public void setOrder(Order o) {order = o;}
  public void setNext(OrderNode n) {next = n;}
  public void setPrev(OrderNode p) {prev = p;}
  
  //accessors
  public Order getOrder() {return order;}
  public OrderNode getNext() {return next;}
  public OrderNode getPrev() {return prev;}
}