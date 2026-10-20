package com.example.project.repository;

import com.example.project.entity.document.AccessDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;


public interface AccessSearchRepository extends ElasticsearchRepository<AccessDocument, Long> {
}
