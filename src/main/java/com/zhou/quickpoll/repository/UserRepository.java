package com.zhou.quickpoll.repository;

import com.zhou.quickpoll.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    public User findByUsername(String username);
}
