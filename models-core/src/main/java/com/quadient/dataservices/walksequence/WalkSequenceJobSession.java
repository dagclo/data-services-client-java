package com.quadient.dataservices.walksequence;

import com.quadient.dataservices.walksequence.model.JobSummary;
import com.quadient.dataservices.walksequence.model.RecordPages;
import com.quadient.dataservices.walksequence.model.RecordTables;
import com.quadient.dataservices.walksequence.model.Records;

public interface WalkSequenceJobSession {

    String getJobId();
    
    void uploadRecords(Records records);
    
    void run();
    
    JobSummary getJobSummary();
    
    RecordTables getRecordTables();
    
    RecordPages getInputRecordPages();
    
    RecordPages getOutputRecordPages();
    
    Records downloadRecords(String pageId);
}
