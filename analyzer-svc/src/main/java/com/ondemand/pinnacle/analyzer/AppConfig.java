package com.ondemand.pinnacle.analyzer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

/**
 * @author Chandu D - i861116
 * @created 06/01/2023 - 11:36 AM
 * @description
 */
@Configuration()
@EnableScheduling
@EnableAsync
public class AppConfig {
    @Bean("service-td")
    public Executor asyncExecutorD() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("service-td-");
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("sched-td");
        return scheduler;
    }
}
