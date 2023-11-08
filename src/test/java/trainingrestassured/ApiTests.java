package trainingrestassured;

import org.junit.jupiter.api.Test;

import io.restassured.response.ResponseBodyExtractionOptions;
import models.Product;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertSame;

import org.hamcrest.Matcher;
import org.hamcrest.Matcher.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.MatcherAssert.*;

public class ApiTests {
	@Test
	public void getCategories() {
		String endpoint = "http://localhost:80/api_testing/category/read.php";
		var response = given().when().get(endpoint).then();
		response.log().body();
	}

	@Test
	public void getProduct() {
		String endpoint = "http://localhost:80/api_testing/product/read_one.php";
				given().queryParam("id", 2)
				.when().get(endpoint)
				.then()
					.assertThat().
					//verify response fields in body
						statusCode(200).
						body("id", equalTo("2")).
						body("name",equalTo("Cross-Back Training Tank")).
						body("description",equalTo("The most awesome phone of 2013!")).
						body("price",equalTo("299.00")).
						body("category_id",equalTo(2)).
						body("category_name",equalTo("Active Wear - Women"));
	}

	@Test
	public void createProduct() {
		String endpoint = "http://localhost:80/api_testing/product/create.php";
		String body = """
				{
				"name": "Water Bottle",
				"description": "Blue water bottle. Holds 64 ounces",
				"price": 12,
				"category_id": 3
				}
				""";
		var response = 
				given().body(body)
				.when().post(endpoint)
				.then();
		//show response body in console
		response.log().body();
	}

	@Test
	public void updateProduct() {
		String endpoint = "http://localhost:80/api_testing/product/update.php";
		String body = """
				{
				"id": 1000,
				"name": "Water Bottle",
				"description": "Blue water bottle. Holds 64 ounces",
				"price": 15,
				"category_id": 3
				 }
				""";
		 var response = 
				 given().body(body)
				 .when().put(endpoint)
				 .then(); 
		 response.log().body();
	}
	
	@Test
	public void deleteProduct() {
		String endpoint = "http://localhost:80/api_testing/product/delete.php";
		String body = """
				{
				"id": 1001
				 }
				""";
		 var response = 
				 given().body(body)
				 .when().delete(endpoint)
				 .then();
		response.log().body();
	}
	
	@Test
	public void createSerializedProduct() {
		String endpoint = "http://localhost:80/api_testing/product/create.php";
		//Import Class
		Product product = new Product(
				"New Sweatband",
				"New Sweatband product",
				200,
				3				
				);
		var response = 
				given().body(product)
				.when().post(endpoint)
				.then();
		response.log().body();
	}
	@Test
	public void updateSerializedProduct() {
		String endpoint = "http://localhost:80/api_testing/product/update.php";
		//Import Class
		Product product = new Product(
				1006,
				"Sweatband",
				"Sweatband product",
				6,
				3				
				);
		var response = 
				given().body(product)
				.when().put(endpoint)
				.then();
		response.log().body();
	}
	
	@Test
	public void getSerializedProduct() {
		String endpoint = "http://localhost:80/api_testing/product/read_one.php";
		var response = 
				given().queryParam("id", 1005)
				.when().get(endpoint)
				.then();
		response.log().body();
	}
	
	@Test
	public void deleteSerializedProduct() {
		String endpoint = "http://localhost:80/api_testing/product/delete.php";
		//Import Class
		Product product = new Product(
				1006				
				);
		var response = 
				given().body(product)
				.when().delete(endpoint)
				.then();
		response.log().body();
	}
	
	@Test
	public void readAllProducts() {
		String endpoint = "http://localhost:80/api_testing/product/read.php";
		given()
		.when().get(endpoint)
		.then()
			.log()
				.body()
				.assertThat()
					.statusCode(200)
					.body("records.size()", equalTo(23));
	}
	
	@Test
	public void readAllTheProductsBodyLogCheck() {
		String endpoint = "http://localhost:80/api_testing/product/read.php";
		given()
		.when().get(endpoint)
		.then()
			.log()
				//Verify fields in response body
				.body()
				.assertThat()
					.statusCode(200)
					.body("records.size()",greaterThan(0))
					.body("records.id",everyItem(notNullValue()))
					.body("records.name",everyItem(notNullValue()))
					.body("records.description",everyItem(notNullValue()))
					.body("records.price",everyItem(notNullValue()))
					.body("records.category_id",everyItem(notNullValue()))
					.body("records.category_name",everyItem(notNullValue()))
					.body("records.id[0]",equalTo(1004));
	}
	@Test
	public void readAllTheProductsHeadersLogCheck() {
		String endpoint = "http://localhost:80/api_testing/product/read.php";
		given()
		.when().get(endpoint)
		.then()
			.log()
				//Verify header response
				.headers()
				.assertThat()
					.statusCode(200)
					.header("Content-Type", equalTo("application/json; charset=UTF-8"))
					.body("records.size()",greaterThan(0))
					.body("records.id",everyItem(notNullValue()))
					.body("records.name",everyItem(notNullValue()))
					.body("records.description",everyItem(notNullValue()))
					.body("records.price",everyItem(notNullValue()))
					.body("records.category_id",everyItem(notNullValue()))
					.body("records.category_name",everyItem(notNullValue()))
					.body("records.id[0]",equalTo(1007));
	}
	
	@Test
	public void getDeserializedProduct() {
		String endpoint = "http://localhost:80/api_testing/product/read_one.php";
		given().
			queryParam("id", "2").
		when().
			get(endpoint).
			// we want get the response in Java Object
			as(Product.class);
	}
	
	@Test
    public void getDeserializedProduct2(){
        String endpoint = "http://localhost:80/api_testing/product/read_one.php";
        Product expectedProduct = new Product(
                2,
                "Cross-Back Training Tank",
                "The most awesome phone of 2013!",
                299.00,
                2,
                "Active Wear - Women"
        );

        Product actualProduct =
            given().
                queryParam("id", "2").
            when().
                get(endpoint).
                    as(Product.class);
        System.out.println(actualProduct.getCategory_name());
        System.out.println(expectedProduct.getCategory_name());
        //Compare two objects property
        MatcherAssert.assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
    }
	@Test
	public void getMultivitamin() {
		String endpoint = "http://localhost:80/api_testing/product/read_one.php";
		//var response = 
				given().
					queryParam("id", 2).
				when().get(endpoint).
				then().
					//log().
				 	//headers().
				 	//body(). ... no need to add
					log().body().
					assertThat().
					statusCode(200).
					header("Content-Type",equalTo("application/json")).
					body("id",equalTo("2")).
					body("name",equalTo("Cross-Back Training Tank")).
					body("description",equalTo("The most awesome phone of 2013!")).
					body("price",equalTo("299.00")).
					body("category_id",equalTo(2)).
					body("category_name",equalTo("Active Wear - Women"));
		
		//response.log().body();
	}
	
	@Test
	public void getAsingleProduct() {
		String endpoint = "http://localhost:80/api_testing/product/read_one.php";
		var response = 
				given().queryParam("id", 2)
				.when().get(endpoint)
				.then();
		response.log().body();
	}
	
	
}
