cd ../
set CDIR=%cd%
cd scripts
set JDIR=%CDIR%/src/main/java
set TESTS=$CDIR/src/test/java
set TESTJAR=$CDIR/src/test/resources/junit-platform-console-standalone-1.7.0-all.jar
set JPRG=src.main.java
set JBACK=$PWD/scripts
@REM rm $JDIR/*.class
javac -cp "%CDIR%:%JDIR%:%TESTS%:%TESTJAR%" %TESTS%/PlagueTest.java
java -jar %TESTJAR% -cp "%JDIR%:%TESTS%"  --select-class PlagueTest --reports-dir='../report'