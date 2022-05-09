https://github.com/k3b/F-Droid-Universal-Catalog/fdroid-universal-html/README.md

# reusable gui components for F-Droid-Universal-Catalog

Goal: Proof of concept gui component for the "k3b-Architecture", that should contain as much
platform-independent code as possible.

F-Droid-Universal-Catalog uses
[mustache-template-engine](http://mustache.github.io/mustache.5.html) based html-snippets to
implement common gui elements.

* Android TextView has limited html-display capabilities.
*
    * no css support
*
    * use de.k3b.fdroid.android.html.util.HtmlUtil#setHtml(TextView, html, ....) to to add
*
    *     limited css-color support to TextView
* Android WebView has full html-display capabilities.
* The mustache-template-engine com.samskivert:jmustache
*
    * works for android and for spring-boot and
*
    * has minimal external library dependencies.

Outside of web content the de.k3b.fdroid.html.service.FormatService can be used to expand mustache
templates
