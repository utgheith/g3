#!/bin/bash

URL=https://github.com/sbt/sbt/releases/download/v1.5.0/sbt-1.5.0.tgz
SBT=.sbt/sbt/bin/sbt

function getit() {
    echo "downloading ..."
    #curl -L $URL --output sbt.tgz
    #mkdir 
}

if [ \! -x .sbt/sbt/bin/sbt ]; then
    echo "downloading ..."
    rm -rf .sbt sbt.tgz
    curl -L $URL --output sbt.tgz
    mkdir -p .sbt
    (cd .sbt ; tar xvfz ../sbt.tgz)
    rm sbt.tgz
fi

exec $SBT "$@"
