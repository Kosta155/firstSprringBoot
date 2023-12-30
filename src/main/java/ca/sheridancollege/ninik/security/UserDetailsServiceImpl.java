package ca.sheridancollege.ninik.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.ninik.repositories.UserRepo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepo secRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        ca.sheridancollege.ninik.beans.User user = secRepo.getUserName(username);
        if(user == null) {
            System.out.println("My user was not found");
            throw new UsernameNotFoundException("User not found");
        }
        List<String> roles = secRepo.getRolesById(user.getUserId());
        //Convert List of strings to a list of granted authorities
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        
        for(String role: roles)
        {
            grantList.add(new SimpleGrantedAuthority(role));
        }


        User springUser = new User(user.getUserName(), user.getEncryptedPassword(), grantList);

        return(UserDetails)springUser;
    }

}
