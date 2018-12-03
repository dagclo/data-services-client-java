package com.quadient.dataservices.etl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadient.dataservices.etl.model.FileInformation;

public class FileInformationTest {

    // Ensures that deserialization of responses is possible. We had to make a modification to the ETL service because
    // of the Record definition which extends array, and swagger-codegen trips over it.
    @Test
    public void testResponseIsDeserializable() throws Exception {
        // an almost-actual historic response
        final String historicResponse =
                "{\"file_id\":\"FILE_ID_IS_HERE\",\"uri\":\"/etl/v1/files/FILE_ID_IS_HERE\",\"file_type\":\"csv\",\"tables\":[{\"index\":0,\"name\":\"quadient_data_services_test5861935298119219761.csv\",\"columns\":[{\"index\":0,\"name\":\"col1\",\"suggested_field\":null},{\"index\":1,\"name\":\"col2\",\"suggested_field\":null},{\"index\":2,\"name\":\"col3\",\"suggested_field\":null},{\"index\":3,\"name\":\"col4\",\"suggested_field\":null},{\"index\":4,\"name\":\"col5\",\"suggested_field\":null},{\"index\":5,\"name\":\"col6\",\"suggested_field\":null},{\"index\":6,\"name\":\"col7\",\"suggested_field\":null},{\"index\":7,\"name\":\"col8\",\"suggested_field\":null},{\"index\":8,\"name\":\"col9\",\"suggested_field\":null},{\"index\":9,\"name\":\"col10\",\"suggested_field\":null}],\"sample_records\":[[\"value475225\",\"value2578\",\"value765887\",\"value355189\",\"value228282\",\"value108378\",\"value423456\",\"value153032\",\"value345702\",\"value540604\"],[\"value556498\",\"value166679\",\"value326829\",\"value242993\",\"value49350\",\"value220944\",\"value300483\",\"value893143\",\"value685675\",\"value746832\"],[\"value338189\",\"value240872\",\"value410758\",\"value431435\",\"value416335\",\"value725383\",\"value416048\",\"value455636\",\"value907191\",\"value438193\"],[\"value870636\",\"value641156\",\"value882411\",\"value210084\",\"value520652\",\"value893279\",\"value971141\",\"value619597\",\"value959598\",\"value325271\"],[\"value213437\",\"value407455\",\"value535143\",\"value220305\",\"value873704\",\"value979091\",\"value667828\",\"value417207\",\"value865552\",\"value566889\"]]}]}";

        final FileInformation fileInformation = new ObjectMapper().readValue(historicResponse, FileInformation.class);

        assertEquals("FILE_ID_IS_HERE", fileInformation.getFileId());
    }
}
