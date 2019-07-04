package com.javatechie.spring.mongo.binary.api.utils;

import com.javatechie.spring.mongo.binary.api.config.DocumentType;
import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.domain.ParameterMigrate;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class PocUtils {

    private static final int NUMBER_OF_INTERACTION = 10000;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private GridFSBucket gridFSBucket;



    public void storeFirstDocument() throws FileNotFoundException {

        DBObject metaData = getMetaData(new Integer(Constants.TENANT_ID_1), Constants.TENANT_UUID_1, new Integer(Constants.THREAD_ID_1), DocumentType.IMAGE, "image");

        // store image file
        InputStream inputStream = new FileInputStream("/var/akio/poc/gridfs/logo.png");


        String fileId = String.valueOf(gridFsOperations.store(inputStream, "logo.png", "image/png", metaData));

    }

    public void storeSecondDocument() throws FileNotFoundException {

        DBObject metaData = getMetaData(new Integer(Constants.TENANT_ID_1), Constants.TENANT_UUID_1, new Integer(Constants.THREAD_ID_2), DocumentType.TEXT, "data");

        gridFsOperations.store(new FileInputStream("/var/akio/poc/gridfs/test.txt"), "myText.txt", "text/plain",
                metaData);
    }

    public void storeThirdDocument() throws FileNotFoundException {

        DBObject metaData = getMetaData(new Integer(Constants.TENANT_ID_2), Constants.TENANT_UUID_2, new Integer(Constants.THREAD_ID_3), DocumentType.TEXT, "data");

        gridFsOperations.store(new FileInputStream("/var/akio/poc/gridfs/test2.txt"), "myTextNumber2.txt", "text/plain",
                metaData);
    }

    private DBObject getMetaData(Integer tenantId, String tenantUuid, Integer threadId, DocumentType documentType, String type) {
        DBObject metaData = new BasicDBObject();
        metaData.put("organization", "Java Techie");
        metaData.put("tenantId", tenantId);
        metaData.put("tenantUuid", tenantUuid);
        metaData.put("threadId", threadId);
        metaData.put("documentType", documentType);
        metaData.put("type", type);
        return metaData;

    }

    public File downloadFile(GridFSFile file) throws IOException {

        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(file.getObjectId());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int data = gridFSDownloadStream.read();
        while (data >= 0) {
            outputStream.write((char) data);
            data = gridFSDownloadStream.read();
        }
        byte[] bytesToWriteTo = outputStream.toByteArray();
        gridFSDownloadStream.close();
        File filecreate = new File("/home/mapicavet/sources/perso/spring-mongo-gridFStemplate-master/src/main/resources/download/" + file.getFilename());
        outputStream.writeTo(new FileOutputStream(filecreate));
        String fileContent = new String(bytesToWriteTo, StandardCharsets.UTF_8);
        return filecreate;
    }

    public List<Interaction> createOtherThread() {
        List<Interaction> interactions = new ArrayList<>();
        for(int i=0; i< NUMBER_OF_INTERACTION; i++)
            interactions.add(createInteraction());

        return interactions;

    }

    private Interaction createInteraction() {

        Interaction interaction = new Interaction();
        interaction.setId(getRandomNumber(9999, 1000000));
        interaction.setMailId(getRandomNumber(9999, 1000000));
        //interaction.setFileName(getFileNameRandom());
        return interaction;
    }


    public String getFileNameRandom(ParameterMigrate parameterMigrate){
        return getTypeFile(parameterMigrate) + "_file_" + getRandomNumber(1, 4) + ".pdf";
    }

    private String getTypeFile(ParameterMigrate parameterMigrate) {
        Map<Integer, String> mapTypeFile = parameterMigrate.getMapTypeFile();
        return mapTypeFile.get(getRandomNumber(0, mapTypeFile.size()));
    }


    private int getRandomNumber(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }
}
