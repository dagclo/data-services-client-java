package com.quadient.dataservices.etl;

import java.io.File;
import java.util.List;

/**
 * A client abstraction for interacting with the Quadient Data Services ETL service.
 */
public interface EtlServiceClient {
    
    EtlServiceFile createFile(File sourceFile);
    
    EtlServiceFile getFile(String fileId);
    
    void deleteFile(String fileId);
    
    EtlServiceTable createTable(List<String> columnNames);

    EtlServiceTable createTable(File sourceFile);

    EtlServiceTable getTable(String tableId);
    
    void deleteTable(String tableId);
}
