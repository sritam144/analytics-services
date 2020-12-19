package com.asiczen.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDistanceAverage {

	private String vehicleNumber;
	private Integer totalDistance;
	private Integer averageDistance;
}
