package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.DocumentMigrate;
import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import com.javatechie.spring.mongo.binary.api.repository.InteractionRepository;
import com.javatechie.spring.mongo.binary.api.utils.PocUtils;
import com.javatechie.spring.mongo.binary.api.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataMigrationService {
    private static final long NBR_MAX_IMPORT = 40;
    Logger logger = LoggerFactory.getLogger(DataMigrationService.class);

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private GridFsService gridFsService;

    @Autowired
    private PocUtils pocUtils;

    @Autowired
    private Utils utils;


    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public static final int INDEX_COMMIT_SIZE = 5000;


    public DocumentMigrate migrateInteractions() throws IOException, NoSuchAlgorithmException {
        StringBuilder stringBuilder = new StringBuilder();

        LocalDateTime startDate = LocalDateTime.now();
        stringBuilder.append("\n-------- START -------------");

        DocumentMigrate documentMigrate = readInteractionsView(interactionRepository.findAll());
        stringBuilder.append("\n<b>Debut migrateInteractions : </b>" + startDate);
        stringBuilder.append("\n<br><b>fin migrateInteractions : </b>" + LocalDateTime.now());
        stringBuilder.append("\n<br><b>Temps total de la migration (secondes): </b>" + ChronoUnit.SECONDS.between(startDate, LocalDateTime.now()) + " sec");
        documentMigrate.setTime(ChronoUnit.SECONDS.between(startDate, LocalDateTime.now()));
        stringBuilder.append("\n<br><b>Nombre de documents copiés : </b>" + documentMigrate.getNumberOfDocument());
        stringBuilder.append("\n<br><b>Total des données copiées :</b> " + utils.bytesToMeg(documentMigrate.getSizeOfDocument()) + " Mb");
        documentMigrate.setSizeOfDocument(utils.bytesToMeg(documentMigrate.getSizeOfDocument()));

        stringBuilder.append("\n<br>-------- END -------------");
        logger.info(stringBuilder.toString());


        return documentMigrate;
        //readInteractionsRandom();
    }


    public DocumentMigrate readInteractionsView(List<Interaction> interactionList) throws NoSuchAlgorithmException {
        long counter =0;
        long size = 0;
        List<InteractionLog> interactionLogs = new ArrayList<>();
        String importCode = Utils.generateUniqueImportCode();

        for (Interaction interaction : interactionList) {
            try {
                InteractionLog interactionLog = gridFsService.indexInteractionData(interaction, importCode);
                interactionLogs.add(interactionLog);
                counter++;
                size += interactionLog.getAttachedFileSize();

                //TODO to clean for the test
                if(counter == NBR_MAX_IMPORT) {
                    writeLogs(interactionLogs);
                    return new DocumentMigrate(counter++, size);
                }
            } catch (IOException e) {
                logger.error("Erreur d'ecriture - threadId : " + interaction.getThreadId());
                writeLogs(interactionLogs);
                return new DocumentMigrate(counter++, size);
            }
        }
        writeLogs(interactionLogs);
        return new DocumentMigrate(counter++, size);
    }

    private void readInteractionsRandom() {
        List<Interaction> interactionList  = pocUtils.createOtherThread();
        for (Interaction interaction : interactionList) {
            //TODO
            // gridFsService.indexInteractionDataRandom(interaction);

        }

    }

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
                logger.debug("writeLogs counter : {}", counter);
            }
            counter++;
        }
        if (!queries.isEmpty()) {
            elasticsearchOperations.bulkIndex(queries);
        }
        elasticsearchOperations.refresh(InteractionLog.class);
        logger.debug("writeLogs completed for index Name : {}", InteractionLog.class);
    }



}
