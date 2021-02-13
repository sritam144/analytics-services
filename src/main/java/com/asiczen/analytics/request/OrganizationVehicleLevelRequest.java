package com.asiczen.analytics.request;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationVehicleLevelRequest {

    private String orgRefName;
    private String vehicleNumber;
    private Date fromDate;
    private Date toDate;
}
