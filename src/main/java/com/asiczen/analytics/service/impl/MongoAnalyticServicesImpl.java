package com.asiczen.analytics.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.mongodb.client.model.geojson.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
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
    public VehicleHistoryResponse findByvehicleNumberAndTimeStampBetween(String vehicleNumber, LocalDateTime startTime, LocalDateTime endTime) {

        log.trace("Start time {} ", startTime.toString());
        log.trace("End TIme {} ", endTime.toString());

        Optional<List<GeoMessage>> geoMessages = messageRepo.findByvehicleNumberAndTimeStampBetween(vehicleNumber, startTime, endTime);

        VehicleHistoryResponse response = new VehicleHistoryResponse();
        List<Location> locationList = new ArrayList<>();

        log.info("Getting vehicle info from mongo");

        if (geoMessages.isPresent()) {
            response.setVehicleNumber(vehicleNumber);

            locationList = geoMessages.get().stream()
                    .map(geoMessage -> new Location(geoMessage.getLocation().getCoordinates().get(0), geoMessage.getLocation().getCoordinates().get(1)))
                    .collect(Collectors.toList());

            double averageSpeed = geoMessages.get().stream()
                    .filter(record -> (record.getSpeed() > 0))
                    .map(record -> record.getSpeed())
                    .mapToInt(intValue -> intValue).average().getAsDouble();

            double totalDistance = geoMessages.get().stream()
                    .filter(record -> record.getCalulatedDistance() > 0)
                    .map(record -> record.getCalulatedDistance())
                    .mapToDouble(r -> r).sum();


            response.setAvgSpeed(averageSpeed);
            response.setLocationlist(locationList);


            response.setAvgmilage(0);
            response.setTotalDistance(Math.abs(totalDistance));
            return response;

        } else {
            throw new ResourceNotFoundException("No data exists for vehicle number {} " + vehicleNumber);
        }


    }

}
