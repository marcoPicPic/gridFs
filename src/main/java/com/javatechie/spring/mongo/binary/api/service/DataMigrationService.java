package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataMigrationService {

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private GridFsService gridFsService;

    //requete de la vue retrun interactions
    public void readView() throws FileNotFoundException {
        List<Interaction> interactionList = interactionRepository.findAll();
        for(Interaction interaction:interactionList) {
            gridFsService.indexInteractionData(interaction);
            //write logs

        }
    }
}
