package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.util.StringUtil;
import com.keldkemp.applicationpanel.web.rest.dto.DataBaseDto;
import com.keldkemp.applicationpanel.web.rest.dto.DataBaseRoleDto;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DataBaseServiceImpl implements DataBaseService {

    private final static Integer STRING_LENGTH_PASSWORD = 15;

    @Autowired
    private DataSource dataSource;

    @Override
    public DataBaseDto generateDbAndOwner(Applications applications) {
        String dbName = applications.getOriginalName().split("/")[1];
        DataBaseRoleDto role = createRole(dbName + "User", StringUtil.generateString(STRING_LENGTH_PASSWORD));
        DataBaseDto db = createDataBase(dbName, role.getOwnerName());
        db.setRole(role);
        setUrlDb(db);

        return db;
    }

    @Override
    public DataBaseRoleDto createRole(@NonNull String name, @NonNull String password) {
        getJdbcTemplate().execute(String.format("CREATE USER %s WITH password '%s'", name, password));

        DataBaseRoleDto roleDto = new DataBaseRoleDto();
        roleDto.setOwnerName(name.toLowerCase());
        roleDto.setOwnerPassword(password);

        return roleDto;
    }

    @Override
    public DataBaseDto createDataBase(@NonNull String nameDB, @NonNull String userName) {
        getJdbcTemplate().execute(
                String.format("CREATE DATABASE %s WITH OWNER %s ENCODING='UTF8' LC_CTYPE='en_US.UTF-8' LC_COLLATE='en_US.UTF-8' TEMPLATE=template0;",
                        nameDB, userName));

        DataBaseDto dataBaseDto = new DataBaseDto();
        DataBaseRoleDto roleDto = new DataBaseRoleDto();

        roleDto.setOwnerName(userName.toLowerCase());
        dataBaseDto.setDbName(nameDB.toLowerCase());
        dataBaseDto.setRole(roleDto);

        return dataBaseDto;
    }

    private void setUrlDb(DataBaseDto dataBase) {
        dataBase.setUrl(String.format("postgres://%s:%s@localhost:5435/%s", dataBase.getRole().getOwnerName(),
                dataBase.getRole().getOwnerPassword(), dataBase.getDbName()));
    }

    private JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbc = new JdbcTemplate();
        jdbc.setDataSource(dataSource);

        return jdbc;
    }
}
