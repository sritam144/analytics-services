package com.asiczen.analytics.response;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetUsageResponse {

	private int totalDistance;
	private int average;
	private Set<VehicleDistanceMatrix> matrix;
	
	private Date timeStamp;
}
