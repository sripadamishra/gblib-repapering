package com.gblib.core.repapering.repository;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.User;

public interface IUserRepository<U> extends CrudRepository<User,Long> {

	User findByLoginId(String login_id);
}
