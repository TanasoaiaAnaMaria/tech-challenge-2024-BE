package com.usv.Team.Finder.App.service;


import com.usv.Team.Finder.App.dto.UserDto;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String eMailAdress) throws UsernameNotFoundException {
        return userRepository.findByeMailAdress(eMailAdress).orElseThrow(()-> new UsernameNotFoundException("User not found :("));
    }

}