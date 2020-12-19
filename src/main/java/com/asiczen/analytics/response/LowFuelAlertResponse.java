package com.asiczen.analytics.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LowFuelAlertResponse {

    Integer count;
    Set<VehicleFuelMatrix> vehicleDetails;
    private int lowFuelPercent = 0;
    private String timeStamp;
}
