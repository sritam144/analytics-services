package com.asiczen.analytics.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OverSpeedVehiclesResponse {

	private int count;
	private Set<VehicleSpeedMatrix> vehicleList;
	private String timeStamp;
}
