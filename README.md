# Introduction

This project is extended from [Dropwizard example application] (https://github.com/dropwizard/dropwizard/tree/master/dropwizard-example).
Following additional REST APIs are implemented
* Create new user profiles
* View user profile
* Check who visited a particular user profile

# Running The Application

To test the example application run the following commands.

* To package the example run.

        mvn package

* To setup the h2 database run.

        java -jar target/assignment-1.0.0-SNAPSHOT.jar db migrate example.yml

* To run the server run.

        java -jar target/assignment-1.0.0-SNAPSHOT.jar server example.yml

* Create new user profiles. You can use postman Rest client also.

	    curl -H "Content-Type: application/json" -X POST -d '{"fullName":"Person1","jobTitle":"Title1"}' http://localhost:8080/people

* View user profile.

	    http://localhost:8080/people/{visitorPersonId}/{personIdToVisit}

* Check who visited a particular user profile.

	    http://localhost:8080/visitors/{visitedPersonId}

# Few design decisions:

* Used simple schema with two tables ```Person``` and ```ProfileView```. ```Person``` has the Person details. ```ProfileView``` to store the profile visits.
* Used meaningful naming conventions to avoid explicit comments in the code.
* Cleanup job which runs every half hour and remove profileViews older than 10 days is required since application do not require that data.
* Added index on the visitedBy column to make the visitors link work faster.
* H2 in-memory database is used to be able to run app without local database.

