package com.asiczen.analytics.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.asiczen.analytics.model.GeoMessage;

@Repository
public interface GeoMessageRepository extends MongoRepository<GeoMessage, String> {

	Optional<List<GeoMessage>> findByVehicleNumberAndTimestampBetween(String vehicleNumber, Date startTime, Date endTime);

	Optional<List<GeoMessage>> findByVehicleNumber(String vehicleNumber);

}
