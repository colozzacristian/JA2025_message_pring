#!/bin/sh
if [ -f .env ]; then
  set -a
  . .env
  set +a
fi
exec java -jar app.jar --gmail.port=${GMAIL_PORT} --gmail.account=${GMAIL_ACCOUNT} --gmail.app.password=${GMAIL_APP_PASSWORD} --whitelist.ips=${whitelist_ips}