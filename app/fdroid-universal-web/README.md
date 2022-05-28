https://github.com/k3b/F-Droid-Universal-Catalog/fdroid-universal-web/README.md

# Spring-Boot-jpa Based web application for F-Droid-Universal-Catalog

Web frontend for F-Droid Catalog

The Web frontend sits on top of the crossplatform java library

Prerequsits:

running jdbc database "jdbc:hsqldb:hsql://localhost/fdroid"

how to build

    ./gradlew bootjar

The Library currently has these features which should be integrated into the the different gui-s (
Web and android)

* Download and Import of F-Droid-V1-Index
* Ready to use Mirrors
  * i.e. https://f-droid.org/repo/ is also available at the mirror https://ftp.fau.de/fdroid/repo
* Support for several simultanius Catalog Languages
  * i.e. show german and english app descriptions or show in all available languages.
* Support for several simultanius Catalog sources
  * i.e. F-Droid, Izzy, guardianproject, ....
* Integrate repositories that use same signature
  * i.e https://f-droid.org/repo/ and https://f-droid.org/archive/ are used together so that
    multilingual description come from .../repo/.. and old program versions come from
    .../archive/...
* Support for multi-language fulltext search

* (Planed) Features
  * Multilingual F-droid-catalog
    * (planned) Web gui
    * App Description (done)
    * Multilingual Full Text App Search (done)
  * Multi Catalog (done)

Goal: Proof of concept non-android web app for the "k3b-Architecture", that should contain as much
platform-independent code as possible.
