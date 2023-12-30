package ca.sheridancollege.ninik.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ticket {

	 
	  int ticketId;
	  String guestName ;
	  double price; 
	  String ticketType;
	  String ticketDescription ;
	  String seat;
	  long secretCode;
	  long userId;
      
	  
	
	
}
