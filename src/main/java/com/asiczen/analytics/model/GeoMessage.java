package com.asiczen.analytics.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "vehiclemessagesprod")
public class GeoMessage {

    private String vehicleNumber;
    private String vehicleType;
    private String imeiNumber;
    private LocalDateTime timestamp;
    private int unplugged;
    private int fuel;
    private int speed;

    private double lat;
    private double lng;

    private double calulatedDistance;
}
