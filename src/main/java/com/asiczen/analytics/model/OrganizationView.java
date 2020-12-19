package com.asiczen.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrganizationView {

    private long orgId;
    private String orgRefName;
    private String orgName;
    private boolean status;

    private Integer overSpeedLimit = 0;
    private Integer underSpeedLimit = 0;
    private Integer fuelLimit = 0;
    private Integer underUtilizedHours = 0;
    private Integer overUtilizedHours = 0;
    private Integer underUtilizedKms = 0;
    private Integer overUtilizedKms = 0;
}
