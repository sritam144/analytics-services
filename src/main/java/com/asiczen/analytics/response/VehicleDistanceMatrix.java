package com.asiczen.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDistanceMatrix {

	private String vehicleNumber;
	private String driverName;
	private int distance;

	private Date timeStamp;
}
