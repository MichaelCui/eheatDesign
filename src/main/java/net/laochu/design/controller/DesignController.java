package net.laochu.design.controller;

import java.util.Locale;

import net.laochu.design.model.User;
import net.laochu.design.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DesignController {
	
	private static final Logger logger = LoggerFactory.getLogger(DesignController.class);
	
	@Autowired
	private UserService userService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/design", method = RequestMethod.GET)
	@ResponseBody
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		User u=new User();
		userService.loginCheck(u);
		
		return "Hello";
	}
	
}
