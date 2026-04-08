# Руководство разработчика FPSK Guide Bot

## Содержание

1. [Архитектура](#архитектура)
2. [Структура проекта](#структура-проекта)
3. [Технологический стек](#технологический-стек)
4. [Настройка окружения](#настройка-окружения)
5. [Модели данных](#модели-данных)
6. [Сервисы](#сервисы)
7. [Многоязычность](#многоязычность)
8. [Миграции БД](#миграции-бд)

## Архитектура

### Общая схема

```
┌─────────────────┐
│  Telegram API   │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│    FpskBot      │ ← Main Entry Point
│  (TelegramBot)  │
└────────┬────────┘
         │
    ┌────┴────┬──────────┬──────────┬──────────┐
    ↓         ↓          ↓          ↓          ↓
┌─────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│Command  │ │Request │ │Message │ │UserState│ │Language│
│Service  │ │Service │ │Service │ │Service  │ │Service │
└────┬────┘ └───┬────┘ └───┬────┘ └───┬────┘ └────────┘
     │          │           │          │
     └──────────┴───────────┴──────────┘
                    ↓
         ┌──────────────────┐
         │ Spring Data JPA  │
         └─────────┬────────┘
                   ↓
         ┌──────────────────┐
         │   PostgreSQL     │
         └──────────────────┘
```

### Паттерны проектирования

- **Service Layer Pattern** - бизнес-логика изолирована в сервисах
- **Repository Pattern** - доступ к данным через Spring Data JPA
- **State Pattern** - управление состояниями пользователя
- **Strategy Pattern** - обработка разных типов сообщений

## Структура проекта

```
FPSKGuideBot/
├── src/
│   ├── main/
│   │   ├── java/com/example/fpskguidebot/
│   │   │   ├── config/
│   │   │   │   └── BotConfig.java
│   │   │   ├── enums/
│   │   │   │   └── Language.java
│   │   │   ├── model/
│   │   │   │   ├── UserRequest.java
│   │   │   │   └── UserState.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRequestRepository.java
│   │   │   │   └── UserStateRepository.java
│   │   │   ├── service/
│   │   │   │   ├── CommandService.java
│   │   │   │   ├── LanguageService.java
│   │   │   │   ├── MessageService.java
│   │   │   │   ├── RequestService.java
│   │   │   │   └── UserStateService.java
│   │   │   ├── FpskBot.java
│   │   │   └── FpskGuideBotApplication.java
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   └── V1__Initial_schema.sql
│   │       └── application.properties
│   └── test/
├── pom.xml
├── README.md
├── QUICK_START.md
├── CUSTOMER_GUIDE.md
└── DEVELOPER_GUIDE.md
```

## Технологический стек

### Backend
- **Java 17** - LTS версия
- **Spring Boot 3.4.1** - фреймворк приложения
- **Spring Data JPA** - ORM
- **Hibernate** - JPA провайдер

### Database
- **PostgreSQL 12+** - реляционная БД
- **Flyway** - миграции БД

### Telegram Integration
- **TelegramBots 6.9.7.1** - Java библиотека для Telegram Bot API

### Build
- **Maven 3.8+** - система сборки

## Настройка окружения

### 1. Установка зависимостей

```bash
# Java 17
sudo apt install openjdk-17-jdk

# Maven
sudo apt install maven

# PostgreSQL
sudo apt install postgresql postgresql-contrib
```

### 2. Конфигурация базы данных

```sql
CREATE DATABASE fpsk_dataBase;
CREATE USER fpsk_user WITH ENCRYPTED PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE fpsk_dataBase TO fpsk_user;
```

### 3. Переменные окружения

```bash
export BOT_TOKEN="1234567890:ABCdefGHIjklMNOpqrsTUVwxyz"
export BOT_USERNAME="@FPSKGuide_bot"
export OPERATOR_CHAT_ID="123456789"
export DB_URL="jdbc:postgresql://localhost:5432/fpsk_dataBase"
export DB_USERNAME="fpsk_user"
export DB_PASSWORD="secure_password"
```

### 4. application.properties

```properties
# Telegram Bot
bot.token=${BOT_TOKEN}
bot.username=${BOT_USERNAME:@FPSKGuide_bot}
operator.chat.id=${OPERATOR_CHAT_ID}

# Database
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/fpsk_dataBase}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

## Модели данных

### UserState

```java
@Entity
@Table(name = "user_states")
public class UserState {
    @Id
    private Long chatId;
    private String state;
    private String language;
    private String tempName;
    private String tempPhone;
    private LocalDateTime lastRequestTime;
}
```

**Состояния:**
- `IDLE` - главное меню
- `WAITING_NAME` - ожидание имени
- `WAITING_PHONE` - ожидание телефона
- `WAITING_MESSAGE` - ожидание текста обращения

### UserRequest

```java
@Entity
@Table(name = "user_requests")
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String userName;
    private String phoneNumber;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private String answer;
    private LocalDateTime answeredAt;
}
```

**Статусы:**
- `PENDING` - ожидает ответа
- `ANSWERED` - оператор ответил

## Сервисы

### CommandService
Обрабатывает команды: `/start`, `/help`, `/lang`, `/list`, `/answer`

### RequestService
Управляет обращениями пользователей, включая rate limiting (5 минут между обращениями)

### UserStateService
Управляет состояниями пользователей в диалоге

### MessageService
Отправляет сообщения и создает клавиатуры

### LanguageService
Предоставляет локализованные тексты для RU/KG/EN

## Многоязычность

### Enum Language

```java
public enum Language {
    RU("ru", "Русский 🇷🇺"),
    KG("kg", "Кыргызча 🇰🇬"),
    EN("en", "English 🇬🇧");
}
```

### Добавление нового языка

1. Добавить в enum `Language`
2. Добавить переводы в `LanguageService`
3. Обновить клавиатуру выбора языка

## Миграции БД

### Flyway автоматически:
1. Создает таблицу `flyway_schema_history`
2. Выполняет миграции из `src/main/resources/db/migration/`
3. Отслеживает версии

### Добавление новой миграции

Создайте файл `V2__Add_feature.sql`:

```sql
ALTER TABLE user_requests ADD COLUMN email VARCHAR(255);
CREATE INDEX idx_user_requests_email ON user_requests(email);
```

### Правила именования
- Формат: `V{номер}__{описание}.sql`
- Примеры: `V1__Initial_schema.sql`, `V2__Add_email.sql`

## Валидация данных

**Имя:**
```java
private boolean isValidName(String name) {
    return name != null && name.length() >= 2 && name.length() <= 50;
}
```

**Телефон:**
```java
private boolean isValidPhone(String phone) {
    return phone != null && phone.matches("^\\+996\\d{9}$");
}
```

**Сообщение:**
```java
private boolean isValidMessage(String message) {
    return message != null && message.length() >= 10 && message.length() <= 1000;
}
```

## Запуск и тестирование

### Локальный запуск
```bash
mvn spring-boot:run
```

### Сборка JAR
```bash
mvn clean package -DskipTests
```

### Тестирование
```bash
mvn test
```

## Развертывание

### Systemd Service (Linux)

```ini
[Unit]
Description=FPSK Guide Bot
After=network.target postgresql.service

[Service]
Type=simple
User=fpsk
WorkingDirectory=/opt/fpsk-bot
ExecStart=/usr/bin/java -jar FPSKGuideBot-0.0.1-SNAPSHOT.jar
Restart=always

Environment="BOT_TOKEN=your_token"
Environment="BOT_USERNAME=@your_bot"
Environment="OPERATOR_CHAT_ID=123456789"
Environment="DB_USERNAME=postgres"
Environment="DB_PASSWORD=password"

[Install]
WantedBy=multi-user.target
```

### Docker

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/FPSKGuideBot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Troubleshooting

### Бот не отвечает
1. Проверить логи
2. Проверить токен бота
3. Проверить подключение к БД

### Ошибки БД
```bash
psql -U postgres -d fpsk_dataBase
\dt
SELECT * FROM user_states;
```

### Проблемы с миграциями
```sql
SELECT * FROM flyway_schema_history;
```

## Контакты

📧 Email: dev@fpsk.kg  
💬 Telegram: @fpsk_dev

---

**Happy Coding!** 🚀
