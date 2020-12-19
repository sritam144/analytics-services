package com.asiczen.analytics.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleHistoryResponse {

	private String vehicleNumber;
	private double avgSpeed;
	private double avgmilage;
	private double totalDistance;
	private List<Location> locationlist;

	private String timeStamp;
}
