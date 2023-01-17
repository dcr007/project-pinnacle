package com.ondemand.pinnacle.ingestion.analyzer.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Chandu D - i861116
 * @created 21/12/2022 - 2:23 PM
 * @description
 */
// TODO: include event arguments and status
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum IngestionEventStatus {
    NON_OPERATIONAL,
    QUEUED,
    ANALYZING,
    COMPLETE,
    ERROR,
    NEEDS_REFRESH,
    DELETED,
    SYNCED;

//    private boolean notify;
//    private boolean active;
//    private boolean updatable;
//    private boolean nonOperational;
}
