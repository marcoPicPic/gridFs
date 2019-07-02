package com.javatechie.spring.mongo.binary.api.utils;

import com.google.common.net.MediaType;
import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Utils {

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
}
