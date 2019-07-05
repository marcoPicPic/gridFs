package api.service;

import api.domain.DocumentMigrate;
import api.domain.Interaction;
import api.domain.InteractionLog;
import api.domain.ParameterMigrate;
import api.repository.InteractionLogRepository;
import api.repository.InteractionRepository;
import api.utils.PocUtils;
import api.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private InteractionLogRepository interactionLogRepository;

    @Autowired
    private GridFsService gridFsService;

    @Autowired
    private PocUtils pocUtils;

    @Autowired
    private Utils utils;



    public DocumentMigrate migrateInteractions(int nbFile, boolean little, boolean medium, boolean big) throws IOException, NoSuchAlgorithmException {
        ParameterMigrate parameterMigrate = new ParameterMigrate(nbFile, little, medium, big);
        StringBuilder stringBuilder = new StringBuilder();

        LocalDateTime startDate = LocalDateTime.now();
        stringBuilder.append("\n------------- START -------------");
        DocumentMigrate documentMigrate = readInteractionsView(interactionRepository.findAll(), parameterMigrate);
        stringBuilder.append("\nDebut migrateInteractions : " + startDate);
        stringBuilder.append("\nFin migrateInteractions : " + LocalDateTime.now());
        stringBuilder.append("\nTemps total de la migration (secondes): " + ChronoUnit.SECONDS.between(startDate, LocalDateTime.now()) + " sec");
        documentMigrate.setTime(ChronoUnit.SECONDS.between(startDate, LocalDateTime.now()));
        stringBuilder.append("\nNombre de documents copiés : " + documentMigrate.getNumberOfDocument());
        stringBuilder.append("\nTotal des données copiées : " + utils.bytesToMeg(documentMigrate.getSizeOfDocument()) + " MB");
        documentMigrate.setSizeOfDocument(utils.bytesToMeg(documentMigrate.getSizeOfDocument()));

        stringBuilder.append("\n-------------  -------------\n\n");
        logger.info(stringBuilder.toString());
        documentMigrate.setReport(stringBuilder.toString());

        return documentMigrate;
        //readInteractionsRandom();
    }


    public DocumentMigrate readInteractionsView(List<Interaction> interactionList, ParameterMigrate parameterMigrate) throws NoSuchAlgorithmException {
        long counter =0;
        long size = 0;
        List<InteractionLog> interactionLogs = new ArrayList<>();
        parameterMigrate.setImportCode(Utils.generateUniqueImportCode());

        for (Interaction interaction : interactionList) {
            try {
                InteractionLog interactionLog = gridFsService.indexInteractionData(interaction, parameterMigrate);
                interactionLogs.add(interactionLog);
                counter++;
                size += interactionLog.getAttachedFileSize();

                if(counter == parameterMigrate.getNbFile()) {
                    writeLogs(interactionLogs);
                    return new DocumentMigrate(counter++, size, parameterMigrate.getImportCode());
                }
            } catch (IOException e) {
                logger.error("Erreur d'ecriture - threadId : " + interaction.getThreadId());
                writeLogs(interactionLogs);
                return new DocumentMigrate(counter++, size, parameterMigrate.getImportCode());
            }
        }
        writeLogs(interactionLogs);
        return new DocumentMigrate(counter++, size, parameterMigrate.getImportCode());
    }

    private void readInteractionsRandom() {
        List<Interaction> interactionList  = pocUtils.createOtherThread();
        for (Interaction interaction : interactionList) {
            //TODO
            // gridFsService.indexInteractionDataRandom(interaction);

        }

    }

    public void writeLogs(List<InteractionLog> interactionLogs) {
        interactionLogRepository.saveAll(interactionLogs);
        logger.debug("writeLogs completed for index Name : {}", InteractionLog.class);
    }


}
