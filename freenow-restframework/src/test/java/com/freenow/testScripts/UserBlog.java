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

public class UserBlog extends BaseClass{

	public static String PROPERTIES_PATH = System.getProperty("user.dir") + File.separator + "src/test/resources/";
	static PropertiesRead prop = new PropertiesRead(PROPERTIES_PATH + File.separator + "userBlog.properties");
	RestAssuredUtil restAssuredUtil = new RestAssuredUtil();
	CommonUtils commonUtils = new CommonUtils();
	Response response = null;
	//private Logger log = LoggerFactory.getLogger(UserBlog.class);

	/**
	 * To verify the email in the comment section of the specific user are in the
	 * proper format Input: Valid UserName
	 */
	@Test
	public void emailValidateForValidUser() {
		log=extent.createTest("To get Post id lists by passing specific user Input: username");
		List<Integer> postIdList = getPostIdListForSpeficUser(prop.getProperty("userNameValue"));
		/* Validate email id By passing PostIdList */
		validateEmailIdByPostIDList(postIdList);
	}

	/**
	 * To verify the email in the comment section of the specific user are in the
	 * proper format Input: UserName with special characters
	 */
	@Test
	public void emailValidateUserWithSpecialcharactor() {
		log=extent.createTest("Email Validate user with special charactors");
		List<Integer> postIdList = getPostIdListForSpeficUser(prop.getProperty("userNameSpecialCharactor"));
		/* Validate email id By passing PostIdList */
		validateEmailIdByPostIDList(postIdList);
	}

	/**
	 * To verify the email for invalid user Input: Invalid user Name
	 */
	@Test
	public void emailValidateUserWithInValidUsername() {
		log=extent.createTest("To verify the email for invalid user Input: Invalid user Name");
		String userId;
		/* Get list of Users */
		response = commonUtils.getResponse(prop.getProperty("baseUtil"), prop.getProperty("getUserEndPointUrl"));
		log.info("List of Users :" + response.asString());

		/* Get user Id of spefic user */
		userId = restAssuredUtil.getValueOfOtherKeyFromRecord(prop.getProperty("userNameKey"),
				prop.getProperty("userNameInValid"), prop.getProperty("idKey"), response);
		Assert.assertNull(userId);
	}

	/**
	 * To get Post id lists by passing specific user Input: username
	 */
	public List<Integer> getPostIdListForSpeficUser(String userNameValue) {
		String userId;
		/* Get list of Users */
		response = commonUtils.getResponse(prop.getProperty("baseUtil"), prop.getProperty("getUserEndPointUrl"));
		log.info("List of Users :" + response.asString());

		/* Get user Id of spefic user */
		userId = restAssuredUtil.getValueOfOtherKeyFromRecord(prop.getProperty("userNameKey"), userNameValue,
				prop.getProperty("idKey"), response);
		Assert.assertNotNull(userId);
		log.info("User Id of spefic user:" + userId);

		/* Get List of posts by spefic user */
		response = commonUtils.getResponse(prop.getProperty("baseUtil"),
				prop.getProperty("getPostIdsByUserId") + userId);
		log.info("List of posts by spefic user:" + response.asString());

		/* Get List of Post Ids */
		List<Object> postIds = restAssuredUtil.getListOfValues(prop.getProperty("idKey"), response);
		List<Integer> postIdList = (List<Integer>) (Object) postIds;
		Assert.assertTrue(postIdList.size() != 0);
		log.info("List of Post Ids:" + postIdList);

		return postIdList;
	}

	/**
	 * To verify the email in the comment section of the specific userby passing
	 * PostIDLists Input: PostId
	 */
	public void validateEmailIdByPostIDList(List<Integer> postIdList) {
		boolean emailpattren = true;
		ListIterator<Integer> iterator = postIdList.listIterator();
		while (iterator.hasNext()) {
			/* Get List of Comments made by users */
			response = commonUtils.getResponse(prop.getProperty("baseUtil"),
					prop.getProperty("getCommentsByPostId") + iterator.next());
			log.info("List of Comments made by users :" + response.asString());

			List<Object> emailIds = restAssuredUtil.getListOfValues(prop.getProperty("emailKey"), response);
			List<String> stringList = (List<String>) (Object) emailIds;
			Assert.assertTrue(stringList.size() != 0);
			for (String email : stringList) {
				/* Get List of email ids */
				log.info("List of email ids:" + email);
				System.out.println("List of email ids:" + email);
				emailpattren = commonUtils.isValidemailId(email);
			}

		}
		Assert.assertEquals(true, emailpattren);

	}

}
