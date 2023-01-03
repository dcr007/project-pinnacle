package com.ondemand.pinnacle.analyzer.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ondemand.pinnacle.analyzer.services.PerfLogAnalyzerService;
import com.ondemand.pinnacle.analyzer.models.DwrAnalysisResult;
import com.ondemand.pinnacle.analyzer.models.PerfLog;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chandu D - i861116
 * @created 13/12/2022 - 10:41 PM
 * @description
 */
@RestController
@RequestMapping(value = "api/internal/analyzer")
@Slf4j
@AllArgsConstructor
public class SplunkDataAnalyzers {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private PerfLogAnalyzerService analyzePerfLogService;

    @PostMapping("/getDwrAnalysisResult")
    public ResponseEntity<JsonNode>

    parseDwrPerfLogStack(@RequestBody PerfLog perfLog)  {
        log.info("perfLog stack received :\n{}", perfLog);

        DwrAnalysisResult dwrAnalysisResult = (DwrAnalysisResult) analyzePerfLogService.generateReport(perfLog);

        log.info("Dwr AnalysisResult generated : -\n {}",dwrAnalysisResult.getDwrAnalysisResult().toPrettyString());

        return ResponseEntity.ok(dwrAnalysisResult.getDwrAnalysisResult());
    }

}
