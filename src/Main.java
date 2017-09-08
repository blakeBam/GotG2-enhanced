//bcb140130
//Blake Bambico
//CS 2336.002
import tickets.*;
import java.util.*;
import java.io.*;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    //all the file opjects kept together for ease of use
    File[] audiFiles = {new File("A1.txt"), new File("A2.txt"), new File("A3.txt")};
    
    //legendary universal scanner
    Scanner input;
    
    //all the auditoriums kept together for ease of use
    Auditorium[] audi = {new Auditorium(), new Auditorium(), new Auditorium()};
    //continually loop through the auditorium files and read them into memory
    for(int i = 0; i < audi.length; i++)
    {
      if(audiFiles[i].exists())
      {
	input = new Scanner(audiFiles[i]);
	setAudi(audi[i], input);
      }
      else
      {
	//leave the program if the file doesn't exist
	System.out.println("File doesn't exist");
	System.exit(0);
      }
    }
    
    //read in the users to a hash map for later use in the program
    File users = new File("userdb.dat");
    HashMap<String, User> database = new HashMap();
    if(users.exists())
    {
      input = new Scanner(users);
      setDatabase(database, input);
    }
    else
    {
      //leave the program if the users file doesn't exist
      System.out.println("The users file doesn't exist");
      System.exit(0);
    }
    
    //keep track of the users choices
    boolean exit = false;
    input = new Scanner(System.in);
    while(!exit)
    {
      //ask the user for a username and make sure it is in the hashmap
      System.out.print("Enter a username to get started: ");
      String user = input.nextLine();
      while(!database.containsKey(user))
      {
	System.out.print("Enter a valid username: ");
	user = input.nextLine();
      }
      
      //get the users data from the hashmap to test the password
      User cur = database.get(user);
      boolean flag = false;
      //loop through 3 times for the max number of tries a user gets
      for(int i = 0; i < 3 && !flag; i++)
      {
	System.out.print("Enter your password: ");
	user = input.nextLine();
	if(user.equals(cur.getPassword())) {flag = true;}
	else if(i < 2) {System.out.println("You have " + (2 - i) + " attempt(s) remaining");}
	else {System.out.println("You are out of attempts, please login again");}
      }
      
      //if the user enters the correct password enter the main menu 
      //check if the user is the admin or not
      if(flag && !cur.getUsername().equals("admin")) {mainMenu(input, audi, cur);} 
      else if(flag) 
      {
	exit = true;
	adminMenu(input, audi);
      }
      //clear the input for the next iteration
      input.nextLine();
    }
    
    //once the program is done continually write the auditoriums back to their file
    for(int i = 0; i < audi.length; i++)
    {
      PrintWriter output = new PrintWriter(audiFiles[i]);
      printAudi(audi[i], output);
      output.close();
    }
  }
  
  //mathod for reading in an auditorium into memory
  public static void setAudi(Auditorium audi, Scanner input)
  {
    //intialize the row and seat size for the auditorium
    int row = 0, seat = 0;
    //loop while the file has more lines
    while(input.hasNext())
    {
      //hold the line in memory as well as the length of the line
      String line = input.nextLine();
      seat = line.length();
      Ticket temp;
      //loop through each seat and determine if it is open or reserved
      for(int i = 0; i < seat; i++)
      {
	if(line.charAt(i) == '#') {temp = new Ticket(row + 1, i + 1, false);}
	else {temp = new Ticket(row + 1, i + 1, true);}
	//add each seat to the auditorium
	audi.addTicket(temp);
      }
      //count the rows
      row++;
    }
    //add the row and seat size to the auditorium linked list
    audi.setRows(row);
    audi.setSeats(seat);
  }
  
  //method for reading in the usernames and passwords from the file to the hashmap
  public static void setDatabase(HashMap<String, User> hash, Scanner input)
  {
    while(input.hasNext())
    {
      String username = input.next(), password = input.next();
      User temp = new User(username, password);
      hash.put(username, temp);
    }
  }
  
  public static int validateInput(Scanner input, String display, int min, int max)
  {
    while(true)
    {
      System.out.print(display);
      try
      {
	int choice = input.nextInt();
	if(choice < min || choice > max) {throw new InputMismatchException();}
	return choice;
      }
      catch (InputMismatchException e)
      {
	System.out.println("Error please enter an integer from " + min + " to " + max + "\n");
	input.nextLine();
      }
    }
  }
  
  public static int validateInput(Scanner input, String display)
  {
    while(true)
    {
      System.out.print(display);
      try
      {
	int choice = input.nextInt();
	if(choice < 0) {throw new InputMismatchException();}
	return choice;
      }
      catch (InputMismatchException e)
      {
	System.out.println("Error please enter a non negative integer\n");
	input.nextLine();
      }
    }
  }
  
  //method for the main menu which subverts to other functions for ease of reading
  public static void mainMenu(Scanner input, Auditorium[] audi, User user)
  {
    int choice = 0;
    //continually loop through the menu until the user would like to exit
    while(choice != 5)
    {
      String display = "------MAIN MENU------\n\n1. Reserve Seats\n"
	      + "2. View Orders\n3. Update Order\n4. Display Receipt\n5. Log Out\n";
      choice = validateInput(input, display, 1, 5);
      
      //depending on the choice go to the appropriate submenu
      switch(choice) {
	case 1:
	{
	  //get seat from the user to reserve
	  Order curOrder = new Order();
	  int audiChoice = validateAudi(input);
	  checkSeats(input,audi,user,curOrder,audiChoice);
	  if(curOrder.getHead() != null) {user.addOrder(curOrder);}
	  break;
	}
	case 2:
	{
	  //display the orders that the user has placed
	  if(user.getOrders().getHead() != null) {user.getOrders().displayOrdersDetail();}
	  else {System.out.println("You don't have any orders yet");}
	  break;
	}
	case 3:
	{
	  //enter the update order method and let the user decide from there
	  updateOrder(input, user, audi);
	  break;
	}
	case 4:
	{
	  //display the recipt to the user
	  user.getOrders().displayReceipt();
	  break;
	}
      }
    }
  }
  
  //method for updating the users  orders
  public static void updateOrder(Scanner input, User user, Auditorium[] audi)
  {
    if(user.getOrders().getHead() != null)
    {
      //display the general information to the user
      user.getOrders().displayOrders();
      //get the order to work on as well as what action the user would like to make
      String display= "Select an order to update: ";
      int order = validateInput(input, display, 1, user.getOrders().getLength());
      display = "1. Add Tickets to Order\n2. Delete tickets from order\n3. Cancel Order\n";
      int choice = validateInput(input, display, 1, 3);
      switch(choice)
      {
	case 1:
	{
	  //enter the reservation system with this order
	  Order temp = user.getOrders().getNode(order).getOrder();
	  checkSeats(input, audi, user, temp, temp.getAuditorium());
	  break;
	}
	case 2:
	{
	  //if the user wants to delete tickets then enter method
	  deleteTickets(input, user, order);
	  break;
	}
	default:
	{
	  //delete the order
	  user.getOrders().delNode(order);
	  break;
	}
      }
    }
    else{System.out.println("You have no orders to update");}
  }
  
  //method for deleting tickets from an order
  public static void  deleteTickets(Scanner input, User user, int order)
  {
    Order temp = user.getOrders().getNode(order).getOrder();
    boolean flag = false;
    int delete = -1;
    //loop to validate the input from the user
    while(!flag)
    {
      temp.ticketDisplay();
      System.out.println((temp.getLength() + 1) + ". Exit");
      System.out.print("Select a seat you would like to remove or exit: ");
      try
      {
	delete = input.nextInt();
	if(delete < 1 || delete > temp.getLength() + 1) {throw new InputMismatchException();}
	flag = true;
      }
      catch(InputMismatchException e)
      {
	System.out.println("Error please enter an integer from 1 to " + (temp.getLength() + 1));
	input.nextLine();
      }
    }
    //if they want to exit the leave the function
    if(delete == temp.getLength() + 1) {return;}
    //remove the requested ticket and check if there is any more tickets to delete
    temp.delNode(delete);
    if(temp.getLength() == 0) {user.getOrders().delNode(order);}
    else {deleteTickets(input, user, order);}
  }
  
  //method to add before actually reserving seats
  public static int validateAudi(Scanner input)
  {
    //format the display for the menu
    String display = "-----AUDITORIUMS-----\n\n1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3\n";
    
    //call a function to validate the input from the user for the choice of auditorium
    return validateInput(input, display, 1, 3);
  }
  
  //option 1 of the main menu, attempt to reserve a seat for the user and create a new order
  public static void checkSeats(Scanner input, Auditorium[] audi, User user, Order curOrder, int audiChoice)
  {    
    //display the auditorium to the user
    displayAudi(audi, audiChoice-1);
    
    //use the array to store how many of each type the user would like and keep a running total
    int[] number = new int[3];
    int total = 0;
    String[] types = {"Adult", "Senior", "Child"};
    String display = "";
    
    //validate the number of tickets in a loop for ease
    for(int i = 0; i < 3; i++)
    {
      display = "How many " + types[i] + " seats would you like to reserve: ";
      number[i] = validateInput(input, display);
      total += number[i];
    }
    
    //make sure the user wants at least one seat
    if(total == 0) {System.out.println("No seats were reserved since you selected none");}
    
    //intitalize an int array to hold where the user would like to reserve seats 
    int[][] seats = new int[total][2];
    
    //loop through the total amount of seats that the user requested
    for(int i = 0; i < total; i++)
    {
      display = "Enter the row for your ticket: ";
      seats[i][0] = validateInput(input, display, 1, audi[audiChoice - 1].getRows());
      
      display = "Enter the seat for your ticket: ";
      seats[i][1] = validateInput(input, display, 1, audi[audiChoice - 1].getSeats());
    }
    
    //flag for checking if all the seats are available or not
    boolean open = true;
    
    //check if the users selected seats are available
    for(int i = 0; i < total && open; i++)
    {
      //node for looping through the list
      Ticket temp = audi[audiChoice - 1].getHead();
      while(true)
      {
	//check if we are at the desired node and if so check if it is available
	if(temp.getRow() == seats[i][0] && temp.getSeat() == seats[i][1])
	{
	  open = !temp.isReserved();
	  break;
	}
	temp = temp.getNext();
      }
    }
    
    //reserve the seats for the user if all the seats are open
    if(open)
    {
      //create a new order to hold all the seats for the user
      curOrder.setAuditorium(audiChoice);
      
      //call a function to reserve the seats and place them in an order
      reserveSeats(audi[audiChoice - 1], seats, curOrder, number, types);
      System.out.println("Your seats have been reserved!\n");
    }
    //if its not valid then attempt to find the best available seat for the user
    else if(total <= audi[audiChoice - 1].getSeats())
    {
      //call a function to locate the best available seats
      Ticket best = bestAvailable(audi[audiChoice - 1], total, number, types);
      if(best != null)
      {
	//string for holding onto the user input
	String decision = "";
	while(true)
	{
	  //let the user know that we have found some seats for them and get confirmation
	  System.out.print("We found " + total + " seat");
	  if(total != 1) {System.out.print("s");}
	  System.out.println(" starting at row " + best.getRow() + " seat " + best.getSeat());
	  System.out.print("Would you like to reserve these seats?(Y/N): ");
	  //validate the the user input Y or N
	  try
	  {
	    decision = input.next();
	    if(!decision.equals("Y") && !decision.equals("N")) {throw new InputMismatchException();}
	    break;
	  }
	  catch (InputMismatchException e)
	  {
	    System.out.println("Error please only enter Y or N");
	  }
	}
	//if they would like the seats then reserve them
	if(decision.equals("Y"))
	{
	  //make a new order with all the seats in them
	  curOrder.setAuditorium(audiChoice);
	  for(int i = 0; i < total; i++)
	  {
	    //make sure to mark wether they are A, S or C seats
	    if(i < number[0]) {best.reserveSeat(types[0]);}
	    else if(i < number[0] + number[1]) {best.reserveSeat(types[1]);}
	    else  {best.reserveSeat(types[2]);}
	    
	    //add the seat to the order
	    curOrder.addNode(new TicketOrder(best));
	    best = best.getNext();
	  }
	  //add the order to the users stuff
	  System.out.println("Your seats have been reserved!\n");
	}
	//let them know that nothing has been reserved
	else {System.out.println("No seats have been reserved");}
      }
      //let them know that no seats were found in the best available search
      else {System.out.println("No seats were found");}
    }
    //we can't possibly find seats in this case so let the user know
    else{System.out.println("No seats were found");}
 }
  
  //mehtod for reserving the seats if they are all available
  public static void reserveSeats(Auditorium audi, int[][] seats, Order curOrder, int[] num, String[] types)
  {
    for(int i = 0; i < seats.length; i++)
    {
      //node for looping through the list
      Ticket temp = audi.getHead();
      while(true)
      {
	//check if we are at the desired node and if so check if it is available
	if(temp.getRow() == seats[i][0] && temp.getSeat() == seats[i][1])
	{
	  //mark each seat depending on how many we have gone through
	  if(i < num[0]) {temp.reserveSeat(types[0]);}
	  else if(i < num[0] + num[1]) {temp.reserveSeat(types[1]);}
	  else {temp.reserveSeat(types[2]);}
	  //add a new node to mark the seat in an order and add to the order
	  TicketOrder ticOrder = new TicketOrder(temp);
	  curOrder.addNode(ticOrder);
	  break;
	}
	temp = temp.getNext();
      }
    }
  }
  
  //method for finding the best avaiable seats in the auditorium
  public static Ticket bestAvailable(Auditorium audi, int total, int[] num, String[] types)
  {
    //a few tickets to hold the best, the potential new best and the cur iterating pointer
    Ticket best = null, hold = null, cur = audi.getHead();
    //get the midpoint of the auditorium
    int[] midpoint = {(audi.getRows()+1)/2,(audi.getSeats()+1)/2};
    double dist = 0;
    //loop while we still can
    while(cur != null)
    {
      //test if the seat is reserved
      if(!cur.isReserved())
      {
	//mark the beginning of the seats with hold and a flag for testing if the seats are available
	boolean flag = true;
	hold = cur;
	//hold onto the row of cur to make sure we stay on the same row
	int row = cur.getRow();
	//make sure we can move to the next or we only need one seat
	if(!cur.hasNext() && total > 1) {flag = false;}
	else {cur = cur.getNext();}
	//continually loop for how many seats we desire and check if they are all available
	for(int i = 1; i < total && flag; i++)
	{
	  if(cur != null && !cur.isReserved() && row == cur.getRow()) {cur = cur.getNext();}
	  else {flag = false;}
	}
	//enter if they are all available
	if(flag)
	{
	  //check if we have found an available before
	  if(best != null)
	  {
	    Ticket temp = hold;
	    //move temp to the midpoint of the available tickets
	    for(int i = 1; i < (total + 1)/ 2; i++) {temp = temp.getNext();}
	    double dist1 = calcDistance(midpoint, temp);
	    //check if the distance to the new best seats is better than the old best seats
	    if(dist1 < dist)
	    {
	      best = hold;
	      dist = dist1;
	    }
	    //if the distances are the same take the row closer to the middle
	    else if(dist1 == dist && Math.abs(best.getRow() - midpoint[0]) > Math.abs(hold.getRow() - midpoint[0]))
	    {
	      best = hold;
	    }
	  }
	  //if not set the ones we just found as the best then set the distance between them as well
	  else 
	  {
	    best = hold;
	    dist = calcDistance(midpoint, best);
	  }
	}
      }
      //if its if reserved then move on to the next seat
      else {cur = cur.getNext();}
    }
    return best;
  }
  
  //method for calculating the distance between two points
  public static double calcDistance(int[] midpoint, int[] point)
  {
    return Math.sqrt(Math.pow(midpoint[0] - point[0], 2) + Math.pow(midpoint[1] - point[1], 2));
  }
  
  //method for calculating the distance between a point and a ticket
  public static double calcDistance(int[] midpoint, Ticket node)
  {
    return Math.sqrt(Math.pow(midpoint[0] - node.getRow(), 2) + Math.pow(midpoint[1] - node.getSeat(), 2));
  }
  
  //method for the admin menu interface
  public static void adminMenu(Scanner input, Auditorium[] audi)
  {
    //display the main menu to user and validate the input
    int choice = 0;
    while(choice != 3)
    {
      System.out.println("------MAIN MENU------\n");
      System.out.println("1. View Auditorium");
      System.out.println("2. Print Report");
      System.out.println("3. Exit");
      try
      {
	choice = input.nextInt();
	if(choice < 1 || choice > 3) {throw new InputMismatchException();}
      }
      catch(InputMismatchException e)
      {
	System.out.println("Error enter an integer from 1 to 3\n");
	input.nextLine();
	continue;
      }
      switch (choice)
      {
	case 1:
	{
	  //display the auditoriums to the admin
	  displayAudi(audi,validateAudi(input) - 1);
	  break;
	}
	case 2:
	{
	  //display the formatted report to the user
	  printReport(audi);
	  break;
	}
	default: break;
      }
    }
  }
  
  //method for neatly formatting the final report to the user
  public static void printReport(Auditorium[] audi)
  {
    //store the total for each section
    int[] totals = new int[5];
    double total = 0;
    System.out.println("   Labels    Open  Reserved Adult   Senior  Child     Total$");
    //loop through each auditoruim
    for(int i = 0; i < audi.length; i++)
    {
      Ticket temp = audi[i].getHead();
      int [] cur = new int[5];
      //count which seats are reserved and open and which types
      while(temp != null)
      {
	if(temp.isReserved())
	{
	  cur[1]++;
	  if(temp.getType().equals("Adult")) {cur[2]++;}
	  else if(temp.getType().equals("Senior")) {cur[3]++;}
	  else if(temp.getType().equals("Child")) {cur[4]++;}
	}
	else {cur[0]++;}
	temp = temp.getNext();
      }
      //add the auditorium totals to the grand total
      for(int j = 0; j < 5; j++) {totals[j] += cur[j];}
      double tempTotal = cur[2]*10+cur[3]*7.5+cur[4]*5.25;
      //display the auditorium report to the admin
      System.out.printf("Auditorium %d: %-8d%-8d%-8d%-8d%-8d$%-8.2f\n", 
	      (i+1),cur[0],cur[1],cur[2],cur[3],cur[4],tempTotal);
      total+=tempTotal;
    }
    //display the total report to the adimin
    System.out.printf("Total:        %-8d%-8d%-8d%-8d%-8d$%-8.2f\n", 
	      totals[0],totals[1],totals[2],totals[3],totals[4],total);
  }
  
  //mathod for displaying the auditorium to the user
  public static void displayAudi(Auditorium[] audi, int place)
  {
    System.out.println("\n-----AUDITORIUM " + (place + 1) + "-----");
    //format the output to line up with the cols
    System.out.print("  ");
    for(int i = 0; i < audi[place].getSeats(); i++) {System.out.print((i+1)%10);}
    System.out.println();
    //create a node to loop through the linked list with
    Ticket temp = audi[place].getHead();
    //go through each row with a nummber next to it to identify where
    for(int i = 0; i < audi[place].getRows(); i++)
    {
      System.out.print((i+1) + " ");
      for(int j = 0; j < audi[place].getSeats(); j++)
      {
	//determine if the seat is open or reserved and show it accordingly
	if(temp.isReserved()) {System.out.print('.');}
	else {System.out.print('#');}
	temp = temp.getNext();
      }
      System.out.println();
    }
  }
  
  //method for printing the auditorium back to the files
  public static void printAudi(Auditorium audi, PrintWriter output)
  {
    //temp for looping through the linkedlist
    Ticket temp = audi.getHead();
    //loop through the row and seats in the row
    for(int i = 0; i < audi.getRows(); i++)
    {
      for(int j = 0; j < audi.getSeats(); j++)
      {
	//determine if the seat is open or reserved
	if(temp.isReserved()) {output.print('.');}
	else {output.print('#');}
	temp = temp.getNext();
      }
      output.println();
    }
  }
}
