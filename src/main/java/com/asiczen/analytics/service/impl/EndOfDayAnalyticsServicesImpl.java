package com.asiczen.analytics.service.impl;

import com.asiczen.analytics.dto.VehicleHours;
import com.asiczen.analytics.response.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiczen.analytics.dto.VehicleDistance;
import com.asiczen.analytics.dto.VehicleDistanceAverage;
import com.asiczen.analytics.dto.VehicleHours;
import com.asiczen.analytics.model.EndOfDayMessage;
import com.asiczen.analytics.repository.EndOfDayMessageRepository;
import com.asiczen.analytics.request.OrgLevelRequest;
import com.asiczen.analytics.service.EndOfDayAnalyticsServices;

@Component
@Slf4j
public class EndOfDayAnalyticsServicesImpl implements EndOfDayAnalyticsServices {

    @Autowired
    EndOfDayMessageRepository eodMessageRepo;


    private int VEHICLE_MOVING_TIME = 0; // Seconds
    private int CALCULATED_DAILY_DISTANCE = 0; // KM


    // method will return vehicle number vs distance traveled over a date range.
    // Distance is the sum of distance traveled over the date range.

    private String getCurrentTimeStampInString() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    @Override
    public List<VehicleDistancevsDate> getVehiclevsDistancevsDate(OrgLevelRequest request) {

        return eodMessageRepo
                .findByorgRefNameAndTimestampBetween(request.getOrgRefName(), request.getFromDate(), request.getToDate())
                .stream()
                .map(record -> new VehicleDistance(record.getVehicleNumber(), record.getCalculatedDailyDistance()))
                .collect(Collectors.groupingBy(VehicleDistance::getVehicleNumber, Collectors.summingDouble(VehicleDistance::getDistanceTraveled)))
                .entrySet().stream()
                .map(record -> new VehicleDistancevsDate(record.getKey(), record.getValue().intValue(), getCurrentTimeStampInString()))
                .collect(Collectors.toList());

    }

