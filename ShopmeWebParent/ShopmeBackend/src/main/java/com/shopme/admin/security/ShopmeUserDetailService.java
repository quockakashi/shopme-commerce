package com.shopme.admin.security;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class ShopmeUserDetailService implements UserDetailsService {

    private UserRepository repo;

    @Autowired
    void setRepo(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findUserByEmail(username);
        if(user != null) {
            return new ShopmeUserDetail(repo.findUserByEmail(username));
        }
        throw new UsernameNotFoundException(username + "is not exists");
    }
}
