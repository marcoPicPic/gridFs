package com.javatechie.spring.mongo.binary.api.repository;

import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface InteractionLogRepository extends MongoRepository<InteractionLog, String> {
    Iterable<InteractionLog> findByImportCode(String importCode);


}
