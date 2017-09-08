//bcb140130
//CS 2336.002
//Blake Bambico
package tickets;

public class User{
  //members
  private String username;
  private String password;
  private OrderList orders;
  
  //constrructors
  public User()
  {
    username = "";
    password = "";
    orders = new OrderList();
  }
  public User(String u, String p)
  {
    username = u;
    password = p;
    orders = new OrderList();
  }
  
  //accesors
  public String getUsername() {return username;}
  public String getPassword() {return password;}
  
  //accessor and mutator for the orders
  public void setOrders(OrderList o) {orders = o;}
  public OrderList getOrders() {return orders;}
  
  //method for adding in an order to our list
  public void addOrder(Order or) {orders.addNode(new OrderNode(or));}
}