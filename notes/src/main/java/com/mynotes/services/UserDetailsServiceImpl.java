package com.mynotes.services;
import com.mynotes.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.mynotes.entities.User user = userRepo.findByUsername(username);
        if (user!=null){
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
        }
        return null;
    }
}

