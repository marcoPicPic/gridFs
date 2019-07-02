package com.javatechie.spring.mongo.binary.api.utils;

import com.google.common.net.MediaType;
import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import java.io.File;

@Component
public class Utils {

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

    public static String generateMigrationReport() {
        return "";
    }

    // * to delete
    public static void writeFakeMigrationLogs() {

    }

    // * to delete
    public static void testMigrationReportGeneration() {
        writeFakeMigrationLogs();
        System.out.println(generateMigrationReport());
    }


}
