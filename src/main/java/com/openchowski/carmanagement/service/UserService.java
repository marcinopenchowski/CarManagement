package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.AuthorityRepo;
import com.openchowski.carmanagement.dao.UserRepo;
import com.openchowski.carmanagement.entity.Authority;
import com.openchowski.carmanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PrePersist;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


   private UserRepo userRepo;
   private AuthorityRepo authorityRepo;
   private BCryptPasswordEncoder passwordEncoder;

   @Autowired
   public UserService(UserRepo userRepo, AuthorityRepo authorityRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
        this.passwordEncoder = passwordEncoder;
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
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           user.setEnabled(true);
           Authority authority = authorityRepo.findByAuthority("ROLE_USER");
           List<Authority> authorityList = new ArrayList<>();
           authorityList.add(authority);
           user.setAuthorityList(authorityList);
       }
       userRepo.save(user);

    }

    public void save(User user, List<String> idStr) {
        if(user.getId()==0) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(true);
            List<Authority> authorityList = new ArrayList<>();
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

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(userName);

        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), mapRolesToAuthorities(user.getAuthorityList()));

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Authority> authorityList) {

       return authorityList.stream().map(authority ->
               new SimpleGrantedAuthority(authority.getAuthorityName()))
               .collect(Collectors.toList());
   }

    public void changePassword(String currentPassword, String newPassword) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String currentUserName = null;

        if(principal instanceof UserDetails){
            currentUserName = ((UserDetails)principal).getUsername();
        }else{
            currentUserName = principal.toString();
        }

        User user = findByUsername(currentUserName);

        findByUsername(currentUserName);


        if(passwordEncoder.matches(currentPassword, user.getPassword())){

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);

        }else{
            throw new RuntimeException("Wrong currentPassword!!");
        }

    }
}
