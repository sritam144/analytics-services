package com.asiczen.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDistancevsDate {

	private String vehicleNumber;
	private int distance;

	private String timeStamp;
}
