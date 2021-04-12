package com.hospitals.ehr.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EhrApiConnect {

	@Autowired
	@Qualifier("ehrTemplate")
	RestTemplate restTemplate;

	@Value("${url}")
	String url;

	public HashMap<String, HashMap<String, String>> ehrApiConnection(String period) {

		ObjectMapper mapper = new ObjectMapper();

		// HealthTrio GET Endpoint
		url = url + "&period=" + period;

//		System.out.println("URL : " + url);

		// JSON Response of EHR Notes data
		String response = restTemplate.getForObject(url, String.class);

//		System.out.println(response.substring(0, 1400));

		// Initialize a new map to set response using object mapper
		HashMap<String, HashMap<String, String>> ehrMap = new HashMap<String, HashMap<String, String>>();

		// To check data is null or not else throws exception
		if (null != response && response.length() > 0) {
			try {
				ehrMap = mapper.readValue(response, new TypeReference<HashMap<String, HashMap<String, String>>>() {
				});
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return ehrMap;
	}

}
