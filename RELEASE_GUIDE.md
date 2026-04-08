# Инструкция для разработчика: Подготовка релиза для заказчика

## Шаг 1: Соберите проект

```bash
mvn clean package -DskipTests
```

JAR файл будет создан: `target/FPSKGuideBot-0.0.1-SNAPSHOT.jar`

## Шаг 2: Создайте папку релиза

```bash
mkdir FPSK_Bot_Release
cd FPSK_Bot_Release
```

## Шаг 3: Скопируйте необходимые файлы

### Для Windows заказчика:

```
FPSK_Bot_Release/
├── FPSKGuideBot.jar                    ← Переименованный JAR
├── start-bot.bat                        ← Скрипт запуска
├── QUICK_START.md                       ← ГЛАВНАЯ ИНСТРУКЦИЯ
├── CUSTOMER_GUIDE.md                    ← Руководство пользователя
└── README.txt                           ← Краткая справка
```

### Команды копирования (Windows):

```cmd
copy target\FPSKGuideBot-0.0.1-SNAPSHOT.jar FPSK_Bot_Release\FPSKGuideBot.jar
copy start-bot.bat FPSK_Bot_Release\
copy QUICK_START.md FPSK_Bot_Release\
copy CUSTOMER_GUIDE.md FPSK_Bot_Release\
```

### Команды копирования (Linux/Mac):

```bash
cp target/FPSKGuideBot-0.0.1-SNAPSHOT.jar FPSK_Bot_Release/FPSKGuideBot.jar
cp start-bot.sh FPSK_Bot_Release/
cp QUICK_START.md FPSK_Bot_Release/
cp CUSTOMER_GUIDE.md FPSK_Bot_Release/
```

## Шаг 4: Обновите start-bot.bat для релиза

Создайте упрощенный `start-bot.bat` в папке релиза:

```batch
@echo off
echo ========================================
echo   FPSK Guide Bot
echo ========================================
echo.

REM Проверка Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ОШИБКА: Java не установлена!
    echo Скачайте Java 17: https://adoptium.net/
    pause
    exit /b 1
)

REM Проверка переменных
if "%BOT_TOKEN%"=="" (
    echo ОШИБКА: Переменные окружения не настроены!
    echo.
    echo Откройте QUICK_START.md для инструкций
    pause
    exit /b 1
)

echo Запуск бота...
java -jar FPSKGuideBot.jar
pause
```

## Шаг 5: Создайте README.txt

```text
===========================================
  FPSK Guide Bot - Telegram бот
===========================================

НАЧНИТЕ С ЭТОГО ФАЙЛА: QUICK_START.md

Быстрая установка:
1. Установите Java 17
2. Установите PostgreSQL
3. Получите токен у @BotFather
4. Настройте переменные окружения
5. Запустите start-bot.bat

Подробная инструкция: QUICK_START.md
Руководство пользователя: CUSTOMER_GUIDE.md

Контакты:
- Email: info@fpsk.kg
- Телефон: +996 312 625 753
- Сайт: fpsk.kg
```

## Шаг 6: Заархивируйте

```bash
# Windows (через проводник)
Правый клик на FPSK_Bot_Release → Отправить → Сжатая ZIP-папка

# Linux/Mac
zip -r FPSK_Bot_Release.zip FPSK_Bot_Release/
```

## Шаг 7: Передайте заказчику

Отправьте:
- ✅ `FPSK_Bot_Release.zip`
- ✅ Ссылку на Git репозиторий (опционально)

## Что заказчик получит:

```
FPSK_Bot_Release/
├── FPSKGuideBot.jar          ← Готовый бот
├── start-bot.bat             ← Двойной клик для запуска
├── QUICK_START.md            ← Инструкция по установке
├── CUSTOMER_GUIDE.md         ← Как пользоваться ботом
└── README.txt                ← Краткая справка
```

## Альтернатива: GitHub Release

1. Создайте тег версии:
```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

2. На GitHub: Releases → Create a new release
3. Загрузите `FPSKGuideBot.jar`
4. Добавьте описание с инструкцией

## Чеклист передачи:

- [ ] JAR файл собран и протестирован
- [ ] Скрипты запуска работают
- [ ] Документация актуальна
- [ ] Токен бота создан и передан
- [ ] OPERATOR_CHAT_ID получен
- [ ] Проведено обучение заказчика
- [ ] Контакты для поддержки предоставлены

## Для обновлений:

Когда нужно обновить бота:
1. Соберите новый JAR
2. Отправьте заказчику только `FPSKGuideBot.jar`
3. Заказчик заменяет старый файл новым
4. Перезапускает бота

Просто и понятно! 🚀
