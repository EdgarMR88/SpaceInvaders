@echo off
echo Compilando archivos Java...

REM Crear carpeta bin si no existe
if not exist bin mkdir bin

REM Compilar los archivos .java
javac -d bin *.java

echo Empaquetando en SpaceInvaders.jar...
"C:\Program Files\Java\jdk-23\bin\jar" cfe SpaceInvaders.jar MANIFEST.MF -C bin .

echo ---
echo ¡Compilación completada!
echo Ejecuta el juego con: java -jar SpaceInvaders.jar
pause
