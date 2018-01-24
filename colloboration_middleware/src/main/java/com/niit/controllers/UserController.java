package com.niit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.UserDao;
import com.niit.model.ErrorClazz;
import com.niit.model.User;

@RestController
public class UserController {
	@Autowired
	private UserDao userDao;
	
	public UserController() {
	System.out.println("UserController INSTANTIATED");
}
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public ResponseEntity<?> registration(@RequestBody User user) {
		if(!userDao.isEmailValid(user.getEmail())) {
			ErrorClazz error=new ErrorClazz(2,"Email id already exists...please enter different email id");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.CONFLICT);
		}
		try {
			userDao.registration(user);
			return new ResponseEntity<User>(user,HttpStatus.CREATED);
		}
		catch(Exception e) {
			ErrorClazz error=new ErrorClazz(2,"Unable to register user details"+e.getMessage());
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	//{'email':'john.s@abc.com','password':'123'}  - i/p
	public ResponseEntity<?> login(@RequestBody User user){
		User validUser=userDao.login(user);
		if(validUser==null)//invalid credentials, error
		{
			ErrorClazz error=new ErrorClazz(3,"Invalid email id/password.. please enter valid credentials");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);//401
		}
		else{//valid credentials, success
			return new ResponseEntity<User>(validUser,HttpStatus.OK);
		}
	}
}
