package com.javatechie.spring.mongo.binary.api.controller;

import com.javatechie.spring.mongo.binary.api.config.DocumentType;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RestController
public class BinaryDataController {

	public static final String TENANT_UUID_1 = "f0e59e00-ed4c-4068-bf7c-f99e69ab4fc1";
	public static final String TENANT_UUID_2 = "c725b4b2-dabb-4c3f-a149-8fb40da331f6";
	public static final int TENANT_ID_2 = 1111111;
	public static final int THREAD_ID_3 = 2333444;
	public static final int TENANT_ID_1 = 1000000;
	public static final int THREAD_ID_2 = 2222222;
	public static final int THREAD_ID_1 = 2000000;

	@Autowired
	private GridFsOperations gridFsOperations;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFSBucket gridFSBucket;

	String fileId = "5d1611d5584bad4d276e1fe6";

	@GetMapping("/saveFiles")
	public String saveFile() throws FileNotFoundException {
		
		doDeleteFiles();

		List<com.mongodb.client.gridfs.model.GridFSFile> resultList = new ArrayList<>();

		gridFsOperations.find(new Query()).into(resultList);
		assertEquals(0, resultList.size());

		storeFirstDocument();

		resultList.clear();
		gridFsOperations.find(new Query()).into(resultList);
		assertEquals(1, resultList.size());

		storeSecondDocument();

		resultList.clear();
		gridFsOperations.find(new Query()).into(resultList);
		assertEquals(2, resultList.size());

		storeThirdDocument();

		resultList.clear();
		gridFsOperations.find(new Query()).into(resultList);
		assertEquals(3, resultList.size());

		return resultList.size() + " files stored successfully..." + getHomeContent();
	}



	@GetMapping("/retrieve/image")
	public String retrieveImageFile() throws IOException {
		GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));

		downloadFile(gridFsFile);
		return "Image File retrieved with name containing : " + gridFsFile.getFilename() + getHomeContent();
	}

	@GetMapping("/retrieve/tenant/{tenantUuid}")
	public String retrieveFilesByTenant(@PathVariable String tenantUuid) throws IOException {
		List<com.mongodb.client.gridfs.model.GridFSFile> files = new ArrayList<>();
		GridFSFindIterable tenantFiles = gridFsOperations.find(new Query(Criteria.where("metadata.tenantUuid").is(tenantUuid)));
		tenantFiles.into(files);
		String fileNames = "";
		for(GridFSFile file : files) {
			downloadFile(file);
			if(fileNames.isEmpty()) {
				fileNames = file.getFilename();
			} else {
				fileNames += ", " + file.getFilename();
			}
		}

		return "Files retrieved : " + fileNames + getHomeContent();
	}


	@GetMapping("/retrieve/text")
	public String retriveTextFile() throws IOException {

		GridFSFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("metadata.type").is("data")));

		downloadFile(dbFile);

		return "Text File retrieved with name containing : " + dbFile.getFilename() + getHomeContent();
	}

	@GetMapping("/deleteFiles")
	public String deleteFiles() {
		doDeleteFiles();
		
		return "Files deleted." + getHomeContent();
	}

	private void doDeleteFiles() {
		gridFsOperations.delete(new Query());

		List<com.mongodb.client.gridfs.model.GridFSFile> resultList = new ArrayList<>();

		gridFsOperations.find(new Query()).into(resultList);
		assertEquals(0, resultList.size());
	}

	@GetMapping("/")
	public String home() {
		return getHomeContent();
	}

	private String getHomeContent() {
		StringBuilder sb = new StringBuilder();
		sb.append("<style>a {font-size: 30px; text-decoration: none; color: red;}</style>");
		sb.append("\n<br/>\n<br/>");
		sb.append("<a href=\"http://localhost:9999/\" alt=\"home\" title=\"home\">&#127968;</a><br/>\n");
		sb.append("<a href=\"http://localhost:9999/saveFiles\" alt=\"save files\" title=\"save files\">&#128190;</a><br/>\n");
		sb.append("<a href=\"http://localhost:9999/retrieve/image\" alt=\"retrieve image\" title=\"retrieve image\">&#128444;</a><br/>\n");
		sb.append("<a href=\"http://localhost:9999/retrieve/text\" alt=\"retrieve text\" title=\"retrieve text\">&#128441;</a><br/>\n");
		sb.append("<a href=\"http://localhost:9999/retrieve/tenant/f0e59e00-ed4c-4068-bf7c-f99e69ab4fc1\" alt=\"retrieve files for tenant 1\" title=\"retrieve files for tenant 1\">T &#49;</a><br/>\n");
		sb.append("<a href=\"http://localhost:9999/retrieve/tenant/c725b4b2-dabb-4c3f-a149-8fb40da331f6\" alt=\"retrieve files for tenant 2\" title=\"retrieve files for tenant 2\">T &#50;</a><br/>\n");
		sb.append("<a href=\"http://localhost:9999/deleteFiles\" alt=\"delete files\" title=\"delete files\">&#128465;</a><br/>\n");
		return sb.toString();
	}

	private void storeFirstDocument() throws FileNotFoundException {

		DBObject metaData = getMetaData(new Integer(TENANT_ID_1), TENANT_UUID_1, new Integer(THREAD_ID_1), DocumentType.IMAGE, "image");

		// store image file
		InputStream inputStream = new FileInputStream("/var/akio/poc/gridfs/logo.png");


		fileId = String.valueOf(gridFsOperations.store(inputStream, "logo.png", "image/png", metaData));
		System.out.println("File id stored : " + fileId);
	}

	private void storeSecondDocument() throws FileNotFoundException {

		DBObject metaData = getMetaData(new Integer(TENANT_ID_1), TENANT_UUID_1, new Integer(THREAD_ID_2), DocumentType.TEXT, "data");

		gridFsOperations.store(new FileInputStream("/var/akio/poc/gridfs/test.txt"), "myText.txt", "text/plain",
				metaData);
	}

	private void storeThirdDocument() throws FileNotFoundException {

		DBObject metaData = getMetaData(new Integer(TENANT_ID_2), TENANT_UUID_2, new Integer(THREAD_ID_3), DocumentType.TEXT, "data");

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

	private void downloadFile(GridFSFile file) throws FileNotFoundException, IOException {

		GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(file.getObjectId());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		String filename = file.getFilename();

		int data = gridFSDownloadStream.read();
		while (data >= 0) {
			outputStream.write((char) data);
			data = gridFSDownloadStream.read();
		}
		byte[] bytesToWriteTo = outputStream.toByteArray();
		gridFSDownloadStream.close();
		outputStream.writeTo(new FileOutputStream(new File("/var/akio/poc/gridfs/[downloadedUsingGridFS]" + file.getFilename())));
		String fileContent = new String(bytesToWriteTo, StandardCharsets.UTF_8);
		System.out.println(fileContent);

		System.out.println("File name : " + file.getFilename() + " content : " + fileContent);
	}

}
