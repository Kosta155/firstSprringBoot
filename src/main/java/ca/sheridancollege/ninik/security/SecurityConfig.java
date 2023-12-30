package ca.sheridancollege.ninik.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;



@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	 private UserDetailsServiceImpl userDetailsService;

	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

	        //remove this before production
	        //or remove after switching to persistent database
	        http.csrf().disable();
	        http.headers().frameOptions().disable();

	        http.authorizeHttpRequests((auth) ->
	                        auth
	                             
	                                .requestMatchers(HttpMethod.GET,"/").permitAll()
	                                .requestMatchers(HttpMethod.POST,"/registration").permitAll()
	                                .requestMatchers(HttpMethod.GET,"/registration").permitAll()
	                                .requestMatchers(HttpMethod.GET,"/css/**").permitAll()
	                                .requestMatchers(HttpMethod.GET,"/images/**").permitAll()
	                                .requestMatchers(HttpMethod.POST,"/login/**").permitAll()
	                                .requestMatchers(HttpMethod.GET,"/add").hasAnyRole("VENDER")
	                                .requestMatchers(HttpMethod.GET,"/error**").permitAll()
	                                .requestMatchers(HttpMethod.GET,"/edit/{id}").hasAnyRole("VENDER")
	                                .requestMatchers(HttpMethod.POST,"/editTicket").hasAnyRole("VENDER")
	                                .requestMatchers(HttpMethod.POST,"/delete/{id}").hasAnyRole("VENDER")


	                               



	                                
	                                .anyRequest().authenticated())
	                                .formLogin((formlogin) ->
	                                formlogin
	                                .loginPage("/login")
	                                .failureUrl("/login?failed")
	                                .permitAll() 

	                )


	                .logout((logout) ->
	                        logout
	                                .deleteCookies("remove")
	                                .invalidateHttpSession(true)
	                                .logoutUrl("/logout")
	                                .logoutSuccessUrl("/login?logout")
	                                .permitAll()
	                )
	                .exceptionHandling((exceptionHandling) ->
	                        exceptionHandling
	                                .accessDeniedPage("/access-denied"));

	        return http.build();


	    }
	    

	    @Bean
	    public AuthenticationManager authManager(HttpSecurity http,
	                                             PasswordEncoder passwordEncoder) throws Exception {
	        AuthenticationManagerBuilder authenticationManagerBuilder =
	                http.getSharedObject(AuthenticationManagerBuilder.class);
	        authenticationManagerBuilder.userDetailsService(userDetailsService)
	                .passwordEncoder(passwordEncoder);
	        return authenticationManagerBuilder.build();
	    }


	}


