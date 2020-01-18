package com.freenow.testScripts;

import java.io.File;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.freenow.utils.CommonUtils;
import com.freenow.utils.PropertiesRead;
import com.freenow.utils.RestAssuredUtil;


import io.restassured.response.Response;
import junit.framework.Assert;


public class UserBlog {
	
	public static String PROPERTIES_PATH = System.getProperty("user.dir") + File.separator+"src/test/resources/";
	static PropertiesRead prop = new PropertiesRead(PROPERTIES_PATH + File.separator + "userBlog.properties");
	 RestAssuredUtil restAssuredUtil = new RestAssuredUtil();
	 CommonUtils commonUtils = new CommonUtils();
	 Response response = null;
	 private Logger log = LoggerFactory.getLogger(UserBlog.class);
	 
	 
	/**
	 * To verify the email in the comment section of the specific user are in the proper format
	 * Input: UserName
	 */
	@Test
	 public void userBlogEmail() { 
		String userId;
		boolean emailpattren= true;
		
		response = commonUtils.checkStatusCode(prop.getProperty("baseUtil"), prop.getProperty("getUserEndPointUrl"));		 
		log.info("List of Users :"+response.asString());
		 		 
		userId = restAssuredUtil.getValueOfOtherKeyFromRecord(prop.getProperty("userNameKey"),prop.getProperty("userNameValue"),prop.getProperty("idKey"),response);
		log.info("User Id of spefic user:"+userId);
		 		 
		response = commonUtils.checkStatusCode(prop.getProperty("baseUtil"), prop.getProperty("getPostIdsByUserId")+userId);
		log.info("List of posts by spefic user:"+response.asString()); 
		
		List<Object> postIds = restAssuredUtil.getListOfValues(prop.getProperty("idKey"),response);	 
		List<Integer> postIdList=(List<Integer>)(Object)postIds;
		log.info("List of Post Ids:"+postIdList);
		 
	    ListIterator<Integer>  iterator = postIdList.listIterator();  
		 while (iterator.hasNext()) {
			response = commonUtils.checkStatusCode(prop.getProperty("baseUtil"), prop.getProperty("getCommentsByPostId")+iterator.next());		 
			log.info("List of Commands made by users :"+response.asString());
			
			 List<Object> emailIds = restAssuredUtil.getListOfValues(prop.getProperty("emailKey"),response); 
			 List<String> stringList=(List<String>)(Object)emailIds;
			 for(String email:stringList) {
				 log.info("List of email ids:"+email);
				 emailpattren =commonUtils.isValidemailId(email);
			 }
			 
		 }
		 Assert.assertEquals(true, emailpattren);
	}
}

