package com.ondemand.pinnacle.analyzer.models;

import com.ondemand.pinnacle.ingestion.models.PerfLog;

/**
 * @author Chandu D - i861116
 * @created 13/12/2022 - 4:03 PM
 * @description
 */
public interface PerfLogSummary<O> {

    public O generateSummary(PerfLog perfLog);
}
