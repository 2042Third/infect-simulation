@echo off
cd ../
set CDIR=%cd%
cd scripts
set JDIR=%CDIR%\src\main\java\
set JPRG=src.main.java.
set JBACK=%cd%\scripts
java -cp "%CDIR%;%JDIR%;." hw4