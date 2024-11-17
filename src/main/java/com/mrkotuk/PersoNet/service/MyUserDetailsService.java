package com.mrkotuk.PersoNet.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.model.UserPrincipals;
import com.mrkotuk.PersoNet.repo.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email).get();

        if (user == null)
            throw new UsernameNotFoundException("User not found");

        return new UserPrincipals(user);
    }
}