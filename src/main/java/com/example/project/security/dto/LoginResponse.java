package com.example.project.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
   private String accessToken;

   @Override
   public String toString() {
      return accessToken;
   }

}
