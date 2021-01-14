package com.asiczen.analytics.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.asiczen.analytics.model.EndOfDayMessage;
import com.asiczen.analytics.response.VehicleLastLocResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RedisConvertedMessageRepository {

	@Value("${app.idle.time}")
	private long IDLETIME;

	private static final String KEY = "LASTVINFO";

	private HashOperations<String, String, EndOfDayMessage> hashOperations;

	private RedisTemplate<String, EndOfDayMessage> redisTemplate;

	public RedisConvertedMessageRepository(RedisTemplate<String, EndOfDayMessage> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOperations = this.redisTemplate.opsForHash();
	}

	public EndOfDayMessage get(String vehicleNumber) {

		log.trace("Looking for vehicle Number {}", vehicleNumber);
		ObjectMapper objMapper = new ObjectMapper();
		return objMapper.convertValue(hashOperations.get(KEY, vehicleNumber), EndOfDayMessage.class);
	}

	public List<VehicleLastLocResponse> getLastLocationAllVehicles() {

		ObjectMapper objMapper = new ObjectMapper();

		// Get list of messages from redis. Get all data from redis..
		List<EndOfDayMessage> messages = hashOperations.keys(KEY).stream()
				.map(item -> objMapper.convertValue(item, String.class))
				.collect(Collectors.toList()).stream()
				.map(this::get).collect(Collectors.toList());

		return messages.stream().map(this::responseGenerator).collect(Collectors.toList());

	}

	private VehicleLastLocResponse responseGenerator(EndOfDayMessage message) {

		VehicleLastLocResponse response = new VehicleLastLocResponse();
		BeanUtils.copyProperties(message, response);

		response.setImeiNumber(message.getImei());
		response.setTotalDistanceDaily(Math.round(message.getCalculatedDailyDistance()));
		response.setTopSpeed(Math.round(message.getTopSpeed()));

		response.setVehicleMovingFlag(message.isVehicleMovingFlag());
		response.setIdleEngineOff(message.isKeyOn());
		response.setFuel(message.getFuel());
		
		Date currentTime = new Date(System.currentTimeMillis());
		long timeDifference = currentTime.getTime() - message.getTimestamp().getTime();

		response.setCurrent((TimeUnit.MILLISECONDS.toSeconds(timeDifference) <= 300) ? true : false);

		response.setLastTime(message.getTimestamp());

		return response;
	}

//	public List<VehicleLastLocResponse> getLastLocationAllVehicles() {
//		log.trace("Getting vehicle info for all vehicles.");
//		List<VehicleLastLocResponse> response = new ArrayList<>();
//		ObjectMapper objMapper = new ObjectMapper();
//		hashOperations.keys(KEY).parallelStream().map(item -> objMapper.convertValue(item, String.class))
//				.collect(Collectors.toList()).stream().map(this::get).collect(Collectors.toList())
//				.forEach(data -> {
//					log.trace("Record received : {} ", data.toString());
//					log.trace("Testing :{} ", data.getVehicleNumber());
//
//					VehicleLastLocResponse loc = new VehicleLastLocResponse();
//					try {
//						BeanUtils.copyProperties(data, loc);
//						loc.setImeiNumber(data.getImei());
//						loc.setLastTime(data.getTimestamp().toString());
//
//						loc.setTopSpeed(Math.round(data.getTopSpeed()));
//						loc.setTotalDistanceDaily(Math.round(data.getCalculatedDailyDistance()));
//
//						try {
//
////							DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
////							Date time = dateFormat.parse(data.getDateTimestamp());
//							Date currentTime = new Date(System.currentTimeMillis());
//
//							// in mili Seconds
//							long difference = currentTime - data.getTimestamp();
//
//							if (difference > IDLETIME) {
//								loc.setCurrent(false);
//							} else {
//
//								loc.setCurrent(true);
//							}
//
//						} catch (Exception ep) {
//							log.error("Error while converting the date");
//							log.error(ep.getLocalizedMessage());
//						}
//
//					} catch (Exception ep) {
//						log.error("Error occured while getting last location.....");
//						log.error("Redis message processing error...............");
//					}
//					response.add(loc);
//				});
//		return response;
//	}
}
