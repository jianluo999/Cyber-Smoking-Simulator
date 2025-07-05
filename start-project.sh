#!/bin/bash

echo "==============================================="
echo "  赛博戒烟模拟器 - 项目启动脚本"
echo "  Cyber Quit Smoking Simulator - Start Script"
echo "==============================================="
echo

echo "正在检查环境..."
echo

# 检查Java
if ! command -v java &> /dev/null; then
    echo "[错误] 未找到Java，请先安装Java 17+"
    exit 1
fi

# 检查Maven
if ! command -v mvn &> /dev/null; then
    echo "[错误] 未找到Maven，请先安装Maven 3.6+"
    exit 1
fi

# 检查Node.js
if ! command -v node &> /dev/null; then
    echo "[错误] 未找到Node.js，请先安装Node.js 16+"
    exit 1
fi

echo "[✓] 环境检查完成！"
echo

echo "正在启动后端服务..."
echo
osascript -e 'tell app "Terminal" to do script "cd '$(pwd)' && mvn spring-boot:run"' &

echo "等待后端服务启动..."
sleep 10

echo "正在启动前端服务..."
echo
cd frontend
osascript -e 'tell app "Terminal" to do script "cd '$(pwd)' && npm run dev"' &

echo
echo "==============================================="
echo "  项目启动完成！"
echo "  后端服务: http://localhost:8080"
echo "  前端服务: http://localhost:3000"
echo "  请等待服务完全启动后访问前端地址"
echo "==============================================="
echo 