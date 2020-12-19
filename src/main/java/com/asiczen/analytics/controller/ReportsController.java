package com.asiczen.analytics.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.asiczen.analytics.dto.VehicleDistanceAverage;
import com.asiczen.analytics.dto.VehiclevsHours;
import com.asiczen.analytics.request.OrgLevelRequest;
import com.asiczen.analytics.response.VehicleDistancevsDate;
import com.asiczen.analytics.service.EndOfDayAnalyticsServices;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/analytics")
@Slf4j
public class ReportsController {

	@Autowired
	EndOfDayAnalyticsServices service;

	@PostMapping("/distancevehicles")
	@ResponseStatus(HttpStatus.OK)
	public List<VehicleDistancevsDate> getVehicleDistanceGraph(@Valid @RequestBody OrgLevelRequest request) {
		return service.getVehiclevsDistancevsDate(request);
	}

	@PostMapping("/distancevehiclesavg")
	@ResponseStatus(HttpStatus.OK)
	public List<VehicleDistanceAverage> getVehicleDistanceAverageGraph(@Valid @RequestBody OrgLevelRequest request) {
		return service.getVehicleDistancevsAvrageDistanceOverDateRange(request);
	}

	@PostMapping("/vehiclevshours")
	@ResponseStatus(HttpStatus.OK)
	public List<VehiclevsHours> getVehicleidleRunningHours(@Valid @RequestBody OrgLevelRequest request) {
		return service.getVehicleHours(request);
	}

}
