package com.asiczen.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleFuelMatrix {

	private String vehicleNumber;
	private String driverName;
	private Integer fuel;

	private String timeStamp;
}
