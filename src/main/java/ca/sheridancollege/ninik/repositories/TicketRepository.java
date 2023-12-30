
package ca.sheridancollege.ninik.repositories;

import java.util.ArrayList;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ninik.beans.Ticket;
import ca.sheridancollege.ninik.beans.User;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Repository
public class TicketRepository {

	
	private NamedParameterJdbcTemplate jdbc;

    public ArrayList<Ticket> getTicketsByUser(User user){
        MapSqlParameterSource parameters =  new MapSqlParameterSource();
        String query;
        if(user.getUserId()==1)
        {
         query="Select * FROM TICKETS";
        }
        else
        {
         query="Select * FROM TICKETS where userId=:uid";
         
        }
       
        parameters.addValue("uid", user.getUserId());
        	
        ArrayList<Ticket> tickets = (ArrayList<Ticket>)
                        jdbc.query(query,parameters,
                        new BeanPropertyRowMapper<Ticket>(Ticket.class));
          

       
        return tickets;
    }
    
    

    public void addTicket(Ticket ticket)
    {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
       String query="insert into TICKETS (guestName,price,ticketType,ticketDescription,seat,secretCode,userId)" +
                       " Values"
                       + "(:na,:pr,:type,:desc,:seat,:sec,:uId)";
        parameters.addValue("na",ticket.getGuestName()); //no columns
        parameters.addValue("pr",ticket.getPrice());
        parameters.addValue("type",ticket.getTicketType());
        parameters.addValue("desc",ticket.getTicketDescription());
        parameters.addValue("seat",ticket.getSeat());
        parameters.addValue("sec",ticket.getSecretCode());
        parameters.addValue("uId",ticket.getUserId());

        jdbc.update(query,parameters);
    }

    public Ticket getTicketByTicketId(int ticketId) {
        MapSqlParameterSource parameters =  new MapSqlParameterSource();
        String query="Select * FROM TICKETS WHERE ticketId = :id";
        parameters.addValue("id",ticketId);
        ArrayList<Ticket> tickets = (ArrayList<Ticket>)
                jdbc.query(query,parameters,
                        new BeanPropertyRowMapper<Ticket>(Ticket.class));

        if(tickets.size()>0)
        {
            return tickets.get(0);
        }
        else{
            return null;
        }

    }

    public void editTicket(Ticket ticket) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
             
        String query = "UPDATE TICKETS SET guestName=:na , price=:pr , ticketType=:a1 , ticketDescription=:m2 , seat=:a2 , secretCode=:dir , userId=:uId where ticketId =:id";
        parameters.addValue("na",ticket.getGuestName()); //no columns
        parameters.addValue("pr",ticket.getPrice());
        parameters.addValue("a1",ticket.getTicketType());
        parameters.addValue("m2",ticket.getTicketDescription());
        parameters.addValue("a2",ticket.getSeat());
        parameters.addValue("dir",ticket.getSecretCode());
        parameters.addValue("id",ticket.getTicketId());
        parameters.addValue("uId",ticket.getUserId());

        jdbc.update(query,parameters);
    }

    public void deleteTicket(Ticket ticket) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "DELETE from TICKETS where ticketId=:ticketId";
        parameters.addValue("ticketId",ticket.getTicketId());
        jdbc.update(query,parameters);

    }

	
	
}
