package com.example.project.entity.document;

import com.example.project.entity.Access;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Document(indexName ="access")
@NoArgsConstructor
@Setting(settingPath= "elasticsearch/access-settings.json")
@Mapping(mappingPath = "elasticsearch/access-mappings.json")
public class AccessDocument {
  @Id
  private Long id;
  private String title;
  private String content;
  private String writer;
  private long routeId;

  public static AccessDocument from(Access access){
    AccessDocument doc = new AccessDocument();
    doc.id = access.getId();
    doc.title = access.getTitle();
    doc.content = access.getContent();
    doc.writer = access.getWriter().getUsername();
    doc.routeId = access.getRouteId();
    return doc;
  }
}
