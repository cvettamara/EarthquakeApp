**Earthquake App**

A full-stack app that pulls real-time earthquake data from the USGS API, stores it in a PostgreSQL database, and displays it on an interactive map via a React frontend.

**Tech Stack**

Backend: Java 21, Spring Boot, Spring Data JPA, Hibernate, PostgreSQL, Maven
Frontend: React.js, Axios, Bootstrap, Leaflet
External API: USGS Earthquake API (GeoJSON, real-time)

**Prerequisites**

Java 21+
PostgreSQL
Maven
Node.js + npm


**Database Setup**
Create the database in PostgreSQL:
sqlCREATE DATABASE earthquake_database;
Then update your application.properties:
propertiesspring.datasource.url=jdbc:postgresql://localhost:5432/earthquake_database
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

Tables are created automatically on first run — no manual SQL needed.


**Running the Backend**
bashcd backend
mvn spring-boot:run
Backend will be running at http://localhost:8080.

**Running the Frontend**
bashcd frontend
npm install
npm start
Frontend will be running at http://localhost:3000.

**How to Use**
Once both are running:

Click Fetch New — pulls the latest earthquakes from the USGS API and saves them to the database
Click Show All — loads all saved earthquakes from the database
Pick a date and click Filter — shows only earthquakes after that date
Click Delete on any row to remove that record

Earthquakes are displayed both in a table and on an interactive map.

**API Endpoints**
Useful if you want to test things manually in Postman:
POST - /api/earthquakes/fetch - Fetches and saves new earthquakes
GET - /api/earthquakes - Returns all earthquakes
GET - /api/earthquakes/after/{timestamp} - Returns earthquakes after a given timestamp
DELETE - /api/earthquakes/{id} - Deletes an earthquake by ID

**Notes & Assumptions**

The app fetches earthquakes from the last hour only (USGS all_hour feed)
Every time you click "Fetch New", old records are deleted and replaced with fresh ones — this avoids duplicates
A magnitude filter (magnitude > 2.0) is implemented but can easily be disabled in EarthquakeService.java if you want to see all events
Timestamps are stored as Unix time in milliseconds


**Possible Improvements**

Auto-refresh — add a @Scheduled job in Spring to automatically pull new data every X minutes
Marker colors — color the map markers based on magnitude (green / yellow / red)
Magnitude threshold — let the user set a custom minimum magnitude filter from the UI
