@echo off
echo =======================================
echo Building Smart Bill Splitter...
echo =======================================
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src\main\java\com\smartbillsplitter\model\*.java src\main\java\com\smartbillsplitter\service\*.java src\main\java\com\smartbillsplitter\*.java
if %ERRORLEVEL% EQU 0 (
    echo Compilation successful! Classes compiled to 'bin' directory.
) else (
    echo Compilation failed.
)
pause