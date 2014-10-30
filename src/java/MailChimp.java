import com.ecwid.mailchimp.MailChimpClient;
import com.ecwid.mailchimp.MailChimpObject;
import com.ecwid.mailchimp.method.v2_0.lists.Email;
import com.ecwid.mailchimp.method.v2_0.lists.MemberInfoData;
import com.ecwid.mailchimp.method.v2_0.lists.MemberInfoMethod;
import com.ecwid.mailchimp.method.v2_0.lists.MemberInfoResult;
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod;
import java.util.Arrays;

public class MailChimp {
	
	public MailChimp() {
	}
	
	public static class MergeVars extends MailChimpObject {

		public String EMAIL, FNAME, LNAME;

		public MergeVars() {
		}

		public MergeVars(String email, String fname, String lname) {
			this.EMAIL = email;
			this.FNAME = fname;
			this.LNAME = lname;
		}
	}
	
	public void subscribeAndCheck(String apikey, String listId, String email) throws Exception{		
		// reuse the same MailChimpClient object whenever possible
		MailChimpClient mailChimpClient = new MailChimpClient();

		// Subscribe a person
		SubscribeMethod subscribeMethod = new SubscribeMethod();
		subscribeMethod.apikey = apikey;
		subscribeMethod.id = listId;
		subscribeMethod.email = new Email();
		subscribeMethod.email.email = email;
		subscribeMethod.double_optin = false;
		subscribeMethod.update_existing = true;
		subscribeMethod.merge_vars = new MergeVars(email, "", "");
		mailChimpClient.execute(subscribeMethod);
		
		System.out.println(email+" has been successfully subscribed to the list. Now will check it...");

		// check his status
		MemberInfoMethod memberInfoMethod = new MemberInfoMethod();
		memberInfoMethod.apikey = apikey;
		memberInfoMethod.id = listId;
		memberInfoMethod.emails = Arrays.asList(subscribeMethod.email);

		MemberInfoResult memberInfoResult = mailChimpClient.execute(memberInfoMethod);
		MemberInfoData data = memberInfoResult.data.get(0);
		System.out.println(data.email + "'s status is " + data.status);

		// Close http-connection when the MailChimpClient object is not needed any longer.
		// Generally the close method should be called from a "finally" block.
		mailChimpClient.close();
	}
	
	public void subscribe(String apikey, String listId, String email, String fname, String lname) throws Exception{		
		// reuse the same MailChimpClient object whenever possible
		MailChimpClient mailChimpClient = new MailChimpClient();

		// Subscribe a person
		SubscribeMethod subscribeMethod = new SubscribeMethod();
		subscribeMethod.apikey = apikey;
		subscribeMethod.id = listId;
		subscribeMethod.email = new Email();
		subscribeMethod.email.email = email;
		subscribeMethod.double_optin = false;
		subscribeMethod.update_existing = true;
		subscribeMethod.merge_vars = new MergeVars(email, fname, lname);
		mailChimpClient.execute(subscribeMethod);
		
		System.out.println(email+" has been successfully subscribed to the list.");

		mailChimpClient.close();
	}

	public static void main(String[] args) throws Exception {
//		thao's list
//		String apikey = "62afd44a23ce9116ec665bb0f2287d7a-us9";		
//		String listId = "da698c7988";
		
//		bokwang centre general list
		String apikey = "cb96bb2b28ee56a84658dccff219b570-us9";		
		String listId = "38f29c5b2f";
	
		String email = "roseskindergarten@gmail.com";
		
		MailChimp mail = new MailChimp();
		mail.subscribe(apikey, listId, email, "thao", "nguyen");
	}
}