todo

html:
v TextView: replace "style-bgcolor" with "class status_xxx"

Progress:
?? Workmanager Query by status ?? ? status only in log but not attached to view ? setText(null) ==>
reload all ? 🗃 F-Droid Archive twice instead of app/versionInfo - menu refresh

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


