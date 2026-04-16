package com.earthquake.service;

import com.earthquake.exception.ApiUnavailableException;
import com.earthquake.model.Earthquake;
import com.earthquake.repository.EarthquakeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EarthquakeService {

    private final EarthquakeRepository repository;
    private final RestTemplate restTemplate;

    // url to USGS API
    private static final String USGS_URL =
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

    public EarthquakeService(EarthquakeRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    //fetch data from USGS API and save in db
    @SuppressWarnings("unchecked")
    public List<Earthquake> fetchAndStoreEarthquakes() {
        Map<String, Object> response;

        // call API
        try {
            response = restTemplate.getForObject(USGS_URL, Map.class);
        } catch (RestClientException e) {
            throw new ApiUnavailableException("USGS API is not available", e);
        }

        //check if the response is valid
        if (response == null || !response.containsKey("features")) {
            throw new RuntimeException("Invalid response from USGS API");
        }

        List<Map<String, Object>> features = (List<Map<String, Object>>) response.get("features");

        //delete old records - no duplicates
        repository.deleteAll();

        List<Earthquake> earthquakes = new ArrayList<>();

        for (Map<String, Object> feature : features) {
            try {
                Map<String, Object> properties = (Map<String, Object>) feature.get("properties");

                if (properties == null) continue;

                //if magnitude < 2: skip
                if (magnitude == null || magnitude <= 2.0) continue;

                Double magnitude = (Double) properties.get("mag");

                // new Earthquake оbject
                Earthquake eq = new Earthquake();
                eq.setId((String) feature.get("id"));
                eq.setMagnitude(magnitude);
                eq.setMagType((String) properties.get("magType"));
                eq.setPlace((String) properties.get("place"));
                eq.setTitle((String) properties.get("title"));
                eq.setTime(((Number) properties.get("time")).longValue());

                // the coordinates are in geometry part on GeoJSON
                Map<String, Object> geometry = (Map<String, Object>) feature.get("geometry");
                if (geometry != null) {
                    List<Double> coords = (List<Double>) geometry.get("coordinates");
                    if (coords != null && coords.size() >= 2) {
                        eq.setLongitude(coords.get(0));
                        eq.setLatitude(coords.get(1));
                    }
                }

                earthquakes.add(eq);

            } catch (Exception e) {
                //if one of the records is invalid, we skip and continue
                System.out.println("Skipping invalid record: " + e.getMessage());
            }
        }

        //save all the earthquakes in databases
        try {
            repository.saveAll(earthquakes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save earthquakes to database", e);
        }

        return earthquakes;
    }

    //returns earthquakes after given timestamp

    public List<Earthquake> getEarthquakesAfter(long timestamp) {
        return repository.findAll()
                .stream()
                .filter(eq -> eq.getTime() > timestamp)
                .toList();
    }

    // AllEarthquakes from db
    public List<Earthquake> getAllEarthquakes() {
        return repository.findAll();
    }

    //delete earthquake with id, throw exception if not found
    public void deleteEarthquake(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Earthquake not found: " + id);
        }
        repository.deleteById(id);
    }
}