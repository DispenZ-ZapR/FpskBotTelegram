# Инструкция по развертыванию FPSK Guide Bot

## Для заказчика

### Что вы получаете

1. **Исходный код проекта** - полный код бота на Java + Spring Boot
2. **База данных PostgreSQL** - для хранения обращений и состояний пользователей
3. **Документация**:
   - `README.md` - краткое описание и быстрый старт
   - `CUSTOMER_GUIDE.md` - руководство пользователя
   - `DEVELOPER_GUIDE.md` - техническая документация
   - `DEPLOYMENT.md` - этот файл

### Варианты развертывания

## Вариант 1: Локальный сервер (Windows/Linux)

### Требования
- Java 17 или выше
- PostgreSQL 12 или выше
- 1 GB RAM минимум
- Постоянное подключение к интернету

### Шаг 1: Установка Java

**Windows:**
```cmd
# Скачайте и установите с https://adoptium.net/
# Проверьте установку:
java -version
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

**Mac:**
```bash
brew install openjdk@17
```

### Шаг 2: Установка PostgreSQL

**Windows:**
```
1. Скачайте с https://www.postgresql.org/download/windows/
2. Установите с паролем для пользователя postgres
3. Запомните порт (по умолчанию 5432)
```

**Linux:**
```bash
sudo apt install postgresql
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Шаг 3: Создание базы данных

```sql
# Подключитесь к PostgreSQL
psql -U postgres

# Создайте базу данных
CREATE DATABASE fpsk_dataBase;

# Создайте пользователя (опционально)
CREATE USER fpsk_user WITH PASSWORD 'secure_password_here';
GRANT ALL PRIVILEGES ON DATABASE fpsk_dataBase TO fpsk_user;

# Выход
\q
```

### Шаг 4: Настройка переменных окружения

**Windows (через cmd):**
```cmd
setx BOT_TOKEN "ваш_токен_от_BotFather"
setx BOT_USERNAME "@FPSKGuide_bot"
setx OPERATOR_CHAT_ID "ваш_chat_id"
setx DB_URL "jdbc:postgresql://localhost:5432/fpsk_dataBase"
setx DB_USERNAME "postgres"
setx DB_PASSWORD "ваш_пароль_postgres"
```

**Linux (добавьте в ~/.bashrc или /etc/environment):**
```bash
export BOT_TOKEN="ваш_токен_от_BotFather"
export BOT_USERNAME="@FPSKGuide_bot"
export OPERATOR_CHAT_ID="ваш_chat_id"
export DB_URL="jdbc:postgresql://localhost:5432/fpsk_dataBase"
export DB_USERNAME="postgres"
export DB_PASSWORD="ваш_пароль_postgres"

# Применить изменения
source ~/.bashrc
```

### Шаг 5: Сборка проекта

```bash
# Перейдите в папку проекта
cd FPSKGuideBot

# Соберите проект
mvn clean package

# JAR файл будет создан в target/FPSKGuideBot-0.0.1-SNAPSHOT.jar
```

### Шаг 6: Запуск бота

**Вручную:**
```bash
java -jar target/FPSKGuideBot-0.0.1-SNAPSHOT.jar
```

**Как служба Windows (рекомендуется):**

Создайте файл `start-bot.bat`:
```batch
@echo off
set BOT_TOKEN=ваш_токен
set BOT_USERNAME=@FPSKGuide_bot
set OPERATOR_CHAT_ID=ваш_chat_id
set DB_URL=jdbc:postgresql://localhost:5432/fpsk_dataBase
set DB_USERNAME=postgres
set DB_PASSWORD=ваш_пароль

java -jar FPSKGuideBot-0.0.1-SNAPSHOT.jar
```

Используйте **NSSM** для создания службы Windows:
```cmd
# Скачайте NSSM с https://nssm.cc/download
nssm install FPSKBot "C:\path\to\start-bot.bat"
nssm start FPSKBot
```

**Как служба Linux (systemd):**

Создайте файл `/etc/systemd/system/fpsk-bot.service`:
```ini
[Unit]
Description=FPSK Guide Bot
After=network.target postgresql.service

[Service]
Type=simple
User=fpsk
WorkingDirectory=/opt/fpsk-bot
ExecStart=/usr/bin/java -jar /opt/fpsk-bot/FPSKGuideBot-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10

Environment="BOT_TOKEN=ваш_токен"
Environment="BOT_USERNAME=@FPSKGuide_bot"
Environment="OPERATOR_CHAT_ID=ваш_chat_id"
Environment="DB_URL=jdbc:postgresql://localhost:5432/fpsk_dataBase"
Environment="DB_USERNAME=postgres"
Environment="DB_PASSWORD=ваш_пароль"

[Install]
WantedBy=multi-user.target
```

Запустите службу:
```bash
sudo systemctl daemon-reload
sudo systemctl enable fpsk-bot
sudo systemctl start fpsk-bot
sudo systemctl status fpsk-bot
```

---

## Вариант 2: VPS/Облачный сервер (Рекомендуется для продакшена)

