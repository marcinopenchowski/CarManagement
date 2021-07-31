package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.AuthorityRepo;
import com.openchowski.carmanagement.entity.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

    private AuthorityRepo authorityRepo;

    @Autowired
    public AuthorityService(AuthorityRepo authorityRepo) {
        this.authorityRepo = authorityRepo;
    }


    public Authority findByAuthority(String authorityName) {
        Authority authority = authorityRepo.findByAuthority(authorityName);
        return authority;
    }

    public List<Authority> findAll() {
        List<Authority> authorityList = authorityRepo.findAll();
        return authorityList;
    }
}
