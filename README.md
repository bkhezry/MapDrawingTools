## simple library for handle markers, polygons and polylines on google maps.

MapDrawingTools is an android library to Drawing manually polygon, polyline and points in the Google Map and return coordinates from library to your App

**Project Setup and Dependencies**
- JDK 8
- Android SDK Build tools 25.0.2
- Supports API Level +17
- AppCompat & Cardview libraries 25.3.1
- Google Play Services Maps 10.2.0

**Highlights**
- Add point of Polygon, Polyline and Markers with Tab on the Map
- Relocation all Markers and draw them with dragging the Markes.
- Supports calculating `area` of polygon & `length` of polyline
- Supports Undo point inserted to the Map

# Preview
## Demo
You can download the latest demo APK from here: https://github.com/bkhezry/MapDrawingTools/blob/master/assets/DemoMapDrawingTools.apk

## Screenshots
<img src="assets/screenshot_1.png" />
<img src="assets/screenshot_2.png" />
<img src="assets/screenshot_3.png" />

# Setup
## 1. Provide the gradle dependency
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
Add the dependency:
```gradle
dependencies {
	compile 'com.github.bkhezry:MapDrawingTools:1.1.0'
}
```

## 2. Add your code


 
# Developed By

* Behrouz Khezry
 * [@bkhezry](https://twitter.com/bkhezry) 


# License

    Copyright 2017 Behrouz Khezry

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
