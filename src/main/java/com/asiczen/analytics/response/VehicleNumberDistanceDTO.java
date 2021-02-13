package com.asiczen.analytics.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleNumberDistanceDTO implements Comparable<VehicleNumberDistanceDTO> {

    private String vehicleNumber;
    private Integer distance;
    private int messageCounter;

    @Override
    public int compareTo(VehicleNumberDistanceDTO obj1) {
        try {
            return this.getDistance().compareTo(obj1.getDistance());
        } catch (Exception exception){
            return 0;
        }
    }
}
