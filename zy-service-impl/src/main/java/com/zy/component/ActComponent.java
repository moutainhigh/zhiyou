package com.gc.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.gc.Config;
import com.gc.mapper.UserMapper;

/**
 * Created by freeman on 16/8/3.
 */
@Component
@Validated
public class ActComponent {


	@Autowired
	private Config config;

	@Autowired
	private UserMapper userMapper;

	

}
