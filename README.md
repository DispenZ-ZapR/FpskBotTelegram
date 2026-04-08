# FPSK Guide Bot

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue.svg)](https://www.postgresql.org/)

> Официальный многоязычный Telegram-бот Федерации профессиональных союзов Кыргызстана

## О проекте

FPSK Guide Bot - интеллектуальный Telegram-бот для круглосуточного доступа к информации о Федерации профессиональных союзов Кыргызстана. Поддерживает три языка (русский, кыргызский, английский) и систему обращений с операторами.

### Основные возможности

- 🌐 Многоязычность (RU/KG/EN)
- 📞 Система обращений с rate limiting
- 📖 Информационные разделы
- 🔒 Защита от спама
- 💾 Сохранение состояний в PostgreSQL

## Быстрый старт

### Требования

- Java 17+
- Maven 3.8+
- PostgreSQL 12+

### Установка

1. **Клонируйте репозиторий**
```bash
git clone https://github.com/your-org/FPSKGuideBot.git
cd FPSKGuideBot
```

2. **Создайте базу данных**
```sql
CREATE DATABASE fpsk_dataBase;
CREATE USER fpsk_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE fpsk_dataBase TO fpsk_user;
```

3. **Настройте переменные окружения**
```bash
export BOT_TOKEN="your_bot_token_from_botfather"
export BOT_USERNAME="@FPSKGuide_bot"
export OPERATOR_CHAT_ID="123456789"
export DB_URL="jdbc:postgresql://localhost:5432/fpsk_dataBase"
export DB_USERNAME="fpsk_user"
export DB_PASSWORD="your_password"
```

4. **Запустите приложение**
```bash
mvn clean install
mvn spring-boot:run
```

## Документация

- **[Быстрая настройка](QUICK_START.md)** - как настроить свой токен и ID
- **[Руководство для заказчика](CUSTOMER_GUIDE.md)** - как использовать бота
- **[Руководство разработчика](DEVELOPER_GUIDE.md)** - техническая документация
- **[Инструкция по развертыванию](DEPLOYMENT.md)** - как развернуть бота на сервере

## Технологии

- Java 17, Spring Boot 3.4.1, Spring Data JPA
- PostgreSQL 12+
- TelegramBots 6.9.7.1
- Flyway (миграции БД)
- Maven 3.8+

## Контакты

**Федерация профессиональных союзов Кыргызстана**

- 🌐 [fpsk.kg](https://fpsk.kg)
- 📧 info@fpsk.kg
- 📱 [@FPSKGuide_bot](https://t.me/FPSKGuide_bot)
- 📞 +996 312 625 753
- 📍 720001, г. Бишкек, пр-т Чуй 207

---

**Разработано с ❤️ для защиты трудовых прав**
