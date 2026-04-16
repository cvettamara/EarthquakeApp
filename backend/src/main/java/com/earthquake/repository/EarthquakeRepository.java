package com.earthquake.repository;

import com.earthquake.model.Earthquake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//interface for working with database
//JpaRepository gives us: save(), findAll(), findById(), deleteById()
@Repository
public interface EarthquakeRepository extends JpaRepository<Earthquake, String> {
}
//String - the type of Id  in Earthquake class