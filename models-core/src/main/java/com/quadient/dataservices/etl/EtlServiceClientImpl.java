package com.quadient.dataservices.etl;

import java.io.File;
import java.util.List;

import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.etl.model.FileInformation;
import com.quadient.dataservices.etl.model.TableInformation;

public class EtlServiceClientImpl implements EtlServiceClient {

    private final Client client;

    public EtlServiceClientImpl(Client client) {
        this.client = client;
    }

    @Override
    public EtlServiceFile createFile(File sourceFile) {
        final FileInformation fileInformation = client.execute(new FileUploadRequest(sourceFile));
        return new EtlServiceFileImpl(client, fileInformation);
    }

    @Override
    public EtlServiceFile getFile(String fileId) {
        return new EtlServiceFileImpl(client, fileId);
    }

    @Override
    public void deleteFile(String fileId) {
        getFile(fileId).deleteFile();
    }

    @Override
    public EtlServiceTable createTable(List<String> columnNames) {
        final TableInformation table = client.execute(new TableCreationSchemaRequest(columnNames));
        return new EtlServiceTableImpl(client, table);
    }

    @Override
    public EtlServiceTable createTable(File sourceFile) {
        final TableInformation table = client.execute(new TableCreationFileUploadRequest(sourceFile));
        return new EtlServiceTableImpl(client, table);
    }

    @Override
    public EtlServiceTable getTable(String tableId) {
        return new EtlServiceTableImpl(client, tableId);
    }

    @Override
    public void deleteTable(String tableId) {
        getTable(tableId).deleteTable();
    }
}
