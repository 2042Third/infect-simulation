@echo off 
cd ../
set CDIR=%cd%
cd scripts
set JDIR=%CDIR%\src\main\java
set JPRG=src.main.java.
set JBACK=%cd%\scripts
del %JDIR%\*.class
javac -cp "%CDIR%;%JDIR%;." %JDIR%\HW4.java 
