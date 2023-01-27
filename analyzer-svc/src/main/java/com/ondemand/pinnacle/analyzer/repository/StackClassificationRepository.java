package com.ondemand.pinnacle.analyzer.repository;

import com.ondemand.pinnacle.analyzer.models.StackClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 19/01/2023 - 9:26 PM
 * @description
 */
@Repository
public interface StackClassificationRepository extends JpaRepository<StackClassification, Long> {
    List<StackClassification> findByPerfLogId(String perfLogId);
}
