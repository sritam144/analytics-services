package com.asiczen.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleHourGraphDTO {

    long idleKeyOnTime;
    long idleKeyOffTime;
    long vehicleMovingTime;
}
