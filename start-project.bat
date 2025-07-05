@echo off
chcp 65001 > nul
echo ===============================================
echo   赛博戒烟模拟器 - 项目启动脚本
echo   Cyber Quit Smoking Simulator - Start Script
echo ===============================================
echo.

echo 正在检查环境...
echo.

:: 检查Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到Java，请先安装Java 17+
    pause
    exit /b 1
)

:: 检查Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到Maven，请先安装Maven 3.6+
    pause
    exit /b 1
)

:: 检查Node.js
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到Node.js，请先安装Node.js 16+
    pause
    exit /b 1
)

echo [✓] 环境检查完成！
echo.

echo 正在启动后端服务...
echo.
start "SpringBoot后端" cmd /k "mvn spring-boot:run"

echo 等待后端服务启动...
timeout /t 10 /nobreak > nul

echo 正在启动前端服务...
echo.
cd frontend
start "Vue3前端" cmd /k "npm run dev"

echo.
echo ===============================================
echo   项目启动完成！
echo   后端服务: http://localhost:8080
echo   前端服务: http://localhost:3000
echo   请等待服务完全启动后访问前端地址
echo ===============================================
echo.
echo 按任意键退出...
pause > nul 