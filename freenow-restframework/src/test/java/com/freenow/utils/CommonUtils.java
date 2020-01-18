package com.freenow.utils;


import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.freenow.testScripts.UserBlog;

import io.restassured.response.Response;

public class CommonUtils {
	
	 RestAssuredUtil restAssuredUtil = new RestAssuredUtil();
	 Response response = null;
	 private Logger log = LoggerFactory.getLogger(CommonUtils.class);


	public Response checkStatusCode(String baseUrl,String endPointUrl) {
		
		String url = baseUrl+endPointUrl;
		try {
		 response = restAssuredUtil.getRequest(url);
		Assert.assertEquals(response.getStatusCode(),200);
		
		log.info("Status code : "+response.getStatusCode());
		
		return response;
		} catch (Exception exception) {
			log.error("user is getting an error while checking for the state code", exception.getMessage());
			return null;
		}
	}
	
	public boolean isValidemailId(String email) {
		
		boolean valid = true;
		
		if (!isValid(email)) 
        	valid= false;
		
		return valid;
		
	}
		
	private boolean isValid(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return  pat.matcher(email).matches(); 
          
    } 

}
