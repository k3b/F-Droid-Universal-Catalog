todo


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
  * Service (aka  )
  * Repository
  * Entity
  * Android-Room-Dao (can provide Android-LiveData)

html:
v TextView: replace "style-bgcolor" with "class status_xxx"

Progress:

    v fdroid-html 
    v de.k3b.fdroid.android.html.GenericTemplateTestAndroid_Broken 
        ? loop over template

files does not work under android

lib

demo:
fatjar: ./gradlew clean :app:demo:bootJar

./gradlew clean test connectedDebugAndroidTest bootJar


