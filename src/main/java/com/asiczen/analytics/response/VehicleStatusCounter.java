package com.asiczen.analytics.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleStatusCounter implements Comparable<VehicleStatusCounter> {

    private int activeVehicleCount;
    private int idleVehicleCount;
    // This field is for x-axis or y-axis of graph
    private String dayOfMonth;


    @Override
    public int compareTo(VehicleStatusCounter vehicleStatusCounter) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date otherDate = formatter.parse(vehicleStatusCounter.getDayOfMonth());
            Date thisDate = formatter.parse(this.dayOfMonth);

            return thisDate.compareTo(otherDate);

        } catch (Exception exception) {
            return 0;
        }

    }
}
