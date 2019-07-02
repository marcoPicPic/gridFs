package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.repository.InteractionRepository;
import com.javatechie.spring.mongo.binary.api.utils.PocUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DataMigrationService {
    Logger logger = LoggerFactory.getLogger(DataMigrationService.class);

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private GridFsService gridFsService;

    @Autowired
    private PocUtils pocUtils;


    public void migrateInteractions() throws IOException {
        logger.info("-------- START -------------");
        logger.info("Debut migrateInteractions : " + LocalDateTime.now());
        readInteractionsView(interactionRepository.findAll());
        logger.info("fin migrateInteractions : " + LocalDateTime.now());
        //readInteractionsRandom();
    }


    //requet view return interactions
    public void readInteractionsView(List<Interaction> interactionList) throws IOException {
        for (Interaction interaction : interactionList) {
            gridFsService.indexInteractionData(interaction);
         ;
        }
    }

    private void readInteractionsRandom() {
        List<Interaction> interactionList  = pocUtils.createOtherThread();
        for (Interaction interaction : interactionList) {
            //TODO
            // gridFsService.indexInteractionDataRandom(interaction);

        }

    }


}
