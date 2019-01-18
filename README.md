# myRetail RESTful Service

This is an exercice to create an end-to-end Proof-of-Concept for a products API.  Original document included. This solution covers problem one.

	
### Prerequisites

Install: 

Gradle - https://gradle.org/install/  
MangoDB - https://www.mongodb.com/download-center  
Java 1.8 (older version probably work) - https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html  
Postman (recommended) - https://www.getpostman.com/downloads/  
		 
*App only tested on Windows


### Installing


1. Unzip and open the project in your preferred editor  
2. Build project; can be done from terminal in project directory with command "gradle build"  
3. Run the project from your editor or from the terminal with command "java -jar build/libs/my-retail-0.0.1-SNAPSHOT.jar"  

	
## Using the App

The DB is initialized with three products which are removed when the app terminates.  This can be changed by commenting out the code in MyRetailApplication.java under @PreDestroy, and more products can be added above this section as well.

The three available product Ids: '111111', '222222', '333333'

While the app is running you can do the following calls in postman:
GET: 'http://localhost:8080/products/<id>' - Get product with <id>
PUT: 'http://localhost:8080/products/<id>' - Update product with <id>
GET: 'http://localhost:8080/products/redsky/<id>' - Get product from redsky external api; this id works - 13860428


## Running the tests

You can run the tests from the terminal with the command 'gradle test' or from the editor.


## Authors

* **Kyle Gee**
