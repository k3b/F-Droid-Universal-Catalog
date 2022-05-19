todo

* android: when version-import completed clear caches => recreate AppList-adapter
* android: icon download complete => logcat
* fix app icon: text overlap in android
  * ?? try to replace TextView with WebView ??
* fix app icon size in web

-----

* migrations on app start:
  * if appCount==0 and there are *.jar files in cache download:
    * reload jars
    * clear icon caches
* repo add icon to display
* AppListActivity: add async icon load

------

* ??? do i need a usecase layer on top of service? i.e. import-v1-usecase
* add support to add a Repo to RepoListActivity
* > Edit repo Dialog
* ? Barcodescanner
* ? url with schema "fdroid:....." via Manifest-Intentfilter ApiActivity with dispatch to affected
  gui
* androidApp:
  * filter by
    * v search expression
    * repo
    * Category
    * android-version
    * sorted by
* add support to download appIcons (and app descr images)

-----

Android-Architecture

* Android-View (Activity/Fragment)
  * connects to ViewModel and observes ViewModel-changes
* Android-ViewModel containing Android-ViewModel with Android-LiveData (MutableLiveData)
  * Connection between View and Service (in android a service is called a "Model" )
  * does not know the view
  * is observable (i.e. by the view)
    * Android-LiveData (MutableLiveData) notifies observers
  * provides lifecycle management and data availablity
  * requires dependency: androidx-lifecycle:lifecycle-xxx (xxx=viewmodel/livedata/runtime...)
* Model
  * ??? usecase ??
  * Service (aka  )
  * Repository
  * Entity
  * Android-Room-Dao (can provide Android-LiveData)

html:

* v TextView: replace "style-bgcolor" with "class status_xxx"

Progress:

    v fdroid-html 
    v de.k3b.fdroid.android.html.GenericTemplateTestAndroid_Broken 
        ? loop over template

files does not work under android

lib

demo:
fatjar: ./gradlew clean :app:demo:bootJar

./gradlew clean test connectedDebugAndroidTest bootJar

cp app/fdroid-universal-cli/build/libs/fdroid-universal-cli.jar . cp
app/fdroid-universal-web/build/libs/fdroid-universal-web.jar .
 




