#!/bin/bash

cd ../
CDIR=$PWD
cd scripts
JDIR=$CDIR/src/main/java
TESTS=$CDIR/src/test/java
TESTJAR=$CDIR/src/test/resources/junit-platform-console-standalone-1.7.0-all.jar
JPRG=src.main.java
JBACK=$PWD/scripts
rm $JDIR/*.class
javac -cp "$CDIR:$JDIR:$TESTS:$TESTJAR" $TESTS/PlagueTest.java
java -jar $TESTJAR -cp "$JDIR:$TESTS"  --select-class PlagueTest --reports-dir='../report'