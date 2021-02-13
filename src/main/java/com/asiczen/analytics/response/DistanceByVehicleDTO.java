package com.asiczen.analytics.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceByVehicleDTO implements Comparable<DistanceByVehicleDTO> {

    LocalDate dateTimeStamp;
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
