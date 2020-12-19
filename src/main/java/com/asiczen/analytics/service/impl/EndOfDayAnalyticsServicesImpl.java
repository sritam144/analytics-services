package com.asiczen.analytics.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiczen.analytics.dto.VehicleDistance;
import com.asiczen.analytics.dto.VehicleDistanceAverage;
import com.asiczen.analytics.dto.VehiclevsHours;
import com.asiczen.analytics.model.EndOfDayMessage;
import com.asiczen.analytics.repository.EndOfDayMessageRepository;
import com.asiczen.analytics.request.OrgLevelRequest;
import com.asiczen.analytics.response.MessageCounter;
import com.asiczen.analytics.response.VehicleDistancevsDate;
import com.asiczen.analytics.service.EndOfDayAnalyticsServices;

@Component
public class EndOfDayAnalyticsServicesImpl implements EndOfDayAnalyticsServices {

	@Autowired
	EndOfDayMessageRepository eodMessageRepo;

	// method will return vehicle number vs distance traveled over a date range.
	// Distance is the sum of distance traveled over the date range.

	private String getCurrentTimeStampInString() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
	}

	@Override
	public List<VehicleDistancevsDate> getVehiclevsDistancevsDate(OrgLevelRequest request) {

		return eodMessageRepo
				.findByorgRefNameAndTimestampBetween(request.getOrgRefName(), request.getFromDate(),request.getToDate())
				.stream()
				.map(record -> new VehicleDistance(record.getVehicleNumber(), record.getCalculatedDailyDistance()))
				.collect(Collectors.groupingBy(VehicleDistance::getVehicleNumber,Collectors.summingDouble(VehicleDistance::getDistanceTraveled)))
				.entrySet().stream()
				.map(record -> new VehicleDistancevsDate(record.getKey(), record.getValue().intValue(),getCurrentTimeStampInString()))
				.collect(Collectors.toList());

	}

	// methods returns a map of vehicle numbers and (total distance , average,distance) over a date range
	@Override
	public List<VehicleDistanceAverage> getVehicleDistancevsAvrageDistanceOverDateRange(OrgLevelRequest request) {

		List<EndOfDayMessage> messsages = eodMessageRepo.findByorgRefNameAndTimestampBetween(request.getOrgRefName(),
				request.getFromDate(), request.getToDate());
		Double averageDistance = messsages.stream().mapToDouble(EndOfDayMessage::getCalculatedDailyDistance).average()
				.getAsDouble();

		return messsages.stream()
				.map(record -> new VehicleDistance(record.getVehicleNumber(), record.getCalculatedDailyDistance()))
				.collect(Collectors.groupingBy(VehicleDistance::getVehicleNumber,
						Collectors.summingDouble(VehicleDistance::getDistanceTraveled)))
				.entrySet().stream()
				.map(record -> new VehicleDistanceAverage(record.getKey(), record.getValue().intValue(), averageDistance.intValue()))
				.collect(Collectors.toList());
	}

	@Override
	public List<VehiclevsHours> getVehicleHours(OrgLevelRequest request) {

		List<EndOfDayMessage> messsages = eodMessageRepo.findByorgRefNameAndTimestampBetween(request.getOrgRefName(),
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

		return messsages.stream()
				.map(record -> new VehiclevsHours(record.getVehicleNumber(), record.getIdleKeyOnTime(),record.getIdleKeyOffTime(), record.getVehicleMovingTime()))
				.collect(Collectors.groupingBy(VehiclevsHours::getVehicleNumber)).entrySet().stream()
				.map(e -> e.getValue().stream().reduce((f1, f2) -> new VehiclevsHours(f1.vehicleNumber,f1.idleHourEngineOn + f2.idleHourEngineOn, f1.idleHourEngineOff + f2.idleHourEngineOff,f1.movingEngineOn + f2.movingEngineOn)))
				.map(Optional<VehiclevsHours>::get)
				.collect(Collectors.toList());
	}

	@Override
	public List<MessageCounter> getMessageCounter(OrgLevelRequest request) {
		//List<MessageCounter> response = new ArrayList<>();
		return new ArrayList<>();
	}

}
