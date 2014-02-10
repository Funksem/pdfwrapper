#!/bin/bash
# ---------------------------------------------------------------------------
# --- pdfwrapper / Start-Skript fuer Unix/Linux
# ---
# --- Copyright (c) 2014 by funksem
# ---
# --- $Id$
# ---------------------------------------------------------------------------

DEFAULT_URL=${rest.url}

CALLED_PATH=${0%/*}
REAL_PATH=`pwd`/$CALLED_PATH


echo "Using JAVA_HOME:       $JAVA_HOME"

        
if [ -z "$JAVA_HOME" ]; then
    echo "The JAVA_HOME environment variable is not defined correctly"
    echo "This environment variable is needed to run this program"
    exit 1
else
     if [ -x "$JAVA_HOME"/bin/java ]; then
        echo
        echo "Starting KV-CONNECT with JAVA_HOME: $JAVA_HOME"
        echo
     else
        echo "The JAVA_HOME/bin/java executable is not defined correctly"
        echo "This executable 'java' is needed to run this program"
        exit 1
     fi
fi

$JAVA_HOME/bin/java -cp $REAL_PATH/lib/"*" de.funksem.pdfwrapper.PdfWrapper $*