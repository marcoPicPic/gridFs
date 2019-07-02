package com.javatechie.spring.mongo.binary.api.utils;

import com.google.common.net.MediaType;
import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import com.javatechie.spring.mongo.binary.api.repository.InteractionLogRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import java.io.File;
import java.util.List;

@Component
public class Utils {

    @Autowired
    private InteractionLogRepository interactionLogRepository;

    List<String> possibleFileNames;
    List<Integer> possibleFileSizesInKo;
    List<Integer> possibleTenantIds;
    List<Integer> possibleThreadIds;
    List<Integer> possibleMailIds;
    List<Integer> possibleParsedMailIds;

    {
        possibleFileNames = Arrays.asList("fraise.png", "rapport.pdf", "video_vacances.mp4", "bulletin-paie.docx");
        possibleFileSizesInKo = Arrays.asList(1, 3, 5, 4000, 5000, 8000, 50000000, 100000000, 800000000);
        possibleTenantIds = Arrays.asList(1, 2, 3, 4);
        possibleThreadIds = Arrays.asList(15369, 14978, 12356, 14785, 15968, 18722);
        possibleMailIds = Arrays.asList(50, 60, 70, 80);
        possibleParsedMailIds = Arrays.asList(600, 700, 800, 900, 1000);

    }

    public static final String YYYY_MM_DD_T_HH_MM_SS_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public String getAttachedFilePath(Interaction interaction) {
        String hexaRepresentationOfId = Integer.toHexString(interaction.getAttachedFileId());
        System.out.printf("getAttachedFilePath : " + hexaRepresentationOfId);
        // 00/00/00/02
        return convertToFilePathOnEightDigits(hexaRepresentationOfId);
    }

    private String convertToFilePathOnEightDigits(String hexaRepresentationOfId) {
        String hexaRepresentationOfIdOnEightDigits = adjustStringLengthIfNecessary(hexaRepresentationOfId);
        System.out.println("hexaRepresentationOfIdOnEightDigits" + hexaRepresentationOfIdOnEightDigits);
        if(hexaRepresentationOfIdOnEightDigits.length() == 8) {
            return hexaRepresentationOfIdOnEightDigits.substring(0,1) + Constants.DIRECTORY_SEPERATOR +
                    hexaRepresentationOfIdOnEightDigits.substring(2,3) + Constants.DIRECTORY_SEPERATOR +
                    hexaRepresentationOfIdOnEightDigits.substring(4,5) + Constants.DIRECTORY_SEPERATOR +
                    hexaRepresentationOfIdOnEightDigits.substring(6,7);
        }
        return "";
    }

    private String adjustStringLengthIfNecessary(String hexaRepresentationOfId) {
        if(hexaRepresentationOfId.length() == 8) {
            return hexaRepresentationOfId;
        }
        if(hexaRepresentationOfId.length() < 8) {
            return prefixWithZeros(hexaRepresentationOfId);
        }
        return hexaRepresentationOfId.substring(0, 8);
    }

    private String prefixWithZeros(String hexaRepresentationOfId) {
        String result = hexaRepresentationOfId;
        while(result.length() < 0) {
            result = "0" + result;
        }
        return result;
    }

    public String getContentType(String fileName) {
        if(fileName.endsWith(".pdf")) {
            return MediaType.PDF.toString();
        }
        if(fileName.endsWith(".png")) {
            return MediaType.PNG.toString();
        }
        // * TODO ... complete the media type detection on recurrent media types
        return MediaType.ANY_TYPE.toString();
    }

    public DBObject generateMetadata(Interaction interaction, File fileToStore) {
        DBObject result = new BasicDBObject();
        result.put("tenantId", interaction.getTenantId());
        result.put("tenantUuid", interaction.getTenantUuid());
        result.put("threadId", interaction.getThreadId());
        result.put("documentType", getContentType(fileToStore.getName()));
        return result;
    }

    public static String generateUniqueImportCode() throws NoSuchAlgorithmException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_SSSZ);
        String nowString = simpleDateFormat.format(Date.from( LocalDateTime.now().atZone( ZoneId.systemDefault()).toInstant()));
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(nowString.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest);
    }

    public String generateMigrationReport() {
        return "";
    }

    // * to delete
    public void writeFakeMigrationLogs() throws NoSuchAlgorithmException {
        Instant instant = Instant.now();
        int id = 490;
        String uniqueImportCode = generateUniqueImportCode();
        for(int i = 0; i < 10; i++) {
            interactionLogRepository.save(generateFakeInteractionLog(Date.from(instant), id + i,  uniqueImportCode));
        }
    }

    private InteractionLog generateFakeInteractionLog(Date date, Integer id, String importCode) {
        InteractionLog interactionLog = new InteractionLog();

        interactionLog.setAttachedFileName(possibleFileNames.get(getRandomCeiledNumber(possibleFileNames.size())));
        interactionLog.setAttachedFileSize(possibleFileSizesInKo.get(getRandomCeiledNumber(possibleFileSizesInKo.size())));
        interactionLog.setDateImport(date);
        interactionLog.setId(id);
        interactionLog.setImportCode(importCode);
        interactionLog.setMailId(possibleMailIds.get(getRandomCeiledNumber(possibleMailIds.size())));
        interactionLog.setParsedMailId(possibleParsedMailIds.get(getRandomCeiledNumber(possibleParsedMailIds.size())));
        interactionLog.setTenantId(possibleTenantIds.get(getRandomCeiledNumber(possibleTenantIds.size())));
        interactionLog.setThreadId(possibleThreadIds.get(getRandomCeiledNumber(possibleThreadIds.size())));

        return  interactionLog;
    }

    // * to delete
    public void testMigrationReportGeneration() throws NoSuchAlgorithmException {
        writeFakeMigrationLogs();
        System.out.println(generateMigrationReport());
    }

    public int getRandomCeiledNumber(int max) {
        return (int) Math.ceil(Math.random() * max);
    }


}