    // methods returns a map of vehicle numbers and (total distance , average,distance) over a date range
    @Override
    public List<VehicleDistanceAverage> getVehicleDistancevsAvrageDistanceOverDateRange(OrgLevelRequest request) {

        List<EndOfDayMessage> messages = eodMessageRepo.findByorgRefNameAndTimestampBetween(request.getOrgRefName(), request.getFromDate(), request.getToDate());

        Double averageDistance = messages.stream()
                .filter(message -> message.getCalculatedDailyDistance() > 1)
                .mapToDouble(EndOfDayMessage::getCalculatedDailyDistance)
                .average()
                .getAsDouble();

        return messages.stream()
                .map(record -> new VehicleDistance(record.getVehicleNumber(), record.getCalculatedDailyDistance()))
                .collect(Collectors.groupingBy(VehicleDistance::getVehicleNumber, Collectors.summingDouble(VehicleDistance::getDistanceTraveled)))
                .entrySet().stream()
                .map(record -> new VehicleDistanceAverage(record.getKey(), record.getValue().intValue(), averageDistance.intValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleHours> getVehicleHours(OrgLevelRequest request) {

        List<EndOfDayMessage> messages = eodMessageRepo.findByorgRefNameAndTimestampBetween(request.getOrgRefName(),
                request.getFromDate(), request.getToDate());

//		 new Foo(1, "P1", 300, 400), 
//         new Foo(2, "P2", 600, 400),
//         new Foo(3, "P3", 30, 20),
//         new Foo(3, "P3", 70, 20),
//         new Foo(1, "P1", 360, 40),
//         new Foo(4, "P4", 320, 200),
//         new Foo(4, "P4", 500, 900));
//         
//         [Foo(1,P1,660,440), Foo(2,P2,600,400), Foo(3,P3,100,40), Foo(4,P4,820,1100)]

//		messsages.stream()
//				.map(record -> new VehiclevsHours(record.getVehicleNumber(), record.getIdleKeyOnTime(),record.getIdleKeyOffTime(), record.getVehicleMovingTime()))
//				.collect(Collectors.groupingBy(item -> item.getVehicleNumber())).entrySet().stream()
//				.map(e -> e.getValue().stream()
//						.reduce((f1, f2) -> new VehiclevsHours(f1.vehicleNumber,
//															   f1.idleHourEngineOn + f2.idleHourEngineOn, 
//															   f1.idleHourEngineOff + f2.idleHourEngineOff,
//															   f1.movingEngineOn + f2.movingEngineOn)))
//				.map(f -> f.get()).collect(Collectors.toList());

        return messages.stream()
                .map(record -> new VehicleHours(record.getVehicleNumber(), record.getIdleKeyOnTime(), record.getIdleKeyOffTime(), record.getVehicleMovingTime()))
                .collect(Collectors.groupingBy(VehicleHours::getVehicleNumber)).entrySet().stream()
                .map(e -> e.getValue().stream().reduce((f1, f2) -> new VehicleHours(f1.vehicleNumber, f1.idleHourEngineOn + f2.idleHourEngineOn, f1.idleHourEngineOff + f2.idleHourEngineOff, f1.movingEngineOn + f2.movingEngineOn)))
                .map(Optional<VehicleHours>::get)
                .map(record -> new VehicleHours(record.getVehicleNumber(), record.getIdleHourEngineOn() / 3600, record.getIdleHourEngineOff() / 3600, record.getMovingEngineOn() / 3600))
                .collect(Collectors.toList());

//        List<VehicleHours> data = messages.stream()
//                .map(record -> new VehicleHours(record.getVehicleNumber(), record.getIdleKeyOnTime(), record.getIdleKeyOffTime(), record.getVehicleMovingTime()))
//                .collect(Collectors.groupingBy(VehicleHours::getVehicleNumber))
//                .entrySet()
//                .stream()
//                .collect(Collectors.toMap(vehicleHour -> {
//                    int sumIdleKeyOnTime = vehicleHour.getValue().stream().mapToInt(VehicleHours::getIdleHourEngineOn).sum();
//                    int sumIdleKeyOffTime = vehicleHour.getValue().stream().mapToInt(VehicleHours::getIdleHourEngineOff).sum();
//                    int sumMovingEngineOn = vehicleHour.getValue().stream().mapToInt(VehicleHours::getMovingEngineOn).sum();
//                    return new VehicleHours(vehicleHour.getKey(), sumIdleKeyOnTime, sumIdleKeyOffTime, sumMovingEngineOn);
//
//                }, Map.Entry::getValue));


//        list.stream().collect(Collectors.groupingBy(Foo::getCategory))
//                .entrySet().stream()
//                .collect(Collectors.toMap(x -> {
//                    int sumAmount = x.getValue().stream().mapToInt(Foo::getAmount).sum();
//                    int sumPrice = x.getValue().stream().mapToInt(Foo::getPrice).sum();
//                    return new Foo(x.getKey(), sumAmount, sumPrice);
//                }, Map.Entry::getValue));

    }

    @Override
    public List<MessageCounter> getMessageCounter(OrgLevelRequest request) {
        //List<MessageCounter> response = new ArrayList<>();
        return new ArrayList<>();
    }

    @Override
    public List<VehicleStatusCounter> getVehicleStatusCountGroupByDates(OrgLevelRequest request) {

        log.info("Date range from endpoint {} {} ", request.getFromDate(), request.getToDate());
        // Step 1 : get the messages for a date range

        List<EndOfDayMessage> messages = eodMessageRepo.findByorgRefNameAndTimestampBetween(request.getOrgRefName(), request.getFromDate(), request.getToDate());

        List<VehicleStatusCounter> response = messages.stream()
                .filter(message -> message.getCalculatedDailyDistance() > CALCULATED_DAILY_DISTANCE)
                .collect(Collectors.groupingBy(record -> convertInToDateMonthYear(record), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new VehicleStatusCounter(entry.getValue().intValue(), 0, entry.getKey()))
                .collect(Collectors.toList());

        messages.stream()
                .filter(message -> message.getCalculatedDailyDistance() == CALCULATED_DAILY_DISTANCE)
                .collect(Collectors.groupingBy(record -> convertInToDateMonthYear(record), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new VehicleStatusCounter(entry.getValue().intValue(), 0, entry.getKey()))
                .forEach(record -> updateResponse(record, response));

        return response;

    }

    @Override
    public List<VehicleActiveDistanceDTO> getActiveVehicleVsDistanceGroupByDate(OrgLevelRequest request) {

        List<EndOfDayMessage> messages = eodMessageRepo.findByorgRefNameAndTimestampBetween(request.getOrgRefName(), request.getFromDate(), request.getToDate());


        List<VehicleActiveDistanceDTO> response = messages.stream()
                .filter(message -> message.getCalculatedDailyDistance() > CALCULATED_DAILY_DISTANCE)
                .collect(Collectors.groupingBy(record -> convertInToDateMonthYear(record), Collectors.summingDouble(record -> record.getCalculatedDailyDistance())))
                .entrySet()
                .stream()
                .map(entry -> new VehicleActiveDistanceDTO(entry.getKey(), 0, entry.getValue().longValue()))
                .collect(Collectors.toList());

        Map<String, Long> map = messages.stream()
                .filter(message -> message.getCalculatedDailyDistance() > CALCULATED_DAILY_DISTANCE)
                .collect(Collectors.groupingBy(record -> convertInToDateMonthYear(record), Collectors.counting()));

        response.stream().forEach(record -> updateActiveVehicleCountResponse(map, record));


        return response;
    }

    private void updateActiveVehicleCountResponse(Map<String, Long> map, VehicleActiveDistanceDTO record) {
        record.setActiveVehicleCount(map.get(record.getDayOfMonth()).intValue());

    }


    private String convertInToDateMonthYear(EndOfDayMessage record) {

        LocalDate convertedDate = convertToLocalDateViaInstant(record.getTimestamp());

        String day = String.valueOf(convertedDate.getDayOfMonth());
        String month = String.valueOf(convertedDate.getMonthValue());
        String year = String.valueOf(convertedDate.getYear());

        return day + "/" + month + "/" + year;
    }

    private void updateResponse(VehicleStatusCounter input, List<VehicleStatusCounter> response) {
        response.stream().filter(record -> record.getDayOfMonth() == input.getDayOfMonth())
                .forEach(record -> record.setIdleVehicleCount(input.getIdleVehicleCount()));
    }


    public LocalDate convertToLocalDateViaInstant(Date inputDate) {
        return inputDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    //validateDateRange(request.getFromDate(),request.getToDate())


}