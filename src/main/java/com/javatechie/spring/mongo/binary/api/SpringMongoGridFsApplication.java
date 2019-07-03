package com.javatechie.spring.mongo.binary.api;

import com.javatechie.spring.mongo.binary.api.service.DataMigrationService;
import com.javatechie.spring.mongo.binary.api.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
public class SpringMongoGridFsApplication implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(SpringMongoGridFsApplication.class);


	@Autowired
    DataMigrationService dataMigrationService;

    @Autowired
    Utils utils;

	public static void main(String[] args) {
		SpringApplication.run(SpringMongoGridFsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//execute import in gridfs
		//dataMigrationService.migrateInteractions();
		//logger.info(utils.generateMigrationReport("EE6D2378AD4DAA1D38446E08684AAD3B"));
	}
}
