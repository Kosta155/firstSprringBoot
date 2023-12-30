package ca.sheridancollege.ninik.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import ca.sheridancollege.ninik.beans.Ticket;
import ca.sheridancollege.ninik.beans.User;
import ca.sheridancollege.ninik.repositories.TicketRepository;
import ca.sheridancollege.ninik.repositories.UserRepo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class TicketController {

	private UserRepo secRepo;
	private TicketRepository ticRepo;
	
	
	
	@GetMapping("/")
    public String goHome()
    {
        return "home";
    }
	
	@GetMapping("/viewTickets")
    public ModelAndView goView(Authentication authentication,Model model)
    {	
	    String name = authentication.getName();
	    User user = secRepo.getUserName(name);
        double subtotal=0;
	    for(Ticket ticket: ticRepo.getTicketsByUser(user))
	    		{
	    			subtotal += ticket.getPrice();
	    		}
	    double tax=subtotal*0.13;
	    double total=subtotal+tax;
	   
	    model.addAttribute("tax",tax);
	    model.addAttribute("total",total);
	    model.addAttribute("subtotal",subtotal);
        return new ModelAndView("view","tickets",ticRepo.getTicketsByUser(user));
    }
	
	@GetMapping("/add")
	public ModelAndView goAdd(Model model)
	{
		List<String> users = new ArrayList<String>();
		for(User user : secRepo.getUsers())
		{
			users.add(user.getUserName());
		}
		users.remove("Jon");
		Collections.sort(users);
        model.addAttribute("users" , users );
        Ticket ticket= new Ticket();
        ticket.setPrice(0.01);
		return new ModelAndView("addTicket","ticket",ticket);
	}
	
	@PostMapping(value = {"/add"})
    public String goAdd(@ModelAttribute Ticket ticket, @RequestParam String userName)
    {
        ticket.setUserId(secRepo.getUserName(userName).getUserId());
        ticRepo.addTicket(ticket);
        return "redirect:/add"; //sends a 302 status code
    }
	
	@GetMapping("/edit/{id}")
    public String goEdit(@PathVariable int id, Model model)
    {
        Ticket ticket = ticRepo.getTicketByTicketId(id);
        List<String> users = new ArrayList<String>();
		for(User user : secRepo.getUsers())
		{
			users.add(user.getUserName());
		}
		Collections.sort(users);
		users.remove("Jon");
		users.remove(secRepo.getUserNameByUserId(ticket.getUserId()));
		model.addAttribute("holder", secRepo.getUserNameByUserId(ticket.getUserId()));
	    model.addAttribute("users" , users );
        model.addAttribute("ticket",ticket);
        return "editTicket";
        
        
    }

    @PostMapping("/editTicket")
    public String processEdit(@ModelAttribute Ticket ticket,@RequestParam double pricey)
    {
    	ticket.setPrice(pricey);
    	System.out.print(ticket.getGuestName());
        ticRepo.editTicket(ticket);
        return "redirect:/viewTickets"; //sends a 302 status code
    }
    
    @GetMapping(value = {"/delete/{id}"})
    public String processDelete(@PathVariable int id, Model model)
    {
        Ticket ticket = ticRepo.getTicketByTicketId(id);
        ticRepo.deleteTicket(ticket);
        return "redirect:/viewTickets";
    }

	
	
	@GetMapping("/login")
    public String loginPage()
    {
        return "login";
    }
	
	 @GetMapping("/access-denied")
	    public String noAccess()
	    {
	        return "Access-Denied";
	    }
	 
	 
	 @GetMapping("/registration")
	    public String goRegister()
	    {

	        return "registration";
	    }
	    @PostMapping("/registration")
	    public String goRegister2( @RequestParam String username, @RequestParam String password)
	    {
	        secRepo.addUser(username, password);
	        User user = secRepo.getUserName(username);
	        secRepo.addUserRole(user.getUserId(), 2);

	        return "redirect:/login";  //just to navigate somewhere without a need to add more model

	    }
	
}

