package com.asiczen.analytics.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleHistoryRequest {

	@NotNull(message="vehicle number cannot be missing or empty")
	@Pattern(regexp="^[a-zA-Z0-9]+$",message="CehicleNumber can't have special characters")
	String vehicleNumber;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	LocalDateTime startDateTime;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	LocalDateTime endDateTime;
}
