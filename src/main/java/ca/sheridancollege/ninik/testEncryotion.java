package ca.sheridancollege.ninik;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class testEncryotion {

    public static String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);

    }


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String password = "123";
		String enpassword = encryptPassword(password);
		System.out.print(enpassword);


	}
  
}