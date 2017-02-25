package com.maverick.metrics;

import com.maverick.metrics.annotation.*;

/**
 * Created by abhinav on 2017-02-23.
 */
public class AwesomeResource {

	@Timed(name = "Awesomeness")
	public String awesomeMethod(String awesomeParam) {
		return awesomeParam;
	}

	public String awefulMethod(String awefulParam) {
		return awefulParam;
	}
}
