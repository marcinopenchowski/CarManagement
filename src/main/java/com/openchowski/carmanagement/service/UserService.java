package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.AuthorityRepo;
import com.openchowski.carmanagement.dao.UserRepo;
import com.openchowski.carmanagement.entity.Authority;
import com.openchowski.carmanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {


   private UserRepo userRepo;
   private AuthorityRepo authorityRepo;

   @Autowired
   public UserService(UserRepo userRepo, AuthorityRepo authorityRepo) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
   }




    public List<User> findAll(){

        List<User> userList = userRepo.findAll();

        return userList;
    }

    public List<User> findAll(String sortField, String sortDirection){

        List<User> userList = userRepo.findAll(createSort(sortField, sortDirection));


        return userList;
    }

    public User findById(int id){

        Optional<User> result = userRepo.findById(id);

        User user = null;

        if(result.isPresent()){

            user = result.get();

        }else{
            throw new RuntimeException("Did not find user(id - " + id + ")");
        }

        return user;
    }


    private Sort createSort(String sortField, String sortDirection){

        Sort sort = sortDirection.equalsIgnoreCase(
                Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        return sort;
    }

    public List<User> search(String searchName){
        List<User> userList = userRepo.search(searchName);

        return userList;
    }

    public void save(User user) {
       if(user.getId()==0) {
           user.setEnabled(true);
           Authority authority = authorityRepo.findByAuthority("ROLE_USER");
           List<Authority> authorityList = new ArrayList<>();
           authorityList.add(authority);
           user.setAuthorityList(authorityList);
       }
       userRepo.save(user);

    }

    public void deleteById(int tempId) {
       userRepo.deleteById(tempId);
    }

    public void update(int userId, List<String> idStr, String username) {

       List<Authority> authorityList = new ArrayList<>();

       Optional<User> resultUser = userRepo.findById(userId);
       User user = null;
       if(resultUser.isPresent()){
           user = resultUser.get();
       }else {
           throw new RuntimeException("Did not find user(id - " + userId + ")");
       }

       for(String tempIdStr : idStr){

           int tempId = Integer.parseInt(tempIdStr);

           Optional<Authority> result = authorityRepo.findById(tempId);

           Authority authority = null;
           if(result.isPresent()){
               authority = result.get();
               authorityList.add(authority);
           }else{
               throw new RuntimeException("Did not find authority(id - " + tempId + ")");
           }

       }
        user.setUsername(username);
        user.setAuthorityList(authorityList);

        userRepo.save(user);

    }

    public User findByUsername(String currentUserName) {
       User user = userRepo.findByUsername(currentUserName);
       return user;
    }
}
