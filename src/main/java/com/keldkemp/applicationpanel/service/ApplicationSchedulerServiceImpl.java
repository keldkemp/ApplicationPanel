package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Applications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class ApplicationSchedulerServiceImpl implements ApplicationSchedulerService {

    private final static String THREAD_NAME = "SCHEDULE_UPDATE_APPLICATION";

    @Autowired
    private ApplicationsService applicationsService;

    private ThreadPoolTaskScheduler executorService;

    private final ConcurrentMap<String, ScheduledFuture<?>> applicationsUpdate = new ConcurrentReferenceHashMap<>();

    public void cancelUpdate(Applications application) {
        String pkApp = "update_app_" + application.getId();
        applicationsUpdate.computeIfPresent(pkApp, (k, v) -> {
            v.cancel(true);
            return null;
        });
    }

    public void handleApplicationUpdate(Applications application) {
        initThread();
        String pkApp = "update_app_" + application.getId();

        applicationsUpdate.computeIfPresent(pkApp, (k, v) -> {
           v.cancel(true);
           return null;
        });

        applicationsUpdate.put(pkApp, scheduleUpdateApp(application.getUpdateDateCronFormat(), application));
    }

    private ScheduledFuture<?> scheduleUpdateApp(String cron, Applications application) {
        CronTrigger cronTrigger = new CronTrigger(cron);
        return executorService.schedule(() -> applicationsService.updateApp(application), cronTrigger);
    }

    private void initThread() {
        if (executorService == null) {
            executorService = new ThreadPoolTaskScheduler();
            executorService.setPoolSize(1);
            executorService.setThreadNamePrefix(THREAD_NAME);
            executorService.initialize();
        }
    }
}
