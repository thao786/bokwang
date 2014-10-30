import org.apache.commons.validator.routines.EmailValidator;

public class EmailCheck {
	public EmailCheck(){
	}
	
	public boolean emailCheck(String email){
		EmailValidator emailValidator=EmailValidator.getInstance();
		
		if (emailValidator.isValid(email)) {
			  return true;
		}
		
		return false;
	}

	public static void main(String[] args) {
		EmailCheck ec = new EmailCheck();
		
		System.out.print(ec.emailCheck("gfds@ga.ca"));
	}

}
