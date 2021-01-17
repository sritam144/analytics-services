package com.asiczen.analytics.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleActiveDistanceDTO implements Comparable<VehicleActiveDistanceDTO> {

    private String dayOfMonth;
    private Integer activeVehicleCount;
    private Long totalDistance;

    @Override
    public int compareTo(VehicleActiveDistanceDTO otherVehicleActiveDistanceDTO) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date otherDate = formatter.parse(otherVehicleActiveDistanceDTO.getDayOfMonth());
            Date thisDate = formatter.parse(this.dayOfMonth);

            return thisDate.compareTo(otherDate);

        } catch (Exception exception) {
            return 0;
        }

    }
}
