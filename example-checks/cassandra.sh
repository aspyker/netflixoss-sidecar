#!/bin/sh
set -x

PRIVATE_IP=`ifconfig eth0 | sed -n 's/.*inet addr:\([0-9.]\+\)\s.*/\1/p'`

/opt/apache-cassandra-2.0.10/bin/cqlsh ${PRIVATE_IP} < select_count.cql > /dev/null 2>&1
rc=$?
if [ $rc != 0 ] ; then
  exit 2
fi
exit 0
