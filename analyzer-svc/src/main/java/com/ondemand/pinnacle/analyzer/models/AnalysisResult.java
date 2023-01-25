package com.ondemand.pinnacle.analyzer.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

/**
 * @author Chandu D - i861116
 * @created 13/12/2022 - 5:35 PM
 * @description
 */
public interface AnalysisResult {
//    O generateReport(PerfLog perfLog) throws JsonProcessingException;
     default  JsonNode merge(JsonNode mainNode, JsonNode updateNode) {

        Iterator<String> fieldNames = updateNode.fieldNames();

        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            JsonNode jsonNode = mainNode.get(fieldName);
            // if field exists and is an embedded object
            if (jsonNode != null && jsonNode.isObject()) {
                merge(jsonNode, updateNode.get(fieldName));
            }
            else {
                if (mainNode instanceof ObjectNode) {
                    // Overwrite field
                    JsonNode value = updateNode.get(fieldName);
                    ((ObjectNode) mainNode).replace(fieldName, value);
                }
            }

        }

        return mainNode;
    }
}
