package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.controller.BinaryDataController;
import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.utils.Utils;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public class GridFsService {

    @Autowired
    private Utils utils;

    @Autowired
    private BinaryDataController binaryDataController;

    public void indexInteractionData(Interaction interaction) throws FileNotFoundException {
        importInteractionFile(interaction);
    }

    private void importInteractionFile(Interaction interaction) throws FileNotFoundException {
        String pathToFileToStore = utils.getAttachedFilePath(interaction);
        File fileToStore = new File(pathToFileToStore);
        DBObject metaData = utils.generateMetadata(interaction);
        binaryDataController.storeDocumentAttachedFile(fileToStore, metaData, (String) metaData.get("documentType"));
    }


}
