#!/bin/bash

> startup.csv

for i in {1..100}
do
        adb shell am force-stop pl.oczadly.baltic.lsc.android
        #adb shell am start-activity -W -n pl.oczadly.baltic.lsc.android/.view.login.LoginView
        adb shell am start-activity -W -n pl.oczadly.baltic.lsc.android/.view.login.LoginView | grep 'TotalTime' | cut -d ' ' -f 2  >> startup.csv
done
