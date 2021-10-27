<b>Управление приложениями</b>

Переменные окружения:

~~~
gitHub.token=Token github
spring.datasource.url=Url DB
spring.jpa.properties.hibernate.dialect=Dialect
spring.datasource.username=UsernameDb
spring.datasource.password=PasswordDb
spring.jpa.hibernate.ddl-auto=update
projectsFolderPath=Folder application projects
jwt.secret=JWT Secret
~~~

О работе приложения:<br>
Работает в связки с GitHub и DockerHub. На GitHub список приложений, на DockerHub список Images.

- Берет список приложений с GitHub.<br>
- Умеет управлять приложениями, у которых есть в Action Docker pull.
- Умеет генерировать Роль и Базу Данных для приложения автоматически.
- Умеет генерировать docker-compose для приложения автоматически.
- Умеет обновлять приложения по расписанию.
- Просмотр статуса приложений.
- Управления контейнерами приложения.