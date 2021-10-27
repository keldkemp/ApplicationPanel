package com.keldkemp.applicationpanel.repositories;

import com.keldkemp.applicationpanel.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<Users, Long> {

    Users findByLogin(String login);
}
