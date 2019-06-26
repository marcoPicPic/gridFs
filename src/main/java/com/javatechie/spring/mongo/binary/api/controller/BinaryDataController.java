package com.javatechie.spring.mongo.binary.api.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@RestController
public class BinaryDataController {

	@Autowired
	private GridFsOperations gridFsOperations;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFSBucket gridFSBucket;

	String fileId = "";

	@GetMapping("/saveFiles")
	public String saveFile() throws FileNotFoundException {
		// define metadata
		DBObject metaData = new BasicDBObject();
		metaData.put("organization", "Java Techie");

		// store image file
		InputStream inputStream = new FileInputStream("/home/mapicavet/Images/logo.png");
		metaData.put("type", "image");

		fileId = String.valueOf(gridFsOperations.store(inputStream, "logo.png", "image/png", metaData));
		System.out.println("File id stored : " + fileId);

		// store text file
		metaData.put("type", "data");
		gridFsOperations.store(new FileInputStream("/home/mapicavet/Images/test.txt"), "myText.txt",
				"text/plain", metaData);

		return "File stored successfully...";
	}

	@GetMapping("/retrive/image")
	public String retriveImageFile() throws IOException {
		GridFSFile gridFsFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(fileId)));
		//GridFSDBFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(fileId)));
		//dbFile.writeTo("/home/mapicavet/Images/reactive-logo.png");
		GridFSFile gridFsFile2 =  gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
		GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFsFile2.getObjectId());
		GridFsResource gridFsResource = new GridFsResource(gridFsFile2,gridFSDownloadStream );

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int data = gridFSDownloadStream.read();
		while (data >= 0) {
			outputStream.write((char) data);
			data = gridFSDownloadStream.read();
		}
		byte[] bytesToWriteTo = outputStream.toByteArray();
		gridFSDownloadStream.close();
		outputStream.writeTo(new FileOutputStream(new File("/home/mapicavet/Images/reactive-logo.png")));
		String file = new String(bytesToWriteTo, StandardCharsets.UTF_8);
		System.out.println(file);

		System.out.println("File name : " + gridFsFile2.getFilename());
		return "Image File retrived with name : " + file;
	}



	@GetMapping("/retrive/text")
	public String retriveTextFile() throws IOException {


		GridFSFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("metadata.type").is("data")));
		GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(dbFile.getObjectId());
		GridFsResource gridFsResource = new GridFsResource(dbFile,gridFSDownloadStream );


		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int data = gridFSDownloadStream.read();
		while (data >= 0) {
			outputStream.write((char) data);
			data = gridFSDownloadStream.read();
		}
		byte[] bytesToWriteTo = outputStream.toByteArray();
		gridFSDownloadStream.close();
		outputStream.writeTo(new FileOutputStream(new File("/home/mapicavet/Images/reactive-text.txt")));
		String file = new String(bytesToWriteTo, StandardCharsets.UTF_8);
		System.out.println(file);


		System.out.println("File name : " + dbFile.getFilename());
		return "Text File retrived with name : " + file;
	}

}
