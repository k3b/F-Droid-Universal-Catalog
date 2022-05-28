https://github.com/k3b/F-Droid-Universal-Catalog/fdroid-universal-jpa/README.md

# Spring-Boot-jpa database for F-Droid-Universal-Catalog

This is the non-android database implementation consumed by spring-boot.

Prerequsits:

download databaseserver hsqldb.jar to "C:/Program Files/Java/apps/"

create working directory

> cd c:\Users\xxxxxx
> mkdir fdroid
> mkdir fdroid\download
> mkdir fdroid\db
> cd fdroid\db
>
> rem start local database "c:\Users\xxxxxx\fdroid\db"
> rem available via jdbc as "jdbc:hsqldb:hsql://localhost/fdroid"
> java -classpath "C:/Program Files/Java/apps/hsqldb/lib/hsqldb.jar" org.hsqldb.server.Server --database.0 file:fdroid --dbname.0 fdroid

