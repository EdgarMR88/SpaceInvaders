@echo off
setlocal

echo ===============================
echo 🚀 Creando ejecutable con jpackage...
echo ===============================

REM Ruta del proyecto (ajusta si lo necesitas)
set INPUT=.
set JAR=SpaceInvaders.jar
set MAINCLASS=Game
set ICON=SpaceInvaders.ico
set DEST=dist

REM Crear ejecutable con jpackage
jpackage ^
  --name "SpaceInvaders" ^
  --input "%INPUT%" ^
  --main-jar "%JAR%" ^
  --main-class "%MAINCLASS%" ^
  --icon "%ICON%" ^
  --type exe ^
  --dest "%DEST%" ^
  --win-console

echo ===============================
echo ✅ ¡Ejecutable creado en: %DEST%!
pause
endlocal
