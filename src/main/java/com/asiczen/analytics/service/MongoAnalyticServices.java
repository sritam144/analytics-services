package com.asiczen.analytics.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.asiczen.analytics.response.VehicleHistoryResponse;

@Service
public interface MongoAnalyticServices {

	//Find the vehicle geo data for a date range
	public VehicleHistoryResponse findByVehicleNumberAndTimeStampBetween(String vehicleNumber, LocalDateTime startTime, LocalDateTime endTime);

}
