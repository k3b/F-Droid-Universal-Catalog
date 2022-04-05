Goal: Create a crossplatform library to transfer a [fdroid] [Repo-Catalog] (= FDroid Json Catalog
data) to Database

* Datasource (platform neutral): java jar data file [index-v1.jar] that contains zip-compressed data
  in fdroid-v1-json format
* Destination Database (platform specific):
    * Platfrom neutral Java Repository Interface with pojo-entities to be persisted in database
    * Anrdoid-Room Database Repository interface dao-implementation
    * J2SE-JPA Database Repository Interface Implementation
* V1UpdateService(platform neutral)

---

Glossar:

* [Address]
    * Example: "https://f-droid.org/repo"
    * Internet URL of a [Repo] or [Mirror] where [Repo-Catalog] or [APK-File] can be downloaded
      from.
* [APK-File]
    * An Android application file that can be installed on an Android device
    * An [APK-File] corresponds to a [Version] of an [App] in a [Repo].
* [App]
    * Information about an Android App that is available in a [Repo] or [Mirror]
    * Each app has a [Repo] independent unique [Packagename]
    * An [App] can have one or more [Version]s
* [Category]
    * An [App] can belong to one or more [Category]s
    * Example: Games, Internet, Connectivity, Graphics, Multimedia
* [fdroid]
    * A Server Software that manages one [Repo-Catalog] with android [App]s and [APK-File]s
    * https://f-droid.org is the most popular [fdroid] server instance or [Repo].
* [fdroid-v1] short for [Fdroid] [JSON] catalog format "v1"
* [index-v1.jar] (alias for [zip]ed [Repo-Catalog] in fileformat [fdroid-v1] )
    * Zipped-file containing the [Repo-Catalog]. The zip contains [index-v1.json]
        * See https://en.wikipedia.org/wiki/ZIP_(file_format)
    * Can be downloaded from a [Repo] or [Mirror]
    * Example: [Repo].[Address] = "https://f-droid.org/repo" can be downloded
      from "https://f-droid.org/repo/index-v1.jar"
* [index-v1.json]  (alias for [Repo-Catalog] in fileformat [fdroid-v1] )
    * contains the [fdroid-v1] [Repo]-Catalog in [JSON] format.
* JSON (JavaScript Object Notation)
    * Is a technical open standard file format and data interchange format that uses human-readable.
    * See https://en.wikipedia.org/wiki/JSON
* [Localized]
    * Language specific Translation of infos about an [App]
    * Example: en(=englisch) [App] summary: "Large digital clock in 24 hour format"
    * Example: de(=german or "deutsch") [App] summary: "Große Digitaluhr im 24-Stunden-Format"
* [Mirror] of a [REPO]
    * A duplicate of a [Ropo] that allows to download under a different [Adress] or url used for
      Load balancing.
    * see https://en.wikipedia.org/wiki/Load_balancing_(computing) for details
* [Package] or [Packagename]
    * A unique Name for an Android [App]
* [Repo]
    * An Android [App] Repository that contains a catalogue of Android apps
    * Each Repo has a content-catalogfile "index-v1.jar", a ziped-file that contains "index-v1.json"
    * A [Repo] allows to download [APK-File] in one or more  [Version]s
* [Repo-Catalog]
    * contains a list off all [App]s available in a [Repo]
    * [fdroid] uses [index-v1.jar] and [index-v1.json] as catalog data.

---

[fdroid-v1] - [JSON] format of one [index-v1.jar] / [index-v1.json] [Repo-Catalog] file

* Catalog
    * [Repo] (Example: address="https://f-droid.org/repo")
        * [Mirror]s (Example "https://ftp.fau.de/fdroid/repo")
    * [App]s (Example [packageName]="com.chancehorizon.just24hoursplus")
        * [Category]s (Example "Games")
        * [Localized] (Example: "en-US" -> summary="Large digital clock in 24 hour format")
    * [Package]s (Example packageName="com.chancehorizon.just24hoursplus")
        * [Version]s (Example versionName="1.4.1")

Database format

* [Repo]
    * [App]s
        * [Category]s
        * [Localized]
        * [Version]s

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
* room-dao findAll (T[] or List<T>) and jpa findAll (Iterable<T>) are not compatible: different
  return types
* room-dao uses insert(T) or update(T); JPA uses save(T)
* annotation namespace: androidx.room.* vs javax.persistence.*

----

links

* https://spring.io/guides/gs/accessing-data-jpa/
* https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.collections-and-iterables