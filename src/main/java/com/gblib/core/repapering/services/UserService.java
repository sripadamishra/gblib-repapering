package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.User;
import com.gblib.core.repapering.repository.IUserRepository;

@Service
public class UserService {

	@Autowired
	IUserRepository<User> userRepository;
	
	@Transactional
	public User findByLoginId(String login_id) {
		
		User u = userRepository.findByLoginId(login_id);		
		return u;
	}
	
	public User findByLoginIdAndPasswordVal(String loginId,String passwordVal) {
		User u = userRepository.findByLoginIdAndPasswordVal(loginId,passwordVal);
		return u;
	}
	
}
