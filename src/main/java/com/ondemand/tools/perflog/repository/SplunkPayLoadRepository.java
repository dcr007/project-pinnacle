package com.ondemand.tools.perflog.repository;

import com.ondemand.tools.perflog.models.SplunkPayLoad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandu D - i861116
 * @created 15/11/2022 - 4:14 PM
 * @description
 */
@Repository
public interface SplunkPayLoadRepository extends
        MongoRepository<SplunkPayLoad,String> {
}
