package com.example.project.service;

import com.example.project.entity.dto.TravelRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("cacheKeyUtil")
public class CacheKeyUtil {
   @Autowired
   ObjectMapper objectMapper;

   public String getTravelRecommKey(TravelRequest request) throws JsonProcessingException {
      String dtoToJson = objectMapper.writeValueAsString(request);
      return DigestUtils.sha256Hex(dtoToJson);
   }
}
