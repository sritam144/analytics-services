package com.asiczen.analytics.service;

import com.asiczen.analytics.dto.VehicleHours;
import com.asiczen.analytics.response.*;
import java.util.List;
import org.springframework.stereotype.Service;

import com.asiczen.analytics.dto.VehicleDistanceAverage;
import com.asiczen.analytics.request.OrgLevelRequest;

@Service
public interface EndOfDayAnalyticsServices {

    List<VehicleDistancevsDate> getVehiclevsDistancevsDate(OrgLevelRequest request);

    List<VehicleDistanceAverage> getVehicleDistancevsAvrageDistanceOverDateRange(OrgLevelRequest request);

    List<VehicleHours> getVehicleHours(OrgLevelRequest request);

    List<MessageCounter> getMessageCounter(OrgLevelRequest request);

    List<VehicleStatusCounter> getVehicleStatusCountGroupByDates(OrgLevelRequest request);

    List<VehicleActiveDistanceDTO> getActiveVehicleVsDistanceGroupByDate(OrgLevelRequest request);
}
