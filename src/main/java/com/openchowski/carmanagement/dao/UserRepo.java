package com.openchowski.carmanagement.dao;

import com.openchowski.carmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username like %?1%")
    List<User> search(String searchName);

    @Query("select u from User u where u.username like ?1")
    User findByUsername(String currentUserName);
}
