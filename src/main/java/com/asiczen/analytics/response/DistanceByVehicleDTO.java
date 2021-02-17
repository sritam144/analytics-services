package com.asiczen.analytics.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceByVehicleDTO implements Comparable<DistanceByVehicleDTO> {

    Date dateTimeStamp;
    int distance;

    @Override
    public int compareTo(DistanceByVehicleDTO nextObj) {
        try {
            return this.dateTimeStamp.compareTo(nextObj.getDateTimeStamp());
        } catch (Exception exception) {
            return 0;
        }
    }
}
