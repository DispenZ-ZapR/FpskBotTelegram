@echo off
echo ========================================
echo   FPSK Guide Bot - Запуск
echo ========================================
echo.

REM Проверка Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ОШИБКА: Java не установлена!
    echo Скачайте Java 17 с: https://adoptium.net/
    pause
    exit /b 1
)

echo [OK] Java установлена
echo.

REM Проверка переменных окружения
if "%BOT_TOKEN%"=="" (
    echo ОШИБКА: Переменная BOT_TOKEN не установлена!
    echo.
    echo Установите переменные окружения:
    echo   setx BOT_TOKEN "ваш_токен"
    echo   setx BOT_USERNAME "@ваш_бот"
    echo   setx OPERATOR_CHAT_ID "ваш_chat_id"
    echo   setx DB_USERNAME "postgres"
    echo   setx DB_PASSWORD "postgres"
    echo.
    echo После установки ПЕРЕЗАПУСТИТЕ командную строку!
    pause
    exit /b 1
)

echo [OK] Переменные окружения настроены
echo.
echo Запуск бота...
echo Для остановки нажмите Ctrl+C
echo.

java -jar target\FPSKGuideBot-0.0.1-SNAPSHOT.jar

pause
