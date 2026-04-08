# Быстрая настройка для заказчика

## Шаг 1: Получите данные для бота

### 1.1 Получите токен бота
1. Откройте Telegram
2. Найдите **@BotFather**
3. Отправьте команду: `/newbot`
4. Введите имя бота (например: FPSK Guide Bot)
5. Введите username бота (например: FPSKGuide_bot)
6. **Скопируйте токен** - он выглядит так: `1234567890:ABCdefGHIjklMNOpqrsTUVwxyz`

### 1.2 Получите свой Chat ID (для получения обращений)
1. Найдите в Telegram: **@userinfobot**
2. Отправьте ему любое сообщение
3. **Скопируйте ваш Id** - это число, например: `123456789`

---

## Шаг 2: Установите Java (один раз)

**Windows:**
1. Скачайте: https://adoptium.net/temurin/releases/?version=17
2. Выберите: Windows x64, .msi installer
3. Установите (просто нажимайте "Далее")
4. Перезагрузите компьютер

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

---

## Шаг 3: Установите PostgreSQL (один раз)

**Windows:**
1. Скачайте: https://www.postgresql.org/download/windows/
2. Установите (запомните пароль!)
3. Откройте "SQL Shell (psql)" из меню Пуск
4. Нажимайте Enter до "Password for user postgres:"
5. Введите ваш пароль
6. Выполните: `CREATE DATABASE fpsk_dataBase;`
7. Выход: `\q`

**Важно:** Таблицы создадутся автоматически при первом запуске бота!

**Linux:**
```bash
sudo apt install postgresql
sudo systemctl start postgresql
sudo -u postgres psql
CREATE DATABASE fpsk_dataBase;
\q
```

---

## Шаг 4: Настройте переменные окружения

**Windows:**

Откройте командную строку **от имени администратора** и выполните:

```cmd
setx BOT_TOKEN "вставьте_ваш_токен_сюда"
setx BOT_USERNAME "@ваш_username_бота"
setx OPERATOR_CHAT_ID "ваш_chat_id"
setx DB_USERNAME "postgres"
setx DB_PASSWORD "ваш_пароль_postgres"
```

**ВАЖНО:** После этого **закройте и откройте новую** командную строку!

**Linux/Mac:**

Добавьте в конец файла `~/.bashrc`:

```bash
export BOT_TOKEN="вставьте_ваш_токен_сюда"
export BOT_USERNAME="@ваш_username_бота"
export OPERATOR_CHAT_ID="ваш_chat_id"
export DB_USERNAME="postgres"
export DB_PASSWORD="ваш_пароль_postgres"
```

Примените:
```bash
source ~/.bashrc
```

---

## Шаг 5: Соберите и запустите бота

**Windows:**
1. Откройте папку с проектом
2. Дважды кликните на `build.bat` (первый раз для сборки)
3. Дождитесь сообщения "Сборка завершена успешно!"
4. Дважды кликните на `start-bot.bat`
5. Готово! Бот запущен

**Linux/Mac:**
```bash
# Сначала соберите проект
chmod +x build.sh
./build.sh

# Затем запустите бота
chmod +x start-bot.sh
./start-bot.sh
```

**Если у вас уже есть готовый JAR файл:**
- Просто запустите `start-bot.bat` (Windows) или `./start-bot.sh` (Linux/Mac)

---

## Проверка работы

1. Откройте Telegram
2. Найдите вашего бота по username
3. Отправьте `/start`
4. Если бот ответил - всё работает! ✅

---

## Как остановить бота

- Нажмите `Ctrl+C` в окне где запущен бот
- Или просто закройте окно

---

## Решение проблем

### "Java не установлена"
→ Установите Java 17 (см. Шаг 2)

### "BOT_TOKEN не установлена"
→ Выполните команды из Шага 4 и **перезапустите командную строку**

### "Error 404: Not Found"
→ Неверный токен бота. Получите новый у @BotFather: `/token`

### "Unable to open JDBC Connection"
→ PostgreSQL не запущен или неверный пароль
→ Windows: Запустите службу "postgresql" в Службах
→ Linux: `sudo systemctl start postgresql`

### Бот не отвечает
→ Проверьте что окно с ботом открыто и работает
→ Проверьте интернет-соединение

---

## Контакты поддержки

📧 Email: info@fpsk.kg  
📞 Телефон: +996 312 625 753  
🌐 Сайт: fpsk.kg
