#!/bin/bash

echo "========================================"
echo "  FPSK Guide Bot - Сборка проекта"
echo "========================================"
echo ""

# Проверка Maven
if ! command -v mvn &> /dev/null; then
    echo "ОШИБКА: Maven не установлен!"
    echo ""
    echo "Установите Maven:"
    echo "  Ubuntu/Debian: sudo apt install maven"
    echo "  Mac: brew install maven"
    exit 1
fi

echo "[OK] Maven установлен"
echo ""
echo "Сборка проекта (это может занять несколько минут)..."
echo ""

mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "ОШИБКА: Сборка не удалась!"
    echo "Проверьте логи выше"
    exit 1
fi

echo ""
echo "========================================"
echo "  Сборка завершена успешно!"
echo "========================================"
echo ""
echo "JAR файл создан: target/FPSKGuideBot-0.0.1-SNAPSHOT.jar"
echo ""
echo "Теперь запустите: ./start-bot.sh"
echo ""
