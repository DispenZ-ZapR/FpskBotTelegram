@echo off
echo FPSK Guide Bot Launcher
echo =======================

REM Check if JAR file exists
if not exist "target\FPSKGuideBot-0.0.1-SNAPSHOT.jar" (
    echo ERROR: JAR file not found in target folder!
    echo Please run: mvn clean package
    pause
    exit /b 1
)

REM Check if token file exists
if not exist "bot_token.txt" (
    echo Creating token configuration file...
    echo Please enter your Telegram bot token:
    set /p TOKEN="Token: "
    echo %TOKEN% > bot_token.txt
    echo Token saved to bot_token.txt
    echo.
)

REM Read token from file
set /p TOKEN=<bot_token.txt

echo Starting bot with token: %TOKEN:~0,10%...
echo.

java -jar target\FPSKGuideBot-0.0.1-SNAPSHOT.jar ID_TOKEN=%TOKEN%

pause
