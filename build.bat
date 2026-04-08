@echo off
echo ========================================
echo   FPSK Guide Bot - Сборка проекта
echo ========================================
echo.

REM Проверка Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ОШИБКА: Maven не установлен!
    echo.
    echo Установите Maven:
    echo 1. Скачайте с https://maven.apache.org/download.cgi
    echo 2. Или используйте готовый JAR файл из релиза
    pause
    exit /b 1
)

echo [OK] Maven установлен
echo.
echo Сборка проекта (это может занять несколько минут)...
echo.

mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo ОШИБКА: Сборка не удалась!
    echo Проверьте логи выше
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Сборка завершена успешно!
echo ========================================
echo.
echo JAR файл создан: target\FPSKGuideBot-0.0.1-SNAPSHOT.jar
echo.
echo Теперь запустите: start-bot.bat
echo.
pause
