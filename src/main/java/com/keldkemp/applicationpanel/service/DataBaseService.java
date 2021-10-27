package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.web.rest.dto.DataBaseDto;
import com.keldkemp.applicationpanel.web.rest.dto.DataBaseRoleDto;
import lombok.NonNull;

public interface DataBaseService {

    DataBaseRoleDto createRole(@NonNull String name, @NonNull String password);

    DataBaseDto createDataBase(@NonNull String nameDB, @NonNull String userName);

    DataBaseDto generateDbAndOwner(Applications applications);
}
