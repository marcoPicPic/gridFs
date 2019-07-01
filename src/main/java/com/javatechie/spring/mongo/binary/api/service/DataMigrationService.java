package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DataMigrationService {

    @Autowired
    GridFsService gridFsService;

    //requete de la vue retrun interactions
    private void readView(){
        List<Interaction> interactionList = new ArrayList<>();
        for(Interaction interaction:interactionList) {
            gridFsService.indexInteractionData(interaction);
            //write logs

        }
    }
}
