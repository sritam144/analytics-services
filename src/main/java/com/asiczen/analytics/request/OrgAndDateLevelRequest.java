package com.asiczen.analytics.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgAndDateLevelRequest {

    private String orgRefName;
    private LocalDateTime inputDate;
}
