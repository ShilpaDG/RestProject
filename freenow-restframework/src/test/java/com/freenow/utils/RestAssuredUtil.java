package com.freenow.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.restassured.RestAssured;
import io.restassured.response.Response;

//@Component("restAssuredUtil")
public class RestAssuredUtil {

	private Logger log = LoggerFactory.getLogger(RestAssuredUtil.class);

	public Response getRequest(String url) {

		try {
			Response response = RestAssured.given().get(url);
			return response;
		} catch (Exception exception) {
			log.error("user is getting an error while getting the response {}", exception.getMessage());
			return null;
		}

	}

	public List<Object> getListOfValues(String key, Response response) {

		try {
			List<Object> jsonResponse = response.jsonPath().getList(key);
			return jsonResponse;
		} catch (Exception exception) {
			log.error("user is getting an error while getting list of Values", exception.getMessage());
			return null;
		}
	}

	public int getRecordSize(Response response) {
		int size = 0;
		try {
			List<String> jsonResponse = response.jsonPath().getList("$");
			size = jsonResponse.size();
			return size;
		} catch (Exception exception) {
			log.error("user is getting an error while getting list of Values", exception.getMessage());
			return size;
		}
	}

	public String getValueOfOtherKeyFromRecord(String keyToGetRecord, String value, String keyToGetValueOf,
			Response response) {
		int count = 0;

		try {
			List<String> jsonResponse = response.jsonPath().getList(keyToGetRecord);

			for (String s : jsonResponse) {
				if (s.equalsIgnoreCase(value)) {
					System.out.println("s     " + s);
					break;
				}
				count++;
			}
			int valueOfKey = response.body().path("[" + count + "]." + keyToGetValueOf + "");
			String castVslue = Integer.toString(valueOfKey);
			return castVslue;

		} catch (Exception exception) {
			log.error("user is getting an error while getting value of other key from record", exception.getMessage());
			return null;
		}
	}

}
