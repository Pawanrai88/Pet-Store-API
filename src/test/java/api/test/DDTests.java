package api.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.endpoints.UsersEndPoints;
import api.payloads.User;
import api.utility.DataProviders;
import io.restassured.response.Response;

public class DDTests {
	
	
	@Test(priority=1,dataProvider="Data",dataProviderClass=DataProviders.class)
	public void testCreateUser(String UId, String username,String fname, String Lname, String email, String pwd, String ph) {
		
		{ 
			User userPayload=new User();
			userPayload.setId(Integer.parseInt(UId));
			userPayload.setUsername(username);
			userPayload.setFirstName(fname);
			userPayload.setLastName(Lname);
			userPayload.setEmail(email);
			userPayload.setPassword(pwd);
			userPayload.setPhone(ph);
		
		Response response=UsersEndPoints.createUser(userPayload);
		Assert.assertEquals(response.getStatusCode(),200);
		
	}
	}
		
		@Test(priority=2,dataProvider="UserNames",dataProviderClass=DataProviders.class,dependsOnMethods="testCreateUser")
		public void testDeleteUser(String username) {
				
			Response response=UsersEndPoints.deleteUser(username);
			Assert.assertEquals(response.getStatusCode(),200);
			
		}

}
