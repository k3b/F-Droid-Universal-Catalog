https://github.com/k3b/F-Droid-Universal-Catalog/app/README.md

# F-Droid-Universal-Catalog android app

Goal: Proof of concept android app for the "k3b-Architecture", that should contain as much
platform-independent code as possible.

## Android-Architecture

* Android-View (Activity/Fragment)
  * connects to ViewModel and observes ViewModel-changes
* Android-ViewModel containing Android-ViewModel with Android-LiveData (MutableLiveData)
  * Connection between View and Service (in android a service is called a "Model" )
  * does not know the view
  * is observable (i.e. by the view)
    * Android-LiveData (MutableLiveData) notifies observers
  * provides lifecycle management and data availablity
  * requires dependency: androidx-lifecycle:lifecycle-xxx (xxx=viewmodel/livedata/runtime...)
* Model ("k3b-Architecture": Mostly platformindependent Services, Entities, Interfaces)
