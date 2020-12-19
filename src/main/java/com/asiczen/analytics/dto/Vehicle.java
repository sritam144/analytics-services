package com.asiczen.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vehicle {

	private String vehicleNumber;
	private String imeiNumber;
	private double lng;
	private double lat;
	private String vehicleType;
	private String lastTime;
	private String driverName;
	private String driverContact;
	private String orgRefName;

	private double totalDistanceDaily;
	private double topSpeed;
}
