package com.example.project.converter;

import com.example.project.entity.Access;
import com.example.project.entity.Image;
import com.example.project.entity.User;
import com.example.project.entity.dto.AccessDto;
import com.example.project.mapper.AccessMapper;
import com.example.project.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccessToAccessDto {
   private final AccessMapper accessMapper;
   private final UserMapper userMapper;

   public AccessDto fromEntity(Access access) {
      List<Long> imageUrls = accessMapper.findImageByAccessId(access.getId());
      String username = userMapper.findUsernameById(accessMapper.findUserIdByAccessId(access.getId()));
      return new AccessDto(
            access.getTitle(),
            access.getContent(),
            imageUrls,
            access.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            access.getUpdated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            username
      );
   }

}
