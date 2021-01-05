package com.asiczen.analytics.response;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UnderUtilizedVehiclesResponse {

	private Integer count;
	private Set<UnderUtilizedVehicleMatrix> vehicleList;

	private int underUtilizedHours = 0;

	private Date timeStamp;

}
