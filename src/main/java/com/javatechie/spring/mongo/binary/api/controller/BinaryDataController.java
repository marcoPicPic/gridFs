package com.javatechie.spring.mongo.binary.api.controller;

import com.javatechie.spring.mongo.binary.api.service.DataMigrationService;
import com.javatechie.spring.mongo.binary.api.utils.Constants;
import com.javatechie.spring.mongo.binary.api.utils.PocUtils;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

//@RestController
public class BinaryDataController {
/*
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

	@GetMapping("/saveFiles")
	public String saveFile() throws FileNotFoundException {
		
		doDeleteFiles();

		List<com.mongodb.client.gridfs.model.GridFSFile> resultList = new ArrayList<>();

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

		return resultList.size() + " files stored successfully..." + pocUtils.getHomeContent();
	}

	@GetMapping("/all")
	public void allInteraction() throws IOException, NoSuchAlgorithmException {
		dataMigrationService.migrateInteractions();
	}

	@GetMapping("/retrieve/image")
	public String retrieveImageFile() throws IOException {
		GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(Constants.FILE_ID)));

		pocUtils.downloadFile(gridFsFile);
		return "Image File retrieved with name containing : " + gridFsFile.getFilename() + pocUtils.getHomeContent();
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

		return "Files retrieved : " + fileNames + pocUtils.getHomeContent();
	}


	@GetMapping("/retrieve/text")
	public String retriveTextFile() throws IOException {

		GridFSFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("metadata.type").is("data")));

		pocUtils.downloadFile(dbFile);

		return "Text File retrieved with name containing : " + dbFile.getFilename() + pocUtils.getHomeContent();
	}

	@GetMapping("/deleteFiles")
	public String deleteFiles() {
		doDeleteFiles();
		
		return "Files deleted." + pocUtils.getHomeContent();
	}

	private void doDeleteFiles() {
		gridFsOperations.delete(new Query());

		List<com.mongodb.client.gridfs.model.GridFSFile> resultList = new ArrayList<>();

		gridFsOperations.find(new Query()).into(resultList);
		assertEquals(0, resultList.size());
	}

	@GetMapping("/")
	public String home() {
		return pocUtils.getHomeContent();
	}


	public void storeDocumentAttachedFile(File file, DBObject metaData, String contentType) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
		gridFsOperations.store(fileInputStream, file.getName(), contentType,
				metaData);
		fileInputStream.close();
	}
*/
}
