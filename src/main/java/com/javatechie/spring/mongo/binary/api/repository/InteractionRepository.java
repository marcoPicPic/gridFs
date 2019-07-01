package com.javatechie.spring.mongo.binary.api.repository;


import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Integer> {

        List<Interaction> findAll();
}
