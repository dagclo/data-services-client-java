package com.quadient.dataservices.etl;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.etl.model.TableInformation;
import com.quadient.dataservices.etl.model.TablePage;
import com.quadient.dataservices.etl.model.TableUpdateResponse;

class EtlServiceTableImpl implements EtlServiceTable {

    private final Client client;
    private final String tableId;
    private List<String> tableColumns;

    public EtlServiceTableImpl(Client client, String tableId) {
        this(client, tableId, null);
    }

    public EtlServiceTableImpl(Client client, TableInformation tableInformation) {
        this(client, tableInformation.getTableId(), tableInformation.getColumnNames());
    }

    public EtlServiceTableImpl(Client client, String tableId, List<String> columnNames) {
        this.client = client;
        this.tableId = tableId;
        this.tableColumns = columnNames;
    }

    @Override
    public List<String> getColumnNames() {
        if (tableColumns == null) {
            tableColumns = getTableInformation().getColumnNames();
        }
        return tableColumns;
    }

    @Override
    public List<String> getPageIds() {
        return getTableInformation().getPageIds();
    }

    private TableInformation getTableInformation() {
        return client.execute(new TableInformationRequest(tableId));
    }

    @Override
    public TableUpdateResponse appendFile(File file) {
        return client.execute(new TableAppendFileUploadRequest(tableId, file));
    }

    @Override
    public TableUpdateResponse appendRecords(List<List<String>> records) {
        return client.execute(new TableAppendRecordsRequest(tableId, records));
    }

    @Override
    public TablePage getPage(String pageId) {
        return client.execute(new TablePageRequest(tableId, pageId));
    }

    @Override
    public void deletePage(String pageId) {
        client.execute(new TablePageDeleteRequest(tableId, pageId));
    }

    @Override
    public void deleteTable() {
        client.execute(new TableDeleteRequest(tableId));
    }

    @Override
    public InputStream downloadAsCsv() {
        return client.execute(new TableDownloadAsCsvRequest(tableId));
    }

    @Override
    public InputStream downloadAsCsv(boolean includeHeader, char separator, char quote, char escape) {
        return client.execute(new TableDownloadAsCsvRequest(tableId, includeHeader, separator, quote, escape));
    }

    @Override
    public String getTableId() {
        return tableId;
    }

    @Override
    public String toString() {
        return "EtlServiceTable[" + tableId + "]";
    }
}