### Подходящие провайдеры
- **DigitalOcean** - от $6/месяц
- **Hetzner** - от €4/месяц
- **AWS EC2** - от $5/месяц
- **Yandex Cloud** - от 500₽/месяц

### Минимальные требования сервера
- 1 vCPU
- 1 GB RAM
- 20 GB SSD
- Ubuntu 22.04 LTS

### Быстрое развертывание на Ubuntu

```bash
# 1. Обновите систему
sudo apt update && sudo apt upgrade -y

# 2. Установите Java 17
sudo apt install openjdk-17-jdk -y

# 3. Установите PostgreSQL
sudo apt install postgresql postgresql-contrib -y

# 4. Настройте PostgreSQL
sudo -u postgres psql
CREATE DATABASE fpsk_dataBase;
CREATE USER fpsk_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE fpsk_dataBase TO fpsk_user;
\q

# 5. Создайте пользователя для бота
sudo useradd -m -s /bin/bash fpsk
sudo mkdir -p /opt/fpsk-bot
sudo chown fpsk:fpsk /opt/fpsk-bot

# 6. Загрузите JAR файл на сервер
scp target/FPSKGuideBot-0.0.1-SNAPSHOT.jar user@server:/opt/fpsk-bot/

# 7. Создайте systemd службу (см. выше)

# 8. Запустите бота
sudo systemctl start fpsk-bot
sudo systemctl enable fpsk-bot
```

---

## Вариант 3: Docker

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/FPSKGuideBot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:12
    container_name: fpsk-postgres
    environment:
      POSTGRES_DB: fpsk_dataBase
      POSTGRES_USER: fpsk_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    restart: unless-stopped

  bot:
    build: .
    container_name: fpsk-bot
    depends_on:
      - postgres
    environment:
      BOT_TOKEN: ${BOT_TOKEN}
      BOT_USERNAME: ${BOT_USERNAME}
      OPERATOR_CHAT_ID: ${OPERATOR_CHAT_ID}
      DB_URL: jdbc:postgresql://postgres:5432/fpsk_dataBase
      DB_USERNAME: fpsk_user
      DB_PASSWORD: ${DB_PASSWORD}
    restart: unless-stopped

volumes:
  postgres_data:
```

### Запуск через Docker

```bash
# 1. Создайте .env файл
cat > .env << EOF
BOT_TOKEN=ваш_токен
BOT_USERNAME=@FPSKGuide_bot
OPERATOR_CHAT_ID=ваш_chat_id
DB_PASSWORD=secure_password
EOF

# 2. Соберите проект
mvn clean package

# 3. Запустите контейнеры
docker-compose up -d

# 4. Проверьте логи
docker-compose logs -f bot
```

---

## Получение необходимых данных

### 1. Получение BOT_TOKEN

1. Откройте Telegram
2. Найдите **@BotFather**
3. Отправьте команду `/newbot`
4. Следуйте инструкциям
5. Скопируйте токен

### 2. Получение OPERATOR_CHAT_ID

**Способ 1:**
1. Найдите **@userinfobot**
2. Отправьте ему сообщение
3. Скопируйте Id

**Способ 2:**
```
https://api.telegram.org/bot<ВАШ_BOT_TOKEN>/getUpdates
```

---

## Мониторинг и обслуживание

### Просмотр логов

```bash
# Linux
sudo journalctl -u fpsk-bot -f --lines=100

# Docker
docker-compose logs -f bot
```

### Перезапуск бота

```bash
# Linux
sudo systemctl restart fpsk-bot

# Docker
docker-compose restart bot
```

### Обновление бота

```bash
# 1. Остановите бота
sudo systemctl stop fpsk-bot

# 2. Замените JAR файл
sudo cp new-version.jar /opt/fpsk-bot/FPSKGuideBot-0.0.1-SNAPSHOT.jar

# 3. Запустите бота
sudo systemctl start fpsk-bot
```

### Резервное копирование БД

```bash
# Создание бэкапа
pg_dump -U postgres fpsk_dataBase > backup_$(date +%Y%m%d).sql

# Восстановление
psql -U postgres fpsk_dataBase < backup_20240403.sql
```

### Автоматический бэкап (cron)

```bash
crontab -e

# Бэкап каждый день в 3:00
0 3 * * * pg_dump -U postgres fpsk_dataBase > /backups/fpsk_$(date +\%Y\%m\%d).sql
```

---

## Решение проблем

### Бот не отвечает
1. Проверьте статус: `systemctl status fpsk-bot`
2. Проверьте логи: `journalctl -u fpsk-bot -n 50`
3. Проверьте токен бота
4. Проверьте интернет

### Ошибка подключения к БД
1. Проверьте PostgreSQL: `systemctl status postgresql`
2. Проверьте credentials
3. Проверьте что БД существует: `psql -U postgres -l`

---

## Контакты

**Разработчик:**
- Email: dev@fpsk.kg
- Telegram: @fpsk_dev

**ФПСК:**
- Email: info@fpsk.kg
- Телефон: +996 312 625 753
- Сайт: fpsk.kg

---

**Успешного запуска!** 🚀
