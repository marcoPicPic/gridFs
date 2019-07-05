package api.service;

import api.domain.Interaction;
import api.domain.InteractionLog;
import api.domain.ParameterMigrate;
import api.repository.InteractionLogRepository;
import api.utils.PocUtils;
import api.utils.Utils;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class GridFsService {

    Logger logger = LoggerFactory.getLogger(GridFsService.class);

    @Autowired
    private Utils utils;

    @Autowired
    private InteractionLogRepository interactionLogRepository;

    @Autowired
    private PocUtils pocUtils;

    @Autowired
    private GridFsOperations gridFsOperations;


    public InteractionLog indexInteractionData(Interaction interaction, ParameterMigrate parameterMigrate) throws IOException {

        logger.info("Interaction threadId : " + interaction.getThreadId());
        File fileToStore = ResourceUtils.getFile("classpath:files/" + pocUtils.getFileNameRandom(parameterMigrate));

        DBObject metaData = utils.generateMetadata(interaction, fileToStore);
        storeDocumentAttachedFile(fileToStore, metaData, (String) metaData.get("documentType"));

        return new InteractionLog(
                new Date(),
                interaction.getTenantId(),
                interaction.getTenantUuid(),
                interaction.getThreadId(),
                interaction.getMailId(),
                interaction.getParsedMailId(),
                fileToStore.length(),
                fileToStore.getName(),
                parameterMigrate.getImportCode());
    }

    private void storeDocumentAttachedFile(File file, DBObject metaData, String contentType) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
        gridFsOperations.store(fileInputStream, file.getName(), contentType,
                metaData);
        fileInputStream.close();
    }

}
