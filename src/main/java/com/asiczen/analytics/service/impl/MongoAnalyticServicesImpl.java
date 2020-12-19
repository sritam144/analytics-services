package com.asiczen.analytics.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiczen.analytics.exception.ResourceNotFoundException;
import com.asiczen.analytics.model.GeoMessage;
import com.asiczen.analytics.repository.GeoMessageRepository;
import com.asiczen.analytics.response.Location;
import com.asiczen.analytics.response.VehicleHistoryResponse;
import com.asiczen.analytics.service.MongoAnalyticServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MongoAnalyticServicesImpl implements MongoAnalyticServices {

	@Autowired
	GeoMessageRepository messageRepo;

	@Override
	public VehicleHistoryResponse findByvehicleNumberAndTimeStampBetween(String vehicleNumber, LocalDateTime startTime,
			LocalDateTime endTime) {

		log.trace("Strat time {} ", startTime.toString());
		log.trace("End TIme {} ", endTime.toString());

		Optional<List<GeoMessage>> geoMessages = messageRepo.findByvehicleNumberAndTimeStampBetween(vehicleNumber,
				startTime, endTime);

		VehicleHistoryResponse response = new VehicleHistoryResponse();
		List<Location> locationList = new ArrayList<>();

		log.info("Getting vehicle info from mongo");

		if (geoMessages.isPresent()) {
			response.setVehicleNumber(vehicleNumber);
			geoMessages.get().forEach(item -> {
				Location loc = new Location(item.getLocation().getX(), item.getLocation().getY());
				locationList.add(loc);
			});

			response.setLocationlist(locationList);
			response.setAvgSpeed(0);
			response.setAvgmilage(0);
			response.setTotalDistance(228.56);
			return response;

		} else {
			throw new ResourceNotFoundException("No data exists for vehicle number {} " + vehicleNumber);
		}
	}

}
