package com.asiczen.analytics.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.asiczen.analytics.repository.RedisOrganizationMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiczen.analytics.repository.RedisConvertedMessageRepository;
import com.asiczen.analytics.response.FleetStatusResponse;
import com.asiczen.analytics.response.FleetUsageResponse;
import com.asiczen.analytics.response.LowFuelAlertResponse;
import com.asiczen.analytics.response.OverSpeedVehiclesResponse;
import com.asiczen.analytics.response.UnderSpeedVehiclesResponse;
import com.asiczen.analytics.response.UnderUtilizedVehicleMatrix;
import com.asiczen.analytics.response.UnderUtilizedVehiclesResponse;
import com.asiczen.analytics.response.VehicleDistanceMatrix;
import com.asiczen.analytics.response.VehicleFuelMatrix;
import com.asiczen.analytics.response.VehicleLastLocResponse;
import com.asiczen.analytics.response.VehicleSpeedMatrix;
import com.asiczen.analytics.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisAnalyticsServiceImpl implements RedisService {

    @Autowired
    RedisConvertedMessageRepository redisRepo;

    @Autowired
    RedisOrganizationMessageRepository redisOrganizationMessageRepository;

    @Override
    public List<VehicleLastLocResponse> getLastLocationOfVehicles(String orgRefName) {

        return redisRepo.getLastLocationAllVehicles().stream()
                .filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName))
                .collect(Collectors.toList());
    }

    @Override
    public FleetStatusResponse getFleetStatus(String orgRefName) {

        FleetStatusResponse response = new FleetStatusResponse();

        List<VehicleLastLocResponse> vehicles = redisRepo.getLastLocationAllVehicles().stream()
                .filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName)).collect(Collectors.toList());

        // Total Vehicle Count
        response.setTotalVehicle(vehicles.stream().distinct().count());

        // No of Vehicles - with no data -- if last message received is 15 minutes older
        response.setNoData(vehicles.stream().filter(record -> (timeDifferenceinMinutes(record.getLastTime()) > 15)).count());

        // Idle -- If distance traveled in last 30 seconds is less than 10 miter
        response.setIdle(vehicles.stream().filter(record -> record.getSpeed() < 20 && record.isIdleEngineOn()).count());

        // inactive -- When engine is off flag to be checked -- isKey-on-off
        response.setInactive(vehicles.stream().filter(record -> record.getSpeed() < 20 && record.isIdleEngineOff()).count());

        // Running vehicle count -- Speed greater than 20
        response.setRunningVehicle(vehicles.stream().filter(record -> record.getSpeed() >= 20).count());

        response.setTimeStamp(getCurrentTimeStampInString());

        return response;
    }

    private Date getCurrentTimeStampInString() {

        //return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        return new Date();
    }

    private Long timeDifferenceinMinutes(String date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date time = null;

        long timeinMinutes = 0L;

        try {
            Date currentTime = new Date(System.currentTimeMillis());
            time = dateFormat.parse(date);
            long difference = (currentTime.getTime() - time.getTime());
            timeinMinutes = TimeUnit.MILLISECONDS.toMinutes(difference);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeinMinutes;
    }

    @Override
    public FleetUsageResponse getFleetUsage(String orgRefName) {

        FleetUsageResponse response = new FleetUsageResponse();

        List<VehicleLastLocResponse> vehicles = redisRepo.getLastLocationAllVehicles().stream()
                .filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName)).collect(Collectors.toList());

        // Total distance traveled by all vehicles at that point in time
        response.setTotalDistance(
                (int) Math.round(vehicles.stream().mapToDouble(VehicleLastLocResponse::getTotalDistanceDaily).sum()));

        // Average distance traveled by vehicles -- Sum of above distance / no of
        // vehicles
        response.setAverage((int) Math.round(
                vehicles.stream().mapToDouble(VehicleLastLocResponse::getTotalDistanceDaily).average().getAsDouble()));

        response.setMatrix(vehicles.stream().map(record -> new VehicleDistanceMatrix(record.getVehicleNumber(), "NA",
                (int) Math.round(record.getTotalDistanceDaily()), getCurrentTimeStampInString())).collect(Collectors.toSet()));

        response.setTimeStamp(getCurrentTimeStampInString());

        return response;
    }

    // Vehicles with Fule% less that 10%
    @Override
    public LowFuelAlertResponse getVehiclesWithLowFuel(String orgRefName) {

        LowFuelAlertResponse response = new LowFuelAlertResponse();

        List<VehicleLastLocResponse> recordlist = redisRepo.getLastLocationAllVehicles();

        response.setVehicleDetails(recordlist.stream().filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName))
                .filter(record -> record.getFuel() < 11d).map(record -> new VehicleFuelMatrix(record.getVehicleNumber(),
                        record.getDriverName(), record.getFuel(), getCurrentTimeStampInString()))
                .collect(Collectors.toSet()));

        // Count vehicles with fuel less than 10%
        response.setCount((int) recordlist.stream()
                .filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName) && item.getFuel() < 11d).distinct()
                .count());

        response.setTimeStamp(getCurrentTimeStampInString());
        response.setLowFuelPercent(redisOrganizationMessageRepository.get(orgRefName).getFuelLimit());

        return response;
    }

    // Vehicles with total daily distance traveled is less than 50km
    @Override
    public UnderUtilizedVehiclesResponse getUnderUtilizedVehicles(String orgRefName) {

        UnderUtilizedVehiclesResponse response = new UnderUtilizedVehiclesResponse();

        List<VehicleLastLocResponse> vehicles = redisRepo.getLastLocationAllVehicles();

        response.setVehicleList(vehicles.stream().filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName))
                .filter(record -> record.getTotalDistanceDaily() < 50d)
                .map(record -> new UnderUtilizedVehicleMatrix(record.getVehicleNumber(), record.getDriverName(), record.getTotalDistanceDaily(), getCurrentTimeStampInString()))
                .collect(Collectors.toSet()));

        // Count of under utilized vehicles.
        response.setCount((int) vehicles.stream()
                .filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName) && item.getTotalDistanceDaily() < 50d)
                .distinct().count());

        response.setTimeStamp(getCurrentTimeStampInString());

        response.setUnderUtilizedHours(redisOrganizationMessageRepository.get(orgRefName).getUnderUtilizedHours());

        return response;
    }

    // Vehicles moving at high speed
    @Override
    public OverSpeedVehiclesResponse getOverSpeedingVehicles(String orgRefName) {

        OverSpeedVehiclesResponse response = new OverSpeedVehiclesResponse();

        List<VehicleLastLocResponse> vehicles = redisRepo.getLastLocationAllVehicles();

        response.setVehicleList(vehicles.stream()
                .filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName) && item.getSpeed() > redisOrganizationMessageRepository.get(orgRefName).getOverSpeedLimit())
                .map(record -> new VehicleSpeedMatrix(record.getVehicleNumber(), record.getDriverName(),
                        record.getSpeed(), getCurrentTimeStampInString()))
                .collect(Collectors.toSet()));

        response.setCount((int) vehicles.stream()
                .filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName) && item.getSpeed() > redisOrganizationMessageRepository.get(orgRefName).getOverSpeedLimit()).distinct()
                .count());

        response.setOverSpeedLimit(redisOrganizationMessageRepository.get(orgRefName).getOverSpeedLimit());

        response.setTimeStamp(getCurrentTimeStampInString());
        return response;
    }

    // Vehicles moving at slow speed
    @Override
    public UnderSpeedVehiclesResponse underSpeedVehicles(String orgRefName) {

        UnderSpeedVehiclesResponse response = new UnderSpeedVehiclesResponse();

        List<VehicleLastLocResponse> vehicles = redisRepo.getLastLocationAllVehicles();

        response.setVehicleList(vehicles.stream()
                .filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName) && item.getSpeed() < 20d
                        && item.getSpeed() > 5d)
                .map(record -> new VehicleSpeedMatrix(record.getVehicleNumber(), record.getDriverName(),
                        record.getSpeed(), getCurrentTimeStampInString()))
                .collect(Collectors.toSet()));

        response.setCount((int) vehicles.stream().filter(item -> item.getOrgRefName().equalsIgnoreCase(orgRefName)
                && item.getSpeed() < 20d && item.getSpeed() > 5d).distinct().count());

        response.setTimeStamp(getCurrentTimeStampInString());
        response.setUnderSpeedLimit(redisOrganizationMessageRepository.get(orgRefName).getUnderSpeedLimit());


        return response;

    }

}
