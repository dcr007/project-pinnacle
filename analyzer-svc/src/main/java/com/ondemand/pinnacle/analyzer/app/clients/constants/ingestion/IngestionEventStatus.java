package com.ondemand.pinnacle.analyzer.app.clients.constants.ingestion;

/**
 * @author Chandu D - i861116
 * @created 21/12/2022 - 2:23 PM
 * @description
 */
public enum IngestionEventStatus {
    NA,
    QUEUED,
    ANALYZING,
    ANOMALIES_DETECTED_TRUE,
    ANOMALIES_DETECTED_FALSE,
    ANALYZING_COMPLETE,
    ERROR
}
