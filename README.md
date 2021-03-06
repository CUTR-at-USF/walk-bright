WalkBright [![Build Status](https://travis-ci.org/CUTR-at-USF/walk-bright.svg?branch=master)](https://travis-ci.org/CUTR-at-USF/walk-bright)
===========================

Increase your visibility while walking at night with WalkBright! Instead of being distracted playing
with your phone while walking, use your phone to make yourself visible to others!

Download the app via the [Google Play store](https://play.google.com/store/apps/details?id=edu.usf.cutr.android.walkbright).

[![Google Play logo](http://www.android.com/images/brand/android_app_on_play_logo_large.png)](https://play.google.com/store/apps/details?id=edu.usf.cutr.android.walkbright)

**FEATURES**:

* Simple, easy to use.
* Multi-colored flashers draw more attention than standard flashlight apps.

Be seen at night with WalkBright.

*DISCLAIMER: WalkBright is only a tool and is not a guarantee of safety. Be alert, know your
surroundings, stay on sidewalks when possible, walk against traffic when sidewalks aren’t available,
look both ways and use due care before crossing streets.*

*WARNING: This app rapidly turns the screen and camera flash on and off. Some individuals may 
experience epileptic seizures when exposed to flashing lights.  If you, or anyone in your family, 
have an epileptic condition, consult your physician prior to using.  If you experience dizziness, 
altered vision, eye or muscle twitches, loss of awareness, disorientation, any involuntary movement,
or convulsions, IMMEDIATELY discontinue use and consult your physician.*

For more information about pedestrian safety, visit [www.walkwisetampabay.com](http://www.walkwisetampabay.com)

## Build Setup

### Prerequisites for both Android Studio and Gradle

1. Download and install the [Android SDK](http://developer.android.com/sdk/index.html).  Make sure to install the Google APIs for your API level (e.g., 17), the Android SDK Build-tools version for your buildToolsVersion version, and the Android Support Repository and Google Repository.
2. Set the "ANDROID_HOME" environmental variable to your Android SDK location.
3. Set the "JAVA_HOME" environmental variables to point to your JDK folder (e.g., "C:\Program Files\Java\jdk1.6.0_27")

### Building in Android Studio

1. Download and install the latest version of [Android Studio](http://developer.android.com/sdk/installing/studio.html).
2. In Android Studio, choose "Import Project" at the welcome screen.
3. Browse to the location of the project, and double-click on the project directory.
4. If prompted with options, check "Use auto-import", and select "Use default gradle wrapper (recommended)".  Click "Ok".
5. Click the green play button (or 'Shift->F10') to run the project!

### Building from the command line using Gradle

1. To build and push the app to the device, run `gradlew installDebug` from the command line at the root of the project
2. To start the app, run `adb shell am start -n edu.usf.cutr.android.walkbright/.WalkBright` (alternately, you can manually start the app)

### Release builds

To build a release build, you need to create a "gradle.properties" file that points to a "secure.properties" file, and a "secure.properties" file that points to your keystore and alias. The `gradlew assembleRelease` command will prompt for your keystore passphrase.

The "gradle.properties" file is located in the opentripplanner-android directory and has the contents:

```
secure.properties=<full_path_to_secure_properties_file>
```

The "secure.properties" file (in the location specified in gradle.properties) has the contents:

```
key.store=<full_path_to_keystore_file>
```

```
key.alias=<key_alias_name>
```

Note that the paths in these files always use the Unix path separator  `/`, even on Windows. If you use the Windows path separator `\` you will get the error `No value has been specified for property 'signingConfig.keyAlias'.`

### Contributing

We welcome contributions to the project!  Please see our [Contributing Guide](https://github.com/CUTR-at-USF/walk-bright/blob/master/CONTRIBUTING.md) for details, including Code Style Guidelines and Template.

## Troubleshooting

### When importing to Android Studio, I get an error "You are using an old, unsupported version of Gradle..."

If you're using Android Studio v0.4.2 or lower, when importing, please be sure to select the "settings.gradle" file in the root, **NOT** the project directory.
You will get the above error if you select the project directory / name of the project.

### I get build errors for the Android Support libraries or Google APIs

Open Android SDK Manager, and under the "Extras" category make sure you've installed both the "Android Support Repository" (in addition to the "Android Support library") as well as the
 "Google Repository".  Also, make sure you have the Google API installed for the API level that you're working with in the "/build.gradle" file,
 including the "Android SDK Build-tools" version (at the top of the "Tools" category in the Android SDK Manager) that
 matches the compileSdkVersion and buildToolsVersion numbers in /walk-bright/build.gradle.

### I get the import gradle project error - “Cause: unexpected end of block data”

Make sure you have the Google API installed for the API level that you're working with in the `/build.gradle` file,
 including the "Android SDK Build-tools" version (at the top of the "Tools" category in the Android SDK Manager) that
 matches the `compileSdkVersion` and `buildToolsVersion` numbers in `/walk-bright/build.gradle`.

### Android Studio or Gradle can't find my Android SDK, or the API Levels that I have installed

Make sure that you're consistently using the same Android SDK throughout Android Studio and your environmental variables.
Android Studio comes bundled with an Android SDK, and can get confused if you're pointing to this SDK within Android Studio
but have your environmental variables pointed elsewhere.  Click "File->Project Structure", and then under "Android SDK"
make sure you "Android SDK Location" is the correct location of your Android SDK.

Also, make sure you've set the "ANDROID_HOME" environmental variable to your Android SDK location and
the "JAVA_HOME" environmental variables to point to your JDK folder.

## Acknowledgements

Based on [Torch](https://play.google.com/store/apps/details?id=com.colinmcdonough.android.torch) by Colin McDonough.  [Torch source code](https://code.google.com/p/torch/) used under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0).
