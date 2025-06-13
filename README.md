# Hotel Management REST API

Этот проект представляет собой REST API для управления гостиницами, включая информацию об отелях, их адресах, контактах, времени заселения и удобствах.

## Возможности

- Создание, обновление, удаление и поиск отелей.
- Хранение и управление адресной информацией отеля.
- Управление контактной информацией отеля (email, телефон и т.д.).
- Настройка времени заселения (arrival time).
- Поддержка множества удобств (amenities) для отелей.
- Фильтрация отелей по городу, стране, бренду, имени и списку удобств.

## Технологии

- **Java 17+**
- **Spring Boot 3**
- **Spring Data JPA (Hibernate)**
- **Jakarta Persistence API**
- **Lombok**
- **H2**
- **Swagger/OpenAPI** для документации
- **Maven** для сборки проекта

-Переключение между базами данных
В проекте реализовано гибкое переключение базы данных через параметры в application.yml, используя собственный бин DataSource, основанный на секции custom.datasource.

Файл конфигурации: application.yml
yaml
custom:
  datasource:
    url: jdbc:h2:mem:hotel-db;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password: 1234
Переключиться на другую БД (например, PostgreSQL)
Измени параметры в блоке custom.datasource:

yaml
custom:
  datasource:
    url: jdbc:postgresql://localhost:5432/hotel_db
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: your_password
Убедись, что драйвер PostgreSQL доступен (в pom.xml):

xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
Перезапусти приложение. Никаких дополнительных изменений не требуется — DataSource будет создан на основе новых параметров.


## Структура проекта

- `entity` — JPA-сущности (Hotel, Address, Contacts, ArrivalTime, Amenity)
- `dto` — Data Transfer Objects для запросов и ответов
- `mapper` — Мапперы между DTO и сущностями
- `repository` — Репозитории Spring Data JPA
- `service` — Бизнес-логика
- `controller` — REST-контроллеры
- `exception` — Глобальная обработка ошибок

## Как запустить проект

1. Склонируйте репозиторий:
    ```bash
    git clone https://github.com/your-username/hotel-management.git
    cd hotel-management
    ```

2. Соберите проект с помощью Maven:
    ```bash
    mvn clean install
    ```

3. Запустите приложение:
    ```bash
    mvn spring-boot:run
    ```

4. Перейдите к Swagger UI:
    ```
    http://localhost:8080/swagger-ui.html
    ```

Контакты
Разработчик: [Егор]
Email: [semenovegor546@gmail.com]
