package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import com.javatechie.spring.mongo.binary.api.repository.InteractionLogRepository;
import com.javatechie.spring.mongo.binary.api.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class DataMigrationService {

    private static final int NUMBER_OF_INTERACTION = 10000;
    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private InteractionLogRepository interactionLogRepository;

    @Autowired
    private GridFsService gridFsService;


    public void migrateInteractions() throws IOException {
        readInteractionsView(interactionRepository.findAll());
        readInteractionsRandom();
    }


    //requet view return interactions
    public void readInteractionsView(List<Interaction> interactionList) throws IOException {
        for (Interaction interaction : interactionList) {
            gridFsService.indexInteractionData(interaction);
            //write logs in elastic
            writeLogs(interaction);
        }
    }

    private void readInteractionsRandom() {
        List<Interaction> interactionList  = createOtherThread();
        for (Interaction interaction : interactionList) {
            //TODO
            // gridFsService.indexInteractionDataRandom(interaction);
            //write logs in elastic
            writeLogs(interaction);
        }

    }

    private void writeLogs(Interaction interaction) {
        interactionLogRepository.save(new InteractionLog(
                new Date(),
                interaction.getTenantId(),
                interaction.getTenantUuid(),
                interaction.getThreadId(),
                interaction.getMailId(),
                interaction.getParsedMailId()));
    }

    private List<Interaction> createOtherThread() {
        List<Interaction> interactions = new ArrayList<>();
        for(int i=0; i< NUMBER_OF_INTERACTION; i++)
            interactions.add(createInteraction());

        return interactions;

    }

    private Interaction createInteraction() {

        Interaction interaction = new Interaction();
        interaction.setId(getRandomNumber(9999, 1000000));
        interaction.setMailId(getRandomNumber(9999, 1000000));
        interaction.setFileName(getFileNameRandom());
        return interaction;
    }


    private String getFileNameRandom(){
        return getTypeFile(getRandomNumber(0, 2)) + "_file_" + getRandomNumber(1, 3);
    }

    private String getTypeFile(int typeFile) {

        switch (typeFile) {
            case 0 : return "little";
            case 1 : return "medium";
            case 2 : return "big";
            default:
                // code block
        }
        return "";
    }


    private int getRandomNumber(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }
}
