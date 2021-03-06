package org.robbins.flashcards.webservices.base;

import org.apache.geronimo.mail.util.Base64;
import org.junit.Before;
import org.robbins.flashcards.jackson.CustomObjectMapper;
import org.robbins.flashcards.jackson.MappingJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@ContextConfiguration(locations = { "classpath*:applicatonContext-webServices-test.xml" })
public abstract class BaseRestTest extends AbstractJUnit4SpringContextTests {

	@Value("${server.address}")
	private String serverAddress;

	@Value("${server.loginname}")
	private String loggedInUserName;

	@Value("${server.password}")
	private String loggedInPassword;

	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * Before test.
	 */
	@Before
	public void beforeTest() {
		// although this is only strictly required with a POST or PUT there's no
		// harm in doing for every HTTP method
		configureRestTemplate();
	}

	/**
	 * Gets the rest template.
	 * 
	 * @return the rest template
	 */
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	/**
	 * Gets the server address.
	 * 
	 * @return the server address
	 */
	public String getServerAddress() {
		return this.serverAddress;
	}

	/*
	 * When POSTing or PUTting the an object it is necessary to configure the
	 * @JsonFilter even though we don't want to use a filter here. The
	 * RestTemplate uses Jackson by default but in this case we need to provide
	 * a customized ObjectMapper
	 * http://stackoverflow.com/questions/9397061/spring
	 * -resttemplate-with-jackson-throws-can-not-resolve-beanpropertyfilter-whe
	 */
	/**
	 * Configure rest template.
	 */
	protected void configureRestTemplate() {
		ObjectMapper mapper = new CustomObjectMapper();
		FilterProvider filterProvider = new SimpleFilterProvider()
				.setFailOnUnknownId(false);
		mapper.setFilters(filterProvider);

		// See:
		// http://blog.pastelstudios.com/2012/03/12/spring-3-1-hibernate-4-jackson-module-hibernate/
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		messageConverter.setObjectMapper(mapper);
		restTemplate.getMessageConverters().add(messageConverter);
	}

	// add Authorization header to each REST request using Basic Auth
	/**
	 * Gets the auth headers.
	 * 
	 * @return the auth headers
	 */
	protected HttpHeaders getAuthHeaders() {
		String encodedString = encodedCreds();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", encodedString);
		return httpHeaders;
	}

	// Authorization header must be Base64 encoded
	/**
	 * Encoded creds.
	 * 
	 * @return the string
	 */
	protected String encodedCreds() {
		String creds = this.loggedInUserName + ":" + this.loggedInPassword;
		byte[] bytes = creds.getBytes();
		byte[] encodingBytes = Base64.encode(bytes);
		return "Basic " + new String(encodingBytes);
	}
}
