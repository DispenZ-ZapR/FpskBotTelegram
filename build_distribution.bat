@echo off
echo Building FPSK Guide Bot Distribution
echo ====================================

REM Clean and build project
echo Building project...
call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b 1
)

REM Create distribution folder
if not exist "distribution" mkdir distribution

REM Copy necessary files
echo Copying files to distribution folder...
copy "target\FPSKGuideBot-0.0.1-SNAPSHOT.jar" "distribution\"
copy "run_bot.bat" "distribution\"
copy "README_FOR_CUSTOMER.md" "distribution\"
copy "pom.xml" "distribution\pom.xml.backup"

REM Create zip archive
echo Creating distribution archive...
cd distribution
powershell -Command "Compress-Archive -Path '*' -DestinationPath '../FPSKGuideBot_Distribution.zip' -Force"
cd ..

echo.
echo Distribution created successfully!
echo File: FPSKGuideBot_Distribution.zip
echo.
echo Contents:
echo - FPSKGuideBot-0.0.1-SNAPSHOT.jar (application)
echo - run_bot.bat (launcher with token management)
echo - README_FOR_CUSTOMER.md (user manual)
echo - pom.xml.backup (for reference)

pause
