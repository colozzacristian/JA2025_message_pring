@echo off
REM Load environment variables from .env file
for /f "delims== tokens=1,2" %%A in (.env) do set %%A=%%B

REM Run Maven with settings.xml using the environment variables
mvn -s .m2/settings.xml clean install