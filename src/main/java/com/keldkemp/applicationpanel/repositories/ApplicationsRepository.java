package com.keldkemp.applicationpanel.repositories;

import com.keldkemp.applicationpanel.models.Applications;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationsRepository extends CrudRepository<Applications, Long> {

    @Query("SELECT app FROM Applications app WHERE app.isAutoUpdate = 1 and app.statusApplications = 1")
    List<Applications> findAllByIsUpdate();

    @Query("SELECT app FROM Applications app WHERE app.originalName = ?1")
    Applications findByOriginalName(String name);
}
