package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import com.javatechie.spring.mongo.binary.api.repository.InteractionLogRepository;
import com.javatechie.spring.mongo.binary.api.utils.PocUtils;
import com.javatechie.spring.mongo.binary.api.utils.Utils;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GridFsService {

    Logger logger = LoggerFactory.getLogger(GridFsService.class);

    //TODO generate code import unique by import
    private static final String IMPORT_CODE = "test";
    @Autowired
    private Utils utils;


    @Autowired
    private InteractionLogRepository interactionLogRepository;

    @Autowired
    private PocUtils pocUtils;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private GridFsOperations gridFsOperations;

    public static final int INDEX_COMMIT_SIZE = 5000;



    private   List<InteractionLog> interactionLogs = new ArrayList<>();


    public InteractionLog indexInteractionData(Interaction interaction, String importCode) throws IOException {
        return importInteractionFile(interaction, importCode);
    }

    private InteractionLog importInteractionFile(Interaction interaction, String importCode) throws IOException {

        logger.info("Interaction threadId : " + interaction.getThreadId());
        File fileToStore = ResourceUtils.getFile("classpath:files/" + pocUtils.getFileNameRandom());

        String fileName = fileToStore.getName();
        long fileSize = fileToStore.length();
        DBObject metaData = utils.generateMetadata(interaction, fileToStore);
       storeDocumentAttachedFile(fileToStore, metaData, (String) metaData.get("documentType"));

        return new InteractionLog(
                new Date(),
                interaction.getTenantId(),
                interaction.getTenantUuid(),
                interaction.getThreadId(),
                interaction.getMailId(),
                interaction.getParsedMailId(),
                fileSize,
                fileName,
                importCode);
    }



    private void storeDocumentAttachedFile(File file, DBObject metaData, String contentType) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
        gridFsOperations.store(fileInputStream, file.getName(), contentType,
                metaData);
        fileInputStream.close();
    }




}
