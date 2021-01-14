package com.asiczen.analytics.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class MongoAnalyticServicesImpl implements MongoAnalyticServices {

    @Autowired
    GeoMessageRepository messageRepo;

    @Override
    @Transactional(readOnly = true)
    public VehicleHistoryResponse findByVehicleNumberAndTimeStampBetween(String vehicleNumber, LocalDateTime startTime, LocalDateTime endTime) {

        log.trace("Start time {} ", startTime.toString());
        log.trace("End TIme {} ", endTime.toString());

        Optional<List<GeoMessage>> geoMessages = messageRepo.findByVehicleNumberAndTimestampBetween(vehicleNumber, startTime, endTime);

        VehicleHistoryResponse response = new VehicleHistoryResponse();
        List<Location> locationList = new ArrayList<>();

        log.trace("Looping on data set Received.");

        if (geoMessages.isPresent()) {
            response.setVehicleNumber(vehicleNumber);

            log.info("Some data are present so will process the data");

            locationList = geoMessages.get()
                    .stream()
                    .map(geoMessage -> new Location(geoMessage.getLat(), geoMessage.getLng(), geoMessage.getTimestamp()))
                    .collect(Collectors.toList());

            response.setLocationlist(locationList);

            response.setTimeStamp(new Date());


            geoMessages.get().stream().forEach(record -> log.info("Speed {} {} {}", record.getSpeed(), record.getCalulatedDistance(), record.getFuel()));


//            try {
//                double averageSpeed = geoMessages.get().stream()
//                        .filter(record -> (record.getSpeed() > 0d))
//                        .map(record -> record.getSpeed())
//                        .mapToInt(intValue -> intValue)
//                        .average()
//                        .getAsDouble();
//                response.setAvgSpeed(averageSpeed);
//
//                double totalDistance = geoMessages.get().stream()
//                        .filter(record -> record.getCalulatedDistance() > 0d)
//                        .map(record -> record.getCalulatedDistance())
//                        .mapToDouble(r -> r)
//                        .sum();
//
//                response.setAvgmilage(0);
//                response.setTotalDistance(Math.abs(totalDistance));
//            } catch (Exception e) {
//                log.error("Error  while getting historical data");
//                log.error(">>>>>>>>>>>>> {}", e.getLocalizedMessage());
//                e.printStackTrace();
//            }
            return response;

        } else {
            throw new ResourceNotFoundException("No data exists for vehicle number {} " + vehicleNumber);
        }


    }

}
