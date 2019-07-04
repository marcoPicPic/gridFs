package com.javatechie.spring.mongo.binary.api.controller;

import com.javatechie.spring.mongo.binary.api.domain.Document;
import com.javatechie.spring.mongo.binary.api.domain.DocumentMigrate;
import com.javatechie.spring.mongo.binary.api.service.DataMigrationService;
import com.javatechie.spring.mongo.binary.api.utils.Constants;
import com.javatechie.spring.mongo.binary.api.utils.PocUtils;
import com.javatechie.spring.mongo.binary.api.utils.Utils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RestController
public class InteractionFilesController {


    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private DataMigrationService dataMigrationService;

    @Autowired
    private PocUtils pocUtils;

    @Autowired
    Utils utils;

    @GetMapping("/saveFiles")
    public String saveFile() throws FileNotFoundException {

        doDeleteFiles();

        List<GridFSFile> resultList = new ArrayList<>();

        gridFsOperations.find(new Query()).into(resultList);
        assertEquals(0, resultList.size());

        pocUtils.storeFirstDocument();

        resultList.clear();
        gridFsOperations.find(new Query()).into(resultList);
        assertEquals(1, resultList.size());

        pocUtils.storeSecondDocument();

        resultList.clear();
        gridFsOperations.find(new Query()).into(resultList);
        assertEquals(2, resultList.size());

        pocUtils.storeThirdDocument();

        resultList.clear();
        gridFsOperations.find(new Query()).into(resultList);
        assertEquals(3, resultList.size());

        return resultList.size() + " files stored successfully..." ;
    }

    @GetMapping("/all/nb/{nbFile}/little/{little}/medium/{medium}/big/{big}")
    public DocumentMigrate allInteraction(@PathVariable int nbFile,
                                          @PathVariable boolean little,
                                          @PathVariable boolean medium,
                                          @PathVariable boolean big) throws IOException, NoSuchAlgorithmException {
        DocumentMigrate documentMigrate = dataMigrationService.migrateInteractions(nbFile, little, medium, big);
        documentMigrate.setReport(documentMigrate.getReport() + utils.generateMigrationReport(documentMigrate.getImportCode()));
        return documentMigrate;
    }

    @GetMapping("/retrieve/image")
    public String retrieveImageFile() throws IOException {
        GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(Constants.FILE_ID)));

        pocUtils.downloadFile(gridFsFile);
        return "Image File retrieved with name containing : " + gridFsFile.getFilename() ;
    }

    @GetMapping("/retrieve/tenant/{tenantUuid}")
    public String retrieveFilesByTenant(@PathVariable String tenantUuid) throws IOException {
        List<com.mongodb.client.gridfs.model.GridFSFile> files = new ArrayList<>();
        GridFSFindIterable tenantFiles = gridFsOperations.find(new Query(Criteria.where("metadata.tenantUuid").is(tenantUuid)));
        tenantFiles.into(files);
        String fileNames = "";
        for(GridFSFile file : files) {
            pocUtils.downloadFile(file);
            if(fileNames.isEmpty()) {
                fileNames = file.getFilename();
            } else {
                fileNames += ", " + file.getFilename();
            }
        }

        return "Files retrieved : " + fileNames ;
    }

    @GetMapping("/retrieve/emailId/{emailId}")
    public Document retrieveFilesEmailId(@PathVariable int emailId) throws IOException {
        List<GridFSFile> gridFSFiles = new ArrayList<GridFSFile>();
        gridFsTemplate.find(new Query(Criteria.where("metadata.emailId").is(emailId))).into(gridFSFiles);

        String fileNames = "";
        Document document = new Document();
        document.setName(gridFSFiles.get(0).getFilename());
        document.setTenantUuid(gridFSFiles.get(0).getMetadata().get("tenantUuid").toString());
        document.setSize(String.valueOf(gridFSFiles.get(0).getLength()/1024));

        return document;
    }

   @RequestMapping(path = "/download/retrieve/emailId/{emailId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download(@PathVariable int emailId) throws IOException {

       List<GridFSFile> gridFSFiles = new ArrayList<GridFSFile>();
       gridFsTemplate.find(new Query(Criteria.where("metadata.emailId").is(emailId))).into(gridFSFiles);
       File file = pocUtils.downloadFile(gridFSFiles.get(0));

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @GetMapping("/retrieve/text")
    public String retriveTextFile() throws IOException {

        GridFSFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("metadata.type").is("data")));

        pocUtils.downloadFile(dbFile);

        return "Text File retrieved with name containing : " + dbFile.getFilename();
    }

    @GetMapping("/deleteFiles")
    public String deleteFiles() {
        doDeleteFiles();

        return "Files deleted.";
    }

    private void doDeleteFiles() {
        gridFsOperations.delete(new Query());

        List<com.mongodb.client.gridfs.model.GridFSFile> resultList = new ArrayList<>();

        gridFsOperations.find(new Query()).into(resultList);
        assertEquals(0, resultList.size());
    }


}
