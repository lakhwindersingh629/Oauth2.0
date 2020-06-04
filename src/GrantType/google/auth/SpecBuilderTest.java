package GrantType.google.auth;


import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoFor.serializeclass.Location;
import pojoFor.serializeclass.SetLocationBody;

public class SpecBuilderTest {

	public static void main(String[] args) throws InterruptedException {

		
		RestAssured.baseURI = "https://rahulshettyacademy.com";

		
		/* 
		 * creating a JSON Payload for body
		 * adding all the keys and value of Json
		 * to POJO classes
		 * 
		 */
		
		// object for pojo class
		SetLocationBody addplace = new SetLocationBody();

		
		addplace.setAccuracy(50);
		addplace.setAddress("29, side layout, cohen 09");
		addplace.setLanaguage("French-IN");
		addplace.setPhone_number("(+91) 983 893 3937");
		addplace.setWebsite("manlaap.com");
		addplace.setName("lakhwinder singh");

		
		//created a arraylist for types key
		
		List<String> myList = new ArrayList<String>();
		myList.add("show park");
		myList.add("shop");
		addplace.setTypes(myList);

		
		//Creating object for nested json Location class
		
		Location locations = new Location();
		
		//set values for nested Json
		locations.setLat(-38.383494);
		locations.setLng(33.427362);
		
		
		//set nested json value to parent json
		addplace.setLocation(locations);
		
		//implement spec builder
		RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON)
		.build();
		
		
		ResponseSpecification responsespec =new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		
		//disconnect given method from when 
		RequestSpecification res = given().spec(req)
				.urlEncodingEnabled(false) 
				.body(addplace);
		
			Response response =	res.when()
				.post("/maps/api/place/add/json")
				.then().spec(responsespec)
				.extract().response(); //extracted json as String 

			
			
		System.out.println(response.asString());


	}

}
