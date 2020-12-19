package com.asiczen.analytics.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.asiczen.analytics.request.VehicleHistoryRequest;
import com.asiczen.analytics.response.ApiResponse;
import com.asiczen.analytics.response.FleetStatusResponse;
import com.asiczen.analytics.response.FleetUsageResponse;
import com.asiczen.analytics.response.LowFuelAlertResponse;
import com.asiczen.analytics.response.OverSpeedVehiclesResponse;
import com.asiczen.analytics.response.UnderSpeedVehiclesResponse;
import com.asiczen.analytics.response.UnderUtilizedVehiclesResponse;
import com.asiczen.analytics.service.MongoAnalyticServices;
import com.asiczen.analytics.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/analytics")
@Slf4j
public class AnalyticsController {

	@Autowired
	MongoAnalyticServices mongoService;

	@Autowired
	RedisService redisService;

	/* Historical data end Point */

	@PostMapping("/history")
	public ResponseEntity<?> getVehicleHistory(@Valid @RequestBody VehicleHistoryRequest request) {

		log.trace("extracting vehicle history");

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponse(HttpStatus.OK.value(), "Vehicle history extracted successfully",
						mongoService.findByvehicleNumberAndTimeStampBetween(request.getVehicleNumber(),	request.getStartDateTime(), request.getEndDateTime())));
	}

	@GetMapping("/lastpositiondtl")
	public ResponseEntity<?> getLastLocation(@Valid @RequestParam String orgRefName) {

		log.trace("Getting last location of all vehicles.");
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
				"Vehicle Last Location extracted successfully", redisService.getLastLocationOfVehicles(orgRefName)));

	}

	// Fleet Status
	@GetMapping("/fleetstatus")
	@ResponseStatus(HttpStatus.OK)
	public FleetStatusResponse getFleetStatus(@Valid @RequestParam String orgRefName) {
		return redisService.getFleetStatus(orgRefName);
	}

	// Fleet Usage
	@GetMapping("/fleetusage")
	@ResponseStatus(HttpStatus.OK)
	public FleetUsageResponse getFleetUsage(@Valid @RequestParam String orgRefName) {
		return redisService.getFleetUsage(orgRefName);
	}

	// Low Fuel Vehicle count
	@GetMapping("/lowfuel")
	@ResponseStatus(HttpStatus.OK)
	public LowFuelAlertResponse getLowFuelVehicles(@Valid @RequestParam String orgRefName) {
		return redisService.getVehiclesWithLowFuel(orgRefName);
	}

	// Under utilized vehicles.
	@GetMapping("/underutilized")
	@ResponseStatus(HttpStatus.OK)
	public UnderUtilizedVehiclesResponse getUnderUtilizedVehicles(@Valid @RequestParam String orgRefName) {
		return redisService.getUnderUtilizedVehicles(orgRefName);

	}

	// Under Speed Vehicles
	@GetMapping("/underspeed")
	@ResponseStatus(HttpStatus.OK)
	public UnderSpeedVehiclesResponse getUnderSpeedVehicles(@Valid @RequestParam String orgRefName) {
		return redisService.underSpeedVehicles(orgRefName);

	}

	// Over Speed Vehicles
	@GetMapping("/overspeed")
	@ResponseStatus(HttpStatus.OK)
	public OverSpeedVehiclesResponse getOverSpeedVehicles(@Valid @RequestParam String orgRefName) {
		return redisService.getOverSpeedingVehicles(orgRefName);

	}

}
