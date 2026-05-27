package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
   private final UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      log.debug("인증 사용자 조회: {}", username);
      User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
               log.warn("인증 실패 - 존재하지 않는 사용자: {}", username);
               return new RuntimeException("User not found: " + username);
            });
      return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), getAuthorities(user));
   }

   private Collection<? extends GrantedAuthority> getAuthorities(User user) {
      return List.of(new SimpleGrantedAuthority(user.getRole()));
   }
}
