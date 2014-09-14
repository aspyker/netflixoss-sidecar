#!/bin/sh
URLPORT=$1
URLPATH=$2
URL=http://localhost:${URLPORT}${URLPATH}
HTTPCODE=`curl --write-out "%{http_code}\n" --silent --output /dev/null ${URL}`
if [ $HTTPCODE == "200" ]; then
  exit 0
fi
exit 2
