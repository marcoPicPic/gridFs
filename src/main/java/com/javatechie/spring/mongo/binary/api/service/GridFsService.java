package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.controller.BinaryDataController;
import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import com.javatechie.spring.mongo.binary.api.repository.InteractionLogRepository;
import com.javatechie.spring.mongo.binary.api.utils.PocUtils;
import com.javatechie.spring.mongo.binary.api.utils.Utils;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class GridFsService {

    //TODO generate code import unique by import
    private static final String IMPORT_CODE = "test";
    @Autowired
    private Utils utils;

    @Autowired
    private BinaryDataController binaryDataController;

    @Autowired
    private InteractionLogRepository interactionLogRepository;

    @Autowired
    private PocUtils pocUtils;


    public void indexInteractionData(Interaction interaction) throws IOException {
        importInteractionFile(interaction);
    }

    private void importInteractionFile(Interaction interaction) throws IOException {


        System.out.println("pathToFileToStore");
        //File fileToStore = new File(pocUtils.getFileNameRandom());
        File fileToStore = ResourceUtils.getFile("classpath:files/" + pocUtils.getFileNameRandom());

        String fileName = fileToStore.getName();
        long fileSize = fileToStore.getTotalSpace();
        DBObject metaData = utils.generateMetadata(interaction, fileToStore);
        binaryDataController.storeDocumentAttachedFile(fileToStore, metaData, (String) metaData.get("documentType"));

        //logs in elasticsearch
        writeLogs(interaction, fileName, fileSize);
    }

    private void writeLogs(Interaction interaction, String fileName, long fileSize) {
        interactionLogRepository.save(new InteractionLog(
                new Date(),
                interaction.getTenantId(),
                interaction.getTenantUuid(),
                interaction.getThreadId(),
                interaction.getMailId(),
                interaction.getParsedMailId(),
                fileSize,
                fileName,
                IMPORT_CODE));
    }


}
