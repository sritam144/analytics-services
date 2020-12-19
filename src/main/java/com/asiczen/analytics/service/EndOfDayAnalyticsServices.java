package com.asiczen.analytics.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.asiczen.analytics.dto.VehicleDistanceAverage;
import com.asiczen.analytics.dto.VehiclevsHours;
import com.asiczen.analytics.request.OrgLevelRequest;
import com.asiczen.analytics.response.MessageCounter;
import com.asiczen.analytics.response.VehicleDistancevsDate;

@Service
public interface EndOfDayAnalyticsServices {

	List<VehicleDistancevsDate> getVehiclevsDistancevsDate(OrgLevelRequest request);

	List<VehicleDistanceAverage> getVehicleDistancevsAvrageDistanceOverDateRange(OrgLevelRequest request);

	List<VehiclevsHours> getVehicleHours(OrgLevelRequest request);
	
	List<MessageCounter> getMessageCounter(OrgLevelRequest request);
}
