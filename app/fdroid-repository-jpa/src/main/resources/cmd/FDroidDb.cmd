@echo off
rem create database-directory if not exists
mkdir %USERPROFILE%\fdroid 2> nul
mkdir %USERPROFILE%\fdroid\db 2> nul

rem cd to database-directory
rem i.e. C:\Users\{username}\fdroid\db
echo cd %USERPROFILE%\fdroid\db
cd %USERPROFILE%\fdroid\db
rem start database server
set db=java -classpath "C:/Program Files/Java/apps/hsqldb/lib/hsqldb.jar" org.hsqldb.server.Server --database.0 file:fdroid --dbname.0 fdroid
echo %db%
%db%
