export class ApplicationDto {

    constructor(id, name, original_name, github_url, is_auto_update, last_update, status_applications, update_date_cron_format, github_repo) {
        if (id != null)
            this.id = id;
        if (name != null)
            this.name = name;
        this.original_name = original_name;
        if (github_url != null)
            this.github_url = github_url;
        if (is_auto_update != null)
            this.is_auto_update = is_auto_update;
        if (last_update != null)
            this.last_update = last_update;
        if (status_applications != null)
            this.status_applications = status_applications;
        if (update_date_cron_format != null)
            this.update_date_cron_format = update_date_cron_format;
        this.github_repo = github_repo;
    }
}