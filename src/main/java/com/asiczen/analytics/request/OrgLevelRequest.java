package com.asiczen.analytics.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrgLevelRequest {

	private String orgRefName;
	private Date fromDate;
	private Date toDate;
}
