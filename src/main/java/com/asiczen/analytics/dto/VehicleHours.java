package com.asiczen.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleHours {

	public String vehicleNumber;
	public int idleHourEngineOn;
	public int idleHourEngineOff;
	public int movingEngineOn;
}
