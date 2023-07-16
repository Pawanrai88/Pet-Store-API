package api.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.payloads.User;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import api.endpoints.UsersEndPoints;


public class UserTest {
	
	Faker fake;
	User userPayload;
	JsonPath js;
    public Logger logger;
	
	@BeforeClass
	public void setup() {
		
		fake=new Faker();
		userPayload=new User();
		userPayload.setId(fake.idNumber().hashCode());
		userPayload.setUsername(fake.name().username());
		userPayload.setFirstName(fake.name().firstName());
		userPayload.setLastName(fake.name().lastName());
		userPayload.setEmail(fake.internet().emailAddress());
		userPayload.setPassword(fake.internet().password());
		userPayload.setPhone(fake.phoneNumber().phoneNumber());
		userPayload.setUserStatus(fake.number().numberBetween(0, 3));
		
		PropertyConfigurator.configure("C:\\Users\\12363\\eclipse-workspace\\PetStore_APIAutomation\\src\\test\\resources\\Config\\log4j.properties");
		logger=LogManager.getLogger(this.getClass());
	}
	
	@Test(priority=1)
	public void testPostUser() {
		logger.info("****** Creating User*******");
		Response response=UsersEndPoints.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("******User is created*******");
	}

	@Test(priority=2,dependsOnMethods="testPostUser")
	public void testGetUser() {
		logger.info("******Retrive the data for created user*******");
		Response response=UsersEndPoints.getUser(this.userPayload.getUsername());
		String api_response=response.then().log().all().extract().response().asString();
		js=new JsonPath(api_response);
		String response_Username=js.get("username");
		System.out.println(response_Username);
		Assert.assertEquals(response.getStatusCode(),200);
		Assert.assertEquals(response_Username,this.userPayload.getUsername());
		logger.info("******Data is retireved ******");
	}
	
	@Test(priority=3,dependsOnMethods="testPostUser")
	public void testUpdateUser() {
		logger.info("******Updating user First name, Last name and Email*******");
		userPayload.setFirstName(fake.name().firstName());
		userPayload.setLastName(fake.name().lastName());
		userPayload.setEmail(fake.internet().emailAddress());
		
		Response response=UsersEndPoints.updateUser(userPayload,this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("******First name, Last name and Email are updated*******");
	}
	
	@Test(priority=4,dependsOnMethods="testPostUser")
	public void testDeleteUser() {	
		logger.info("******Delete the newly created User*******");
		Response response=UsersEndPoints.deleteUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("******User is Deleted *******");
	}
}
