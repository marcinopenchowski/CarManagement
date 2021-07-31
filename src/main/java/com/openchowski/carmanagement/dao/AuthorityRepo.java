package com.openchowski.carmanagement.dao;

import com.openchowski.carmanagement.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Integer> {

    @Query("select a from Authority a where a.authorityName like ?1")
    Authority findByAuthority(String authorityName);
}
