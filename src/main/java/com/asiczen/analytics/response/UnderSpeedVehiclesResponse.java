package com.asiczen.analytics.response;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UnderSpeedVehiclesResponse {

	private int count;
	private Set<VehicleSpeedMatrix> vehicleList;
	private int underSpeedLimit=0;

	private Date timeStamp;
}
