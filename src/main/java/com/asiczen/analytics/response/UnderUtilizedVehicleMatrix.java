package com.asiczen.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UnderUtilizedVehicleMatrix {

	private String vehicleNumber;
	private String driverName;
	private double distanceTraveled;

	private String timeStamp;
}
