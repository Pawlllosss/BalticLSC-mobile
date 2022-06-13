# BalticLSC-mobile
A mobile application client of the HPC platform BalticLSC created for purposes of the master thesis.

Project website: https://www.balticlsc.eu/

App download: https://www.balticlsc.eu/downloads/

# Functionality
The application provides almost identical functionalities as the [web client for the BalticLSC platform](https://balticlsc.iem.pw.edu.pl/#/).

Most important functionalities:
* Presenting and managing applications
* Presenting and managing computation tasks
* Presenting and managing data sets
* Displaying CAL (*computation application language*) diagrams

The biggest difference is lack of modify and create CAL diagrams functionality.

# How to run
Either download and install the APK file from the BalticLSC website, or run the project from Android Studio with use of the AVD emulator.

# Stack
* Kotlin
* Kotlin Multiplatform Mobile
* Gradle
* MockWebServer
* Espresso

# Repo structure

* BalticLSC
  * src   <---- Android specific files
* shared
  * src/androidMain  <---- Android implementation of functions/classes signatures in commonMain 
  * src/commonMain   <---- code shared between Android and iOS version, or class/function signature
  * src/iosMain      <---- iOS implementation of functions/classes signatures in commonMain 


# Supported devices
This application supports devices with Android 9 (Android SDK 28). It's possible to implement iOS version with use of the shared code in the `shared` module.

# Screenshots

## Lists
<p float="left">
  <img src="https://user-images.githubusercontent.com/20254121/173412595-e6a39902-0342-4697-b1b9-06c2d0bc3539.png" width=250px/>
  <img src="https://user-images.githubusercontent.com/20254121/173412622-4d762b58-a55e-4106-850d-e33da824fabf.png" width=250px/>
  <img src="https://user-images.githubusercontent.com/20254121/173412640-68f3c107-6f9e-4a6e-96e8-9691aac38e98.png" width=250px/>
</p>

## Details

<p float="left">
  <img src="https://user-images.githubusercontent.com/20254121/173412730-96c23dd3-8711-417b-a7bf-dd438b8bea96.png" width=250px/>
  <img src="https://user-images.githubusercontent.com/20254121/173412761-89be167c-4ba1-4933-a70a-7ba541892aaf.png" width=250px/>
</p>

## CAL diagram
![cal_diagram_rotated(1)](https://user-images.githubusercontent.com/20254121/173412777-54a8b5ef-881d-4a39-b647-5b450af2d08b.png)
