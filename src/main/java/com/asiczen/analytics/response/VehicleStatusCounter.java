package com.asiczen.analytics.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleStatusCounter {

    private int activeVehicleCount;
    private int idleVehicleCount;
    // This field is for x-axis or y-axis of graph
    private String dayOfMonth;
}
