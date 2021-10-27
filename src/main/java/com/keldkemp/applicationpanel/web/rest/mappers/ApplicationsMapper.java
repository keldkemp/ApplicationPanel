package com.keldkemp.applicationpanel.web.rest.mappers;

import com.keldkemp.applicationpanel.github.GitHubService;
import com.keldkemp.applicationpanel.github.domain.GitHubUserRepo;
import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.web.rest.dto.ApplicationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ApplicationsMapper {

    @Autowired
    private GitHubService gitHubService;

    public List<ApplicationDto> applicationsDto(List<Applications> applications) {
        return applications.stream()
                .map(obj -> applicationDto(obj, gitHubService.getUserRepo(obj.getOriginalName())))
                .collect(Collectors.toList());
    }

    public ApplicationDto applicationDto(Applications applications) {
        return applicationDto(applications, gitHubService.getUserRepo(applications.getOriginalName()));
    }

    private ApplicationDto applicationDto(Applications application, GitHubUserRepo gitHubUserRepo) {
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setId(application.getId());
        applicationDto.setName(application.getName());
        applicationDto.setOriginalName(application.getOriginalName());
        applicationDto.setGitHubUrl(application.getGitHubUrl());
        applicationDto.setLastUpdate(application.getLastUpdate());
        applicationDto.setStatusApplications(application.getStatusApplications());
        applicationDto.setIsAutoUpdate(application.getIsAutoUpdate());
        applicationDto.setUpdateDateCronFormat(application.getUpdateDateCronFormat());
        applicationDto.setGitHubUserRepo(gitHubUserRepo);

        return applicationDto;
    }

    @Mappings({
            @Mapping(target = "id", source =
                    "applicationDto.id")
    })
    public abstract Applications toApplications(ApplicationDto applicationDto);
}
