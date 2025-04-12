package com.mrkotuk.PersoNet.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.model.User;
import com.mrkotuk.PersoNet.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                true, true, true, true,
                AuthorityUtils.createAuthorityList(user.getRole().name()));
    }
}
