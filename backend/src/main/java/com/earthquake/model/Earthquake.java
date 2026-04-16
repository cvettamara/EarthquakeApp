package com.earthquake.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Entity class -> earthquakes in databases
@Entity
@Table(name = "earthquakes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Earthquake {

    @Id
    private String id;
    private Double magnitude;
    private String magType;
    private String place;
    private String title;
    private Long time;
    private Double longitude;
    private Double latitude;
}