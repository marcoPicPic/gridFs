package com.javatechie.spring.mongo.binary.api.repository;

import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface InteractionLogRepository extends ElasticsearchRepository<InteractionLog, String> {
    Iterable<InteractionLog> findByImportCode(String importCode);


}
