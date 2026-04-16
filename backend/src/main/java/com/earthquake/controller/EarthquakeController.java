package com.earthquake.controller;

import com.earthquake.model.Earthquake;
import com.earthquake.service.EarthquakeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST Controller - recieve HTTP requests and return the asnwers
@RestController
@RequestMapping("/api/earthquakes")
@CrossOrigin(origins = "*") // allows requests from any domain (for frontend)
public class EarthquakeController {

    private final EarthquakeService service;

    public EarthquakeController(EarthquakeService service) {
        this.service = service;
    }

    // POST /api/earthquakes/fetch - fetch and save USGS API
    @PostMapping("/fetch")
    public ResponseEntity<List<Earthquake>> fetchAndStore() {
        return ResponseEntity.ok(service.fetchAndStoreEarthquakes());
    }

    // GET /api/earthquakes - return all from db
    @GetMapping
    public ResponseEntity<List<Earthquake>> getAll() {
        return ResponseEntity.ok(service.getAllEarthquakes());
    }

    // GET /api/earthquakes/after/{timestamp} - return earthquake with given timestamp
    @GetMapping("/after/{timestamp}")
    public ResponseEntity<List<Earthquake>> getAfter(@PathVariable long timestamp) {
        return ResponseEntity.ok(service.getEarthquakesAfter(timestamp));
    }

    // DELETE /api/earthquakes/{id} - delete earthquake for ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.deleteEarthquake(id);
        return ResponseEntity.ok("Earthquake deleted successfully");
    }
}