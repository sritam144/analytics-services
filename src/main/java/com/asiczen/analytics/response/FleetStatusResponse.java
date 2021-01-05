package com.asiczen.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetStatusResponse {

	private Long totalVehicle;
	private Long runningVehicle;
	private Long idle;
	private Long stopped;
	private Long inactive;
	private Long noData;
	
	private Date timeStamp;
}
