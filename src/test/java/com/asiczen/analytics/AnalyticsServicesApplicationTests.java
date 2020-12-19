                                                                                                                                                                                              package com.asiczen.analytics;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AnalyticsServicesApplicationTests {

	@Autowired
	private MockMvc mockMvc;

//	@Test
//	void contextLoads() {
//	}

	// TestCases for Vehicle Last Location

	
	// No Url found
	@Test
	void getLastLocationNoUrl() throws Exception {

		String requestParam = "asiczen";

		mockMvc.perform(MockMvcRequestBuilders.get("/lastpositiondtl").contentType(MediaType.APPLICATION_JSON)
				.requestAttr("orgRefName", requestParam).accept(MediaType.APPLICATION_JSON)).andExpect(status().is(404))
				.andReturn();

	}

	// Fail case -- Bad Request
	@Test
	void getLastLocationbadRequest() throws Exception {

		String requestParam = "asiczen";

		mockMvc.perform(MockMvcRequestBuilders.get("/api/analytics/lastpositiondtl").param("orgRefNameb", requestParam)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().is(400))
				.andReturn();

	}

	// Success case
	/*
	 * @Test void getLastLocation() throws Exception {
	 * 
	 * String requestParam = "asiczen";
	 * 
	 * mockMvc.perform(MockMvcRequestBuilders.get("/api/analytics/lastpositiondtl").
	 * param("orgRefName", requestParam)
	 * .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).
	 * andExpect(status().is(200)) .andReturn();
	 * 
	 * }
	 */
	
	
	// history -url
}
