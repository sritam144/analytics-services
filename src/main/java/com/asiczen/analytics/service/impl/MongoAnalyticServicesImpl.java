package com.asiczen.analytics.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    public VehicleHistoryResponse findByVehicleNumberAndTimeStampBetween(String vehicleNumber, Date startTime, Date endTime) {

        log.info("Start time {} ", startTime.toString());
        log.info("End TIme {} ", endTime.toString());

        Optional<List<GeoMessage>> geoMessages = messageRepo.findByVehicleNumberAndTimestampBetween(vehicleNumber, startTime, endTime);

        VehicleHistoryResponse response = new VehicleHistoryResponse();

        log.trace("Looping on data set Received.");

        if (geoMessages.isPresent()) {
            response.setVehicleNumber(vehicleNumber);

            log.info("Some data are present so will process the data");

            List<Location> locationList = geoMessages.get()
                    .stream()
                    .map(geoMessage -> new Location(geoMessage.getLat(), geoMessage.getLng(), geoMessage.getTimestamp()))
                    .collect(Collectors.toList());

            response.setLocationlist(locationList);

            response.setTimeStamp(new Date());


            geoMessages.get().stream().forEach(record -> log.info("Speed {} {} {}", record.getSpeed(), record.getCalulatedDistance(), record.getFuel()));

            return response;

        } else {
            throw new ResourceNotFoundException("No data exists for vehicle number {} " + vehicleNumber);
        }


    }

}
