package com.example.project.config.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
   private String username;
   private String password;
   private String confirmedPassword;
   private String email;
   public RegisterRequest(String username, String password, String email){
      this.username=username;
      this.password=password;
      this.email=email;
   }
}
