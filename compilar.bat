@echo off
echo ============================
echo 🎮 COMPILANDO JUEGO...
echo ============================

REM 1. Crear carpeta bin si no existe
if not exist bin mkdir bin

REM 2. Compilar los .java
javac -d bin src\*.java

REM 3. Copiar recursos a bin (para que se empaqueten en el .jar)
xcopy /E /I /Y resources\* bin

REM 4. Crear el .jar con todo dentro
jar --create --file=SpaceInvaders.jar --main-class=Game -C bin .

echo ----------------------------
echo ✅ ¡JAR generado con recursos incluidos!
pause
