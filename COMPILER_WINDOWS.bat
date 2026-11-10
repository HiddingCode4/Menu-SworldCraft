@echo off
cd /d "%~dp0"
call gradlew.bat build
if errorlevel 1 (
 echo Echec de compilation. Verifiez que le JDK 25 est installe.
 pause
 exit /b 1
)
echo Le mod est disponible dans build\libs
pause
