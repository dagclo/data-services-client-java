package com.quadient.dataservices.etl;

import java.io.InputStream;

import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.etl.model.FileInformation;
import com.quadient.dataservices.etl.model.TransformRequest;
import com.quadient.dataservices.etl.model.TransformResponse;

class EtlServiceFileImpl implements EtlServiceFile {

    private final Client client;
    private final String fileId;
    private FileInformation fileInformation;

    public EtlServiceFileImpl(Client client, String fileId) {
        this.client = client;
        this.fileId = fileId;
    }

    public EtlServiceFileImpl(Client client, FileInformation fileInformation) {
        this.client = client;
        this.fileId = fileInformation.getFileId();
        this.fileInformation = fileInformation;
    }

    @Override
    public EtlServiceTable transformToTable(boolean deleteFile) {
        return transformToTable(new TransformRequest().deleteAfterTransform(deleteFile));
    }

    @Override
    public EtlServiceTable transformToTable(TransformRequest transformRequest) {
        final FileTransformToTableRequest req = new FileTransformToTableRequest(fileId, transformRequest);
        final TransformResponse transformResponse = client.execute(req);
        return new EtlServiceTableImpl(client, transformResponse.getTableId(), transformResponse.getColumnNames());
    }

    @Override
    public FileInformation getFileInformation() {
        if (fileInformation == null) {
            fileInformation = client.execute(new FileInformationRequest(fileId));
        }
        return fileInformation;
    }

    @Override
    public void deleteFile() {
        client.execute(new FileDeleteRequest(fileId));
    }

    @Override
    public InputStream download() {
        return client.execute(new FileDownloadRequest(fileId));
    }

    @Override
    public String getFileId() {
        return fileId;
    }

    @Override
    public String toString() {
        return "EtlServiceFile[" + fileId + "]";
    }
}
