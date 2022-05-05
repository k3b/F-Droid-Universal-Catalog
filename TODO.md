todo

* switch to ViewModel with LiveData

----

Android-Architecture

* Android-View (Activity/Fragment)
    * connects to ViewModel and observes ViewModel-changes
* Android-ViewModel containing Android-ViewModel with Android-LiveData (MutableLiveData)
    * Connection between View and Service (in android a service is called a "Model" )
    * does not know the view
    * is observable (i.e. by the view)
        * Android-LiveData (MutableLiveData) notifies observers
    * provides lifecycle management and data availablity
    * requires dependency androidx-lifecycle:lifecycle-xxx (xxx=viewmodel/livedata/runtime...)
* Service (in android a service is called a "Model" )
* Repository
* Android-Room-Dao (can provide Android-LiveData)

html:
v TextView: replace "style-bgcolor" with "class status_xxx"

Progress:

    ?? Workmanager Query by status ?? 
    ? status only in log but not attached to view 
    ? setText(null) ==> reload all 
    ? 🗃 F-Droid Archive twice instead of app/versionInfo - menu refresh

    v fdroid-html 
    v de.k3b.fdroid.android.html.GenericTemplateTestAndroid_Broken 
        ? loop over template

files does not work under android

- Android RepoListActivity v Android RepoListAdapter - yellow=Download-in-progress - red = error
    - Status area with progress info
        - http-dwonload-progress ( 🗺 5 / 3500 KB )
        - parse-download-progress ( 🗃 4 / 3800 ⬇ 🏬 ℹ )

lib

demo:
fatjar: ./gradlew clean :app:demo:bootJar

./gradlew clean test connectedDebugAndroidTest bootJar


