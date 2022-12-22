package com.ondemand.pinnacle.ingestion.entities;

import com.ondemand.pinnacle.analyzer.models.PerfLogIngestionEvent;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author Chandu D - i861116
 * @created 21/12/2022 - 11:49 AM
 * @description
 */
@Entity
@NoArgsConstructor
@Table(name = "ingestion_svc_event_queue")
public class IngestionEventQueueEntity {
    @Id
    @SequenceGenerator(
            name="seq",
            sequenceName = "ingestion_svc_event_queue_id_seq",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator="seq")
    @Column(name = "id", updatable = false, nullable = false)
    @Getter @Setter
    private Long id;

    @Column(nullable = false, name = "perf_log_id")
    @Getter @Setter
    private String perfLogId;

    @Column(columnDefinition = "TIMESTAMP",
            name = "perf_log_ts",nullable = false)
    @Getter @Setter
    private LocalDateTime perfLogTs;

    @Column(nullable = false, name = "perf_log_ingestion_event")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private PerfLogIngestionEvent perfLogIngestionEvent = PerfLogIngestionEvent.NA;

    @Column(name="perf_log_ingestion_event_error")
    @Getter @Setter
    private String perfLogIngestionEventError;

    @Column(name="perf_log_ingestion_event_last_changed_at")
    @Getter
    private Long perfLogIngestionEventLastChangedAt;

    @PrePersist
    private  void prePersist(){
        this.perfLogIngestionEventLastChangedAt = OffsetDateTime.now().toInstant().toEpochMilli();
    }

    @PreUpdate
    private void preUpdate(){
        this.perfLogIngestionEventLastChangedAt = OffsetDateTime.now().toInstant().toEpochMilli();
    }


    public IngestionEventQueueEntity(String perfLogId, LocalDateTime perfLogTs, PerfLogIngestionEvent perfLogIngestionEvent, String perfLogIngestionEventError) {
        this.perfLogId = perfLogId;
        this.perfLogTs = perfLogTs;
        this.perfLogIngestionEvent = perfLogIngestionEvent;
        this.perfLogIngestionEventError = perfLogIngestionEventError;
    }
}
