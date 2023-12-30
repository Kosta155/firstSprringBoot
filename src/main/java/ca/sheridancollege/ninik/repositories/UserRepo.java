
package ca.sheridancollege.ninik.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ninik.beans.Ticket;
import ca.sheridancollege.ninik.beans.User;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserRepo {

	private NamedParameterJdbcTemplate jdbc;

    public ArrayList<User> getUsers()
    {
        String query= "Select * From SEC_USER";
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        ArrayList<User> users = (ArrayList<User>) jdbc.query(query, parameters, new BeanPropertyRowMapper<User>(User.class));
        return users;

    }

    public User getUserName(String username) {
        MapSqlParameterSource parameters =  new MapSqlParameterSource();
        String query="Select * FROM SEC_USER WHERE username= :woof";
        parameters.addValue("woof",username);
        ArrayList<User> users = (ArrayList<User>)
                jdbc.query(query,parameters,
                        new BeanPropertyRowMapper<User>(User.class));

        return (users.size()>0)?users.get(0):null;

    }

    public ArrayList<String> getRolesById(long userId){
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        String query = "SELECT user_role.userId, sec_role.roleName "
                + "FROM user_role, sec_role WHERE "
                + "user_role.roleId=sec_role.roleId and userId=:id";
        parameters.addValue("id",userId);
        ArrayList<String> roles = new ArrayList<String>();
        List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
        for (Map<String,Object> row : rows)
        {
            roles.add((String)row.get("roleName"));

        }
        return roles;

    }

    public void addUser(String username , String password)
    {
        String query = "insert into SEC_User (userName, encryptedPassword, ENABLED)\r\n"
                + "values  (:uname, :ep, 1);";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("uname", username);
        parameters.addValue("ep", passwordEncoder().encode(password));
        jdbc.update(query, parameters);

    }

    public void addUserRole(long userId , long roleId)
    {
        String query = "insert into user_role (userId, roleId) values (:uid, :rid)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("uid", userId);
        parameters.addValue("rid", roleId);
        jdbc.update(query, parameters);

    }

    public String getUserNameByUserId(long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT userName FROM SEC_USER where userId = :uid";
        parameters.addValue("uid",id);

        ArrayList<User> users = (ArrayList<User>)
                jdbc.query(query,parameters,
                        new BeanPropertyRowMapper<User>(User.class));

        if(users.size()>0)
        {
            return users.get(0).getUserName();
        }
        else{
            return null;
        }
	}
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}


	


