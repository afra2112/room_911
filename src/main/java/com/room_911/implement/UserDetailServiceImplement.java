package com.room_911.implement;

import com.room_911.entity.Admin;
import com.room_911.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImplement implements UserDetailsService {
    @Autowired
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .authorities("ADMIN")
                .build();
    }
}
