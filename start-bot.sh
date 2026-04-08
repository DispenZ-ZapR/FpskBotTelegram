#!/bin/bash

echo "========================================"
echo "  FPSK Guide Bot - Запуск"
echo "========================================"
echo ""

# Проверка Java
if ! command -v java &> /dev/null; then
    echo "ОШИБКА: Java не установлена!"
    echo "Установите Java 17:"
    echo "  Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "  Mac: brew install openjdk@17"
    exit 1
fi

echo "[OK] Java установлена"
echo ""

# Проверка переменных окружения
if [ -z "$BOT_TOKEN" ]; then
    echo "ОШИБКА: Переменная BOT_TOKEN не установлена!"
    echo ""
    echo "Установите переменные окружения:"
    echo "  export BOT_TOKEN=\"ваш_токен\""
    echo "  export BOT_USERNAME=\"@ваш_бот\""
    echo "  export OPERATOR_CHAT_ID=\"ваш_chat_id\""
    echo "  export DB_USERNAME=\"postgres\""
    echo "  export DB_PASSWORD=\"postgres\""
    echo ""
    echo "Или добавьте их в ~/.bashrc"
    exit 1
fi

echo "[OK] Переменные окружения настроены"
echo ""
echo "Запуск бота..."
echo "Для остановки нажмите Ctrl+C"
echo ""

java -jar target/FPSKGuideBot-0.0.1-SNAPSHOT.jar
