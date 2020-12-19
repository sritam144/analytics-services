package com.asiczen.analytics.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.asiczen.analytics.response.FleetStatusResponse;
import com.asiczen.analytics.response.FleetUsageResponse;
import com.asiczen.analytics.response.LowFuelAlertResponse;
import com.asiczen.analytics.response.OverSpeedVehiclesResponse;
import com.asiczen.analytics.response.UnderSpeedVehiclesResponse;
import com.asiczen.analytics.response.UnderUtilizedVehiclesResponse;
import com.asiczen.analytics.response.VehicleLastLocResponse;

@Service
public interface RedisService {

	public List<VehicleLastLocResponse> getLastLocationOfVehicles(String orgRefName);

	public FleetStatusResponse getFleetStatus(String orgRefName);

	public FleetUsageResponse getFleetUsage(String orgRefName);

	public LowFuelAlertResponse getVehiclesWithLowFuel(String orgRefName);
	
	public UnderUtilizedVehiclesResponse getUnderUtilizedVehicles(String orgRefName);
	
	public OverSpeedVehiclesResponse getOverSpeedingVehicles(String orgRefName);
	
	public UnderSpeedVehiclesResponse underSpeedVehicles(String orgRefName);
}
