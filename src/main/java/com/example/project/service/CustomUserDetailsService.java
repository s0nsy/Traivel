package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
   private final UserMapper userMapper;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
      User user = userMapper.findByUsername(username);
      if(user==null)
         throw new RuntimeException( "User not found: "+ username);

      return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),getAuthorities(user));
   }
   // 권한 부여하기
   private Collection<? extends GrantedAuthority> getAuthorities (User user){
      return List.of(new SimpleGrantedAuthority("ROLE_"+ user.getRole()));
   }
}
