package com.example.project.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
   private String accessToken;

   @Override
   public String toString() {
      return accessToken;
   }

}
