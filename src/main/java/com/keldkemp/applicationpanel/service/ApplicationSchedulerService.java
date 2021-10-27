package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Applications;

public interface ApplicationSchedulerService {

    void handleApplicationUpdate(Applications application);

    void cancelUpdate(Applications application);
}
