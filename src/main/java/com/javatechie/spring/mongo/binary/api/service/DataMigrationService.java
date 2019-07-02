package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import com.javatechie.spring.mongo.binary.api.repository.InteractionRepository;
import com.javatechie.spring.mongo.binary.api.utils.PocUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.javatechie.spring.mongo.binary.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataMigrationService {
    Logger logger = LoggerFactory.getLogger(DataMigrationService.class);

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private GridFsService gridFsService;

    @Autowired
    private PocUtils pocUtils;


    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public static final int INDEX_COMMIT_SIZE = 5000;

    public void migrateInteractions() throws IOException, NoSuchAlgorithmException {
        logger.info("-------- START -------------");
        logger.info("Debut migrateInteractions : " + LocalDateTime.now());
        readInteractionsView(interactionRepository.findAll());
        logger.info("fin migrateInteractions : " + LocalDateTime.now());
        //readInteractionsRandom();
    }


    //requet view return interactions
    public void readInteractionsView(List<Interaction> interactionList) throws IOException, NoSuchAlgorithmException {
        List<InteractionLog> interactionLogs = new ArrayList<>();
        String importCode = Utils.generateUniqueImportCode();

        for (Interaction interaction : interactionList) {
            try {
                interactionLogs.add(gridFsService.indexInteractionData(interaction, importCode));
            } catch (IOException e) {
                logger.error("Erreur d'ecriture - threadId : " + interaction.getThreadId());
                writeLogs(interactionLogs);
            }
        }

        writeLogs(interactionLogs);
    }

    private void readInteractionsRandom() {
        List<Interaction> interactionList  = pocUtils.createOtherThread();
        for (Interaction interaction : interactionList) {
            //TODO
            // gridFsService.indexInteractionDataRandom(interaction);

        }

    }

   /* private void writeLogs(List<InteractionLog> interactionLogs) {

        /*List<IndexQuery> queries = new ArrayList<>();
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setObject(new InteractionLog(
                new Date(),
                interaction.getTenantId(),
                interaction.getTenantUuid(),
                interaction.getThreadId(),
                interaction.getMailId(),
                interaction.getParsedMailId(),
                fileSize,
                fileName,
                IMPORT_CODE));
        queries.add(indexQuery);
        elasticsearchOperations.bulkIndex(queries);
        queries.clear();
        elasticsearchOperations.refresh(InteractionLog.class);*/



        /*interactionLogRepository.save(new InteractionLog(
                new Date(),
                interaction.getTenantId(),
                interaction.getTenantUuid(),
                interaction.getThreadId(),
                interaction.getMailId(),
                interaction.getParsedMailId(),
                fileSize,
                fileName,
                IMPORT_CODE));
        interactionLogRepository.refresh();*/

        /*interactionLogs.add(new InteractionLog(
                new Date(),
                interaction.getTenantId(),
                interaction.getTenantUuid(),
                interaction.getThreadId(),
                interaction.getMailId(),
                interaction.getParsedMailId(),
                fileSize,
                fileName,
                IMPORT_CODE));
*/

    /*    elasticsearchOperations.bulkIndex(new InteractionLog(
                new Date(),
                interaction.getTenantId(),
                interaction.getTenantUuid(),
                interaction.getThreadId(),
                interaction.getMailId(),
                interaction.getParsedMailId(),
                fileSize,
                fileName,
                IMPORT_CODE), Interaction.class);*/
    //}

    public void writeLogs(List<InteractionLog> interactionLogs) {
        int counter = 0;
        List<IndexQuery> queries = new ArrayList<>();

        for (InteractionLog o : interactionLogs) {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(o);
            queries.add(indexQuery);
            if (counter % INDEX_COMMIT_SIZE == 0) {
                elasticsearchOperations.bulkIndex(queries);
                queries.clear();
                logger.debug("saveAllByIndexName counter : {}", counter);
            }
            counter++;
        }
        if (!queries.isEmpty()) {
            elasticsearchOperations.bulkIndex(queries);
        }
        elasticsearchOperations.refresh(InteractionLog.class);
        logger.debug("saveAllByIndexName completed for index Name : {}", InteractionLog.class);
    }


}
