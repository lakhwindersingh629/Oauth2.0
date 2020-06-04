package GrantType.google.auth;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import Pojo.GetCourse;
import Pojo.WebAutomation;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class AuthorizationCode {

	public static void main(String[] args) throws InterruptedException {

		// Authrization code

//		WebDriver driver;
//
//		System.setProperty("webdriver.chrome.driver", ".//lib//chromedriver");
//		driver = new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.get(
//				"https://accounts.google.com/o/oauth2/v2/auth?"
//				+ "scope=https://www.googleapis.com/auth/userinfo.email&"
//				+ "auth_url=https://accounts.google.com/o/oauth2/v2/auth&"
//				+ "client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&"
//				+ "redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//
//		
//	driver.findElement(By.xpath("//input[@type='email']")).sendKeys("dynamite.fb6@gmail.com");
//	driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.RETURN);
//	Thread.sleep(3000);
//	driver.findElement(By.xpath("//input[@name='password']")).sendKeys("Loyalty01");
//	driver.findElement(By.xpath("//input[@type='password']")).sendKeys(Keys.RETURN);
//	String url= driver.getCurrentUrl();

		// per google new update in 2020 you are not able to automate auth code in
		// browser so comment above code
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AFbYbNMfAJaCGmXhhDZB6GqM7K9iYULZrXjk1J1I0WShIAeGTkejKj_-1FU6hog4ZJLtahXtgqs8Nf6Vos8otU&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=1&prompt=none#";

		String partialcode = url.split("code=")[1];
		String auth_code = partialcode.split("&scope")[0];
		System.out.println("Authrization code is :- " + auth_code);

		// get Access token
		String access_token_response = given().urlEncodingEnabled(false) // stop encoding of special character in auth
																			// code like %
				.queryParams("code", auth_code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();

		// get access token from JSON
		JsonPath js = new JsonPath(access_token_response);
		String access_token = js.getString("access_token");

		System.out.println("access token :- " + access_token);
		// get resource and put response in GETCOURSE (pojo) object
		GetCourse gc = given().queryParam("access_token", access_token).expect().defaultParser(Parser.JSON) // expect
																											// json as
																											// response
				.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);// end point

		// playing with response
		System.out.println(gc.getExpertise());

		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());

		List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();

		ArrayList<String> response_coursesTitle = new ArrayList<String>();
		
		// iterate all the course title of web automation
		for (WebAutomation i : webAutomationCourses) {

			/* add all the course title of web automation in the Arraylist 
			to compare with expected course title */
			response_coursesTitle.add(i.getCourseTitle());
			
			
			
			// if course title match than show the price
			if (i.getCourseTitle().equalsIgnoreCase("Protractor")) {

				// print price of Protractor courses
				System.out.println("price is :- " + i.getPrice());
			}

		}
		
		//expected course title of web automation 
		String[] courseTitle = {"Selenium Webdriver Java", "Cypress", "Protractor"};
		
		//convert array to arraylist for comparing the expected and response coursetitle
		List<String> actualTitle= Arrays.asList(courseTitle); 
		
		
		//compare two arraylist
	  	Assert.assertTrue(response_coursesTitle.equals(actualTitle));
	  	

	}

}
