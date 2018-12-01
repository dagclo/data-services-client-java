package com.quadient.dataservices.etl;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.quadient.dataservices.etl.model.TablePage;
import com.quadient.dataservices.etl.model.TableUpdateResponse;

public interface EtlServiceTable {

    List<String> getColumnNames();
    
    List<String> getPageIds();

    TableUpdateResponse appendFile(File file);

    TableUpdateResponse appendRecords(List<List<String>> records);

    TablePage getPage(String pageId);

    void deletePage(String pageId);

    void deleteTable();

    InputStream downloadAsCsv();

    InputStream downloadAsCsv(boolean includeHeader, char separator, char quote, char escape);

    String getTableId();
}
