
cd ../
CDIR=$PWD
cd scripts
JDIR=${CDIR}/src/main/java
JPRG=src.main.java
rm $JDIR/*.class
javac -cp "$CDIR:$JDIR:." $JDIR/hw4.java
