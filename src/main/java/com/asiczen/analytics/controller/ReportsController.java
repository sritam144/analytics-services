package com.asiczen.analytics.controller;

import com.asiczen.analytics.dto.VehicleHours;
import com.asiczen.analytics.request.OrgAndDateLevelRequest;
import com.asiczen.analytics.request.OrganizationVehicleLevelRequest;
import com.asiczen.analytics.response.DistanceByVehicleDTO;
import com.asiczen.analytics.response.GraphResponseDTO;
import com.asiczen.analytics.response.VehicleStatusCounter;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.asiczen.analytics.dto.VehicleDistanceAverage;
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
    public List<VehicleHours> getVehicleidleRunningHours(@Valid @RequestBody OrgLevelRequest request) {
        return service.getVehicleHours(request);
    }


    // Count the number of vehicles active,inactive,idle over a period of time group by days.
    // Front end should send the date range at least of 1 week and maximum of 1 month.

    @PostMapping("/vehiclestatuscounter")
    @ResponseStatus(HttpStatus.OK)
    public GraphResponseDTO getVehicleStatusCountGroupByDates(@Valid @RequestBody OrgLevelRequest request) {
        return new GraphResponseDTO(new Date(), service.getVehicleStatusCountGroupByDates(request));
    }

    @PostMapping("/activevehiclevsdistance")
    @ResponseStatus(HttpStatus.OK)
    public GraphResponseDTO getActiveVehicleVsDistanceGroupByDate(@Valid @RequestBody OrgLevelRequest request) {
        return new GraphResponseDTO(new Date(), service.getActiveVehicleVsDistanceGroupByDate(request));
    }

//    @PostMapping("/activevehiclehours")
//    @ResponseStatus(HttpStatus.OK)
//    public GraphResponseDTO getVehicleActivityHours(@Valid @RequestBody OrgLevelRequest request) {
//        return new GraphResponseDTO(new Date(), service.getVehicleActivityHoursGroupByDate(request));
//    }


    @PostMapping("/distanceByVehicle")
    @ResponseStatus(HttpStatus.OK)
    public GraphResponseDTO getDistanceByVehicleNumberAndDates(@Valid @RequestBody OrganizationVehicleLevelRequest organizationVehicleLevelRequest) {
        return new GraphResponseDTO(new Date(), service.getDistanceByVehicleAndDates(organizationVehicleLevelRequest));
    }

    @PostMapping("/distanceByDateAndVehicle")
    @ResponseStatus(HttpStatus.OK)
    public GraphResponseDTO getDistanceByOrganizationAndDate(@Valid @RequestBody OrgAndDateLevelRequest orgAndDateLevelRequest) {
        return new GraphResponseDTO(new Date(), service.getDistanceByDateAndOrganization(orgAndDateLevelRequest));
    }
}
