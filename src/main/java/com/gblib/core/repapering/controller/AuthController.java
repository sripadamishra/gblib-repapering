/**
 * 
 */
package com.gblib.core.repapering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.model.User;
import com.gblib.core.repapering.services.UserService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class AuthController {

	@Autowired
	UserService userService;
	
		
	@RequestMapping(value = "/find/user/{loginid}", method = RequestMethod.GET)
	public @ResponseBody User getUserDetails(@PathVariable String loginid) {		
		return userService.findByLoginId(loginid);
	}
}
