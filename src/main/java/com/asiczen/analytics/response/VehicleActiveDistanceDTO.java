package com.asiczen.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleActiveDistanceDTO {

    private String dayOfMonth;
    private Integer activeVehicleCount;
    private Long totalDistance;

}
