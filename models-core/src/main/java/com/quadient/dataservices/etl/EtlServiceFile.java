package com.quadient.dataservices.etl;

import java.io.InputStream;

import com.quadient.dataservices.etl.model.FileInformation;
import com.quadient.dataservices.etl.model.TransformRequest;

public interface EtlServiceFile {

    FileInformation getFileInformation();

    InputStream download();

    EtlServiceTable transformToTable(boolean deleteFile);

    EtlServiceTable transformToTable(TransformRequest transformRequest);

    void deleteFile();

    String getFileId();
}
