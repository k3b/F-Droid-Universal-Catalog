https://github.com/k3b/F-Droid-Universal-Catalog/README.md

# F-Droid-Universal-Catalog

Goal: Create a crossplatform library to transfer a [fdroid](Glossar#fdroid)(
Glossar#fdroid) [Repo-Catalog](Glossar#Repo-Catalog) (= FDroid Json Catalog data) to Database

* Datasource (platform neutral): java jar data file [index-v1.jar](Glossar#index-v1.jar) that
  contains zip-compressed data in fdroid-v1-json format
* Destination Database (platform specific):
    * Platfrom neutral Java Repository Interface with pojo-entities to be persisted in database
    * Anrdoid-Room Database Repository interface dao-implementation
    * J2SE-JPA Database Repository Interface Implementation
* V1UpdateService(platform neutral)

---

[fdroid-v1](Glossar#fdroid-v1) - [JSON](Glossar#JSON) format of
one [index-v1.jar](Glossar#index-v1.jar)
/ [index-v1.json](Glossar#index-v1.json) [Repo-Catalog](Glossar#Repo-Catalog) file

* Catalog
    * [Repo](Glossar#Repo) (Example: address="https://f-droid.org/repo")
        * [Mirror](Glossar#Mirror)s (Example "https://ftp.fau.de/fdroid/repo")
    * [App](Glossar#App)s (Example [packageName](Glossar#packageName)="
      com.chancehorizon.just24hoursplus")
        * [Category](Glossar#Category)s (Example "Games")
        * [Localized](Glossar#Localized) (Example: "en-US" -> summary="Large digital clock in 24
          hour format")
    * [Package](Glossar#Package)s (Example packageName="com.chancehorizon.just24hoursplus")
        * [Version](Glossar#Version)s (Example versionName="1.4.1")

todo links

Database format

* [Repo](Glossar#)
    * [App](Glossar#)s
        * [Category](Glossar#)s
        * [Localized](Glossar#)
        * [Version](Glossar#)s

-----

Architecture (high level, Modul)

* app (android)
    * fdroid-domain
    * fdroid-v1
    * fdroid-repository-android
* demo (spring-boot-cli)
    * fdroid-domain
    * fdroid-v1
    * fdroid-repository-jpa
    * room-annotations

-----

Architecture (detailed)

* Userinterface out of scope: This work tries to esablish a platform independant service
  architecture in the fdroid ecosystem.
* Services (non-Android Code that uses other Services und Repository-Interfaces)
    * V1UpdateService Reads reads Android Catalog json data [fdroid-v1](Glossar#) and updates the
      Database via XxxxxxRepositories

> > * FDroidCatalogJsonStreamParserXxxx : reads Android Catalog json data [fdroid-v1](Glossar#) as a Stream of Events: on[Repo](Glossar#), on[App](Glossar#), on[Version](Glossar#)
                * XxxxUpdateService : translates [fdroid-v1](Glossar#) to database updates via XxxxRepository: [Repo](Glossar#)UpdateService, [App](Glossar#)UpdateService, [App](Glossar#)UpdateService, ...
      !!  * CanonicalLocale: Merges [Localized](Glossar#) to non-locale-languages: i.e. en, en-AU, en-GB, en-US, en-rUS, en-us and en_US will all be mapped to "en"
      ??  * LocaleImportFilter : Filter out languages that the user does not understand and that is not stored in the local database to save memory.       * Example: I can read de, en, nds, nl but not ja, cn, ar, ...... ??  * [Repo](Glossar#)SecurityService: Make shure that catalogdata [index-v1.jar](Glossar#) is not tampered with (checksum and signature is ok)
      ??* DeviceCompatibilitySearchFilter:
                * If i have a Android-5 device and the software requires Android-6 or later     * If i have installed an [App](Glossar#) from [Repo](Glossar#) f-droid.org i cannot install an update[Version](Glossar#) from [Repo](Glossar#) f-droid.org because the [App](Glossar#)-[Version](Glossar#)-signature does not match. ??* [App](Glossar#)-[Category](Glossar#)-SearchFilter/Sorter:
                * show [App](Glossar#)s that match one or more [Category](Glossar#)s: Example I want to see [App](Glossar#)s of [Category](Glossar#) "games"
      ??* FulltextSearchFilter over [Packagename](Glossar#) and [Localized](Glossar#)- name, summary, description, whatIsNew of all languages the user understands. ??* RelevanceSorter: Relevance in app-title is higher than app-summay than app-description.     * Example when i search for "FDroid" i want to see the FDroid-Client-app before apps that contain "Available for download in FDroid. ??* LastUpdateSorter/Filter: Which app was updated in the last 90 days? ??* LanguageFilter:
                * app that has a translated catalog summary/description in one or more languages.     * the app-userinterface has been translated in one or more languages. ??* App-Rating/App-Comments (FDroid.org independant)
      ??* Userdefined Software collection and Software-Categories (FDroid.org independant)
      ??* Android-Specific app-Installation/Update/Deinstallation-Service

* Repository-Interface (database abstraction) with implementatino in Android-Room (XxxxxDao) and
  Spring-Boot-JPA XxxxRepositoryJpa + XxxxRepositoryAdapter
    * [Repo](Glossar#)Repository with [Repo](Glossar#)Dao and Spring-Boot-JPA [Repo](Glossar#)
      RepositoryJpa + [Repo](Glossar#)
      RepositoryAdapter,
    * [App](Glossar#)Repository with ...
    * [Localized](Glossar#)Repository with ...
    * [Version](Glossar#)Repository with ...

> > =current work in progress
!!=todo ??=not implemented yet

-----

Software Moduls

* app (Android)
    * data_v1 (java based pojos with gson-json reader)
    * data_db (java Repository-interfaces, entity-pojos, import services)
        * repository-interfaces implemented with Android-Room

* demo (non android, based on spring-boot)
    * data_v1 (java based pojos with gson-json reader)
    * data_db (java Repository-interfaces, entity-pojos, import services)
        * interfaces implemented with non-Android-jpa
        * fake-room-annotation-jar

-----

Database Differences between Android-Room and j2se-jpa

* Room-Entities don't allow object references
    *
  see https://developer.android.com/training/data-storage/room/referencing-data#understand-no-object-references
    * only primitive types, primarykeys and foreinkeys are allowed in entities. No List<Xxx> or
      Object references.
    * Model navigation: instead of using entity members (List<Xxx> or Object references) you must
      use repository methods
* room-annotations are inside android-modules that cannot be used inside non-android-libs
    * workaround create a non-android-lib with code duplicates of the room-annotations
* room-dao findAll (T[](Glossar#) or List<T>) and jpa findAll (Iterable<T>) are not compatible:
  different return types
* room-dao uses insert(T) or update(T); JPA uses save(T)
* annotation namespace: androidx.room.* vs javax.persistence.*

----

links

* https://jeffreypalermo.com/2008/07/the-onion-architecture-part-1/
* https://spring.io/guides/gs/accessing-data-jpa/
* https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.collections-and-iterables

-----

fdroid apps

droidi-fy       https://github.com/NeoApplications/Neo-Store/
gdroid          https://gitlab.com/gdroid/gdroidclient/
f-droid         https://gitlab.com/fdroid/fdroidclient
fdroidclassic   https://git.bubu1.eu/Bubu/fdroidclassic

fdroid-html android generic test (lib zum ermitteln der templates)

test a link [APK-File](Glossar#)(#APK-File)
  
