package com.example.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NaverSearchListResponse {
   private List<NaverSearchRequest> items;
}
