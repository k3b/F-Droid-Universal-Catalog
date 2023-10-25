todo

* Rename existing url-parameter "-v(ersion)" to "-s(dk-version)"

> webservice api to allow angular or reduce web client
> v category (to get mapping/combobox categoryId to categoryName)
> appDetail
> v appDetail
* filter returned locales

* locale (to get mapping/checkboxex localeId to localeCode)

> demo json data for angular or react web client

------

* web-detail rendering either with or without <pre>

* download before delete&rename must check if signature is ok

* android: when version-import completed clear caches => recreate AppList-adapter
* android: icon download complete => logcat

-----

* migrations on app start:
  * if appCount==0 and there are *.jar files in cache download:
    * reload jars
    * clear icon caches

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

-----


??? use gdroid-bot data ??
https://gitlab.com/gdroid/gdroiddata/-/blob/master/metadata/tags.yaml

			file_browser:
			  - com.amaze.filemanager
			  - com.dnielfe.manager

			gallery:
			  - com.simplemobiletools.gallery
			  - com.simplemobiletools.gallery.pro
			  - de.k3b.android.androFotoFinder
			  - org.horaapps.leafpic
			  - us.koller.cameraroll
		

		https://gitlab.com/gdroid/gdroiddata/raw/master/metadata/gdroid.jar

		C:\Users\eve\Downloads\gdroid.jar/metadata/gdroid.json


		{
		 "com.simplemobiletools.gallery.pro": {
		  "localized": {
		   "en-US": {
			"phoneScreenshots": [
			 "https://raw.githubusercontent.com/SimpleMobileTools/Simple-Gallery/master/fastlane/metadata/android/en-US/images/phoneScreenshots/app_5.jpg", 
			 "https://raw.githubusercontent.com/SimpleMobileTools/Simple-Gallery/master/fastlane/metadata/android/en-US/images/phoneScreenshots/app.jpg", 
			 "https://raw.githubusercontent.com/SimpleMobileTools/Simple-Gallery/master/fastlane/metadata/android/en-US/images/phoneScreenshots/app_2.jpg", 
			 "https://raw.githubusercontent.com/SimpleMobileTools/Simple-Gallery/master/fastlane/metadata/android/en-US/images/phoneScreenshots/app_3.jpg", 
			 "https://raw.githubusercontent.com/SimpleMobileTools/Simple-Gallery/master/fastlane/metadata/android/en-US/images/phoneScreenshots/app_6.jpg", 
			 "https://raw.githubusercontent.com/SimpleMobileTools/Simple-Gallery/master/fastlane/metadata/android/en-US/images/phoneScreenshots/app_4.jpg", 
			 "https://raw.githubusercontent.com/SimpleMobileTools/Simple-Gallery/master/fastlane/metadata/android/en-US/images/tenInchScreenshots/tablet-10.jpg", 
			 "https://raw.githubusercontent.com/SimpleMobileTools/Simple-Gallery/master/fastlane/metadata/android/en-US/images/sevenInchScreenshots/tablet-7.jpg"
			]
		   }
		  }, 
		  "metrics": {
		   "added_on_day": 17851, 
		   "age_in_days": 1309, 
		   "age_last_v_12": 0.9917, 
		   "age_last_v_24": 0.9958, 
		   "age_last_v_3": 0.9667, 
		   "age_last_v_6": 0.9833, 
		   "avg_update_frequency": 2.5961, 
		   "avg_update_frequency_normalised": 1, 
		   "m_github_stars": 2325, 
		   "m_github_stars_normalised": 1, 
		   "m_github_stars_per_day": 1.7762, 
		   "m_github_stars_per_day_normalised": 1, 
		   "updated_on_day": 19157, 
		   "uptodatesince": 3
		  }, 
		  "neighbours": {
		   "a": [
			"us.koller.cameraroll", 
			"com.simplemobiletools.calendar.pro", 
			"com.simplemobiletools.keyboard", 
			"com.simplemobiletools.voicerecorder", 
			"site.leos.apps.lespas", 
			"com.gtp.showapicturetoyourfriend", 
			"com.simplemobiletools.smsmessenger", 
			"de.baumann.browser", 
			"org.piwigo.android"
		   ]
		  }, 
		  "tags": [
		   "gallery"
		  ]
		 }, 
		 "An.stop": {
		  "metrics": {
		   "added_on_day": 15159, 
		   "age_in_days": 4001, 
		   "age_last_v_12": 0.0000, 
		   "age_last_v_24": 0.0000, 
		   "age_last_v_3": 0.0000, 
		   "age_last_v_6": 0.0000, 
		   "m_github_stars": 4, 
		   "m_github_stars_normalised": 0, 
		   "m_github_stars_per_day": 0.0010, 
		   "m_github_stars_per_day_normalised": 0.0063, 
		   "updated_on_day": 15562, 
		   "uptodatesince": 3598
		  }, 
		  "neighbours": {
		   "a": [
			"com.willianveiga.countdowntimer", 
			"edu.killerud.kitchentimer", 
			"org.dpadgett.timer", 
			"org.mattvchandler.progressbars", 
			"com.simplemobiletools.clock", 
			"com.best.deskclock", 
			"omegacentauri.mobi.simplestopwatch", 
			"com.kodarkooperativet.notificationstopwatch", 
			"com.philliphsu.clock2"
		   ]
		  }
		 }, ....
		 
		 "de.k3b.android.calef": {
		  "metrics": {
		   "added_on_day": 19013, 
		   "age_in_days": 147, 
		   "age_last_v_12": 0.7417, 
		   "age_last_v_24": 0.8708, 
		   "age_last_v_3": 0.0000, 
		   "age_last_v_6": 0.4833, 
		   "avg_update_frequency": 19.1373, 
		   "avg_update_frequency_normalised": 1, 
		   "m_github_stars": 5, 
		   "m_github_stars_normalised": 0, 
		   "m_github_stars_per_day": 0.0340, 
		   "m_github_stars_per_day_normalised": 0.2154, 
		   "updated_on_day": 19067, 
		   "uptodatesince": 93
		  }, 
		  "neighbours": {
		   "a": [
			"ws.xsoh.etar", 
			"de.k3b.android.calendar.ics.adapter", 
			"org.sufficientlysecure.standalonecalendar", 
			"com.simplemobiletools.calendar.pro", 
			"org.billthefarmer.diary", 
			"com.forrestguice.suntimescalendars", 
			"de.k3b.android.toGoZip", 
			"com.sweetiepiggy.everylocale", 
			"org.sufficientlysecure.localcalendar"
		   ]
		  }
		 }, 
		 .... 
		}

-----

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
 




