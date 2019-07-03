package com.javatechie.spring.mongo.binary.api.utils;

import com.google.common.net.MediaType;
import com.javatechie.spring.mongo.binary.api.domain.DocumentMigrate;
import com.javatechie.spring.mongo.binary.api.domain.ImportSummary;
import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.domain.InteractionLog;
import com.javatechie.spring.mongo.binary.api.repository.InteractionLogRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@Component
public class Utils {

    String INDEX_NAME = "gridfs_activities";

    @Autowired
    private InteractionLogRepository interactionLogRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    private static final long  MEGABYTE = 1024L * 1024L;

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
        result.put("tenantUuid", interaction.getTenantUuid().trim());
        result.put("threadId", interaction.getThreadId());
        result.put("documentType", getContentType(fileToStore.getName()));
        result.put("emailId", interaction.getMailId());
        result.put("parsedMailId", interaction.getParsedMailId());
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

    public String generateMigrationReport(String importCode) {
        ImportSummary importResultsSummary = getImportResultsSummary(importCode);
        return buildSummaryMessage(importResultsSummary);
    }

    private String buildSummaryMessage(ImportSummary importResultsSummary) {
        StringBuilder summary = new StringBuilder();
        summary.append("\n==================================== IMPORT SUMMARY ====================================");
        summary.append("\n");
        summary.append(" import : " + importResultsSummary.getImportCode());
        Set<Integer> tenantIds = importResultsSummary.getInfosByTenant().keySet();
        for(Integer tenantId : tenantIds) {
            summary.append("tenant id : " + tenantId + " ");
            summary.append("number of documents processed : " + importResultsSummary.getInfosByTenant().get(tenantId).getNumberOfDocument() + " ");
            summary.append("volume of documents processed : " + importResultsSummary.getInfosByTenant().get(tenantId).getSizeOfDocument() + " ");
            summary.append("\n");
        }
        return summary.toString();
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

        interactionLog.setAttachedFileName(possibleFileNames.get(getRandomCeiledNumber(possibleFileNames.size() - 1)));
        interactionLog.setAttachedFileSize(possibleFileSizesInKo.get(getRandomCeiledNumber(possibleFileSizesInKo.size() - 1)));
        interactionLog.setDateImport(date);
        interactionLog.setId(id);
        interactionLog.setImportCode(importCode);
        interactionLog.setMailId(possibleMailIds.get(getRandomCeiledNumber(possibleMailIds.size() - 1)));
        interactionLog.setParsedMailId(possibleParsedMailIds.get(getRandomCeiledNumber(possibleParsedMailIds.size() - 1)));
        interactionLog.setTenantId(possibleTenantIds.get(getRandomCeiledNumber(possibleTenantIds.size() - 1)));
        interactionLog.setThreadId(possibleThreadIds.get(getRandomCeiledNumber(possibleThreadIds.size() - 1)));

        return  interactionLog;
    }

    // * to delete
    public void testMigrationReportGeneration() throws NoSuchAlgorithmException {
        writeFakeMigrationLogs();
        System.out.println(generateMigrationReport("EE6D2378AD4DAA1D38446E08684AAD3B"));
    }

    public int getRandomCeiledNumber(int max) {
        return (int) Math.ceil(Math.random() * max);
    }


    public ImportSummary getImportResultsSummary(String importCode) {
        ImportSummary importSummary = new ImportSummary();
        importSummary.setImportCode(importCode);
        //Iterable<InteractionLog> all = interactionLogRepository.findByImportCode(importCode);
        BoolQueryBuilder filter = new BoolQueryBuilder();

        // import_code
        filter.must(QueryBuilders.termQuery("import_code", importCode));

        //Query
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFilter(filter)
                //.withSort(new FieldSortBuilder(ElasticConstant.DATE_STAT).order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10000))
                .build();

        //Result
        Page<InteractionLog> all = elasticsearchTemplate.queryForPage(
                query,
                InteractionLog.class);

        for(InteractionLog current : all) {
            importSummary.add(current);
        }
        return importSummary;
    }

    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE ;
    }


}
