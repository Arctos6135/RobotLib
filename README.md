# RobotLib
[![master Build Status](https://img.shields.io/travis/com/Arctos6135/RobotLib/master.svg?label=build%20%28master%29&logo=travis)](https://travis-ci.com/Arctos6135/RobotLib/branches)&nbsp;
[![Latest Build Status](https://img.shields.io/travis/com/Arctos6135/RobotLib.svg?label=build%20%28latest%29&logo=travis)](https://travis-ci.com/Arctos6135/RobotLib)&nbsp;
[![Latest Stable Release](https://img.shields.io/github/tag/Arctos6135/RobotLib.svg?logo=data:image/svg%2bxml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBzdGFuZGFsb25lPSJubyI/Pgo8IURPQ1RZUEUgc3ZnIFBVQkxJQyAiLS8vVzNDLy9EVEQgU1ZHIDIwMDEwOTA0Ly9FTiIKICJodHRwOi8vd3d3LnczLm9yZy9UUi8yMDAxL1JFQy1TVkctMjAwMTA5MDQvRFREL3N2ZzEwLmR0ZCI+CjxzdmcgdmVyc2lvbj0iMS4wIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciCiB3aWR0aD0iMzIuMDAwMDAwcHQiIGhlaWdodD0iMzIuMDAwMDAwcHQiIHZpZXdCb3g9IjggOCAxNi4wMDAwMDAgMTYuMDAwMDAwIgogcHJlc2VydmVBc3BlY3RSYXRpbz0ieE1pZFlNaWQgbWVldCI+Cgo8ZyB0cmFuc2Zvcm09InRyYW5zbGF0ZSgwLjAwMDAwMCwzMi4wMDAwMDApIHNjYWxlKDAuMTAwMDAwLC0wLjEwMDAwMCkiCmZpbGw9IiNGRkZGRkYiIHN0cm9rZT0ibm9uZSI+CjxwYXRoIGQ9Ik05NyAyMjMgYy0xOCAtMTcgLTUgLTYxIDI2IC05MSAzOSAtMzggNDcgLTM5IDc1IC05IDI5IDMxIDI4IDM3IC0xMQo3NSAtMzEgMzAgLTc0IDQyIC05MCAyNXogbTgwIC0zNSBsMzMgLTMyIC0yMiAtMjMgLTIyIC0yMyAtMzMgMzIgYy0zMSAzMCAtNDEKNTcgLTI2IDcxIDE0IDE1IDM4IDYgNzAgLTI1eiIvPgo8cGF0aCBkPSJNMTEwIDIwMCBjMCAtNSA1IC0xMCAxMCAtMTAgNiAwIDEwIDUgMTAgMTAgMCA2IC00IDEwIC0xMCAxMCAtNSAwCi0xMCAtNCAtMTAgLTEweiIvPgo8L2c+Cjwvc3ZnPgo=)](https://github.com/Arctos6135/RobotLib/releases/latest)&nbsp;
[![MIT License](https://img.shields.io/github/license/Arctos6135/RobotLib.svg?logo=data:image/svg%2bxml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBzdGFuZGFsb25lPSJubyI/Pgo8IURPQ1RZUEUgc3ZnIFBVQkxJQyAiLS8vVzNDLy9EVEQgU1ZHIDIwMDEwOTA0Ly9FTiIKICJodHRwOi8vd3d3LnczLm9yZy9UUi8yMDAxL1JFQy1TVkctMjAwMTA5MDQvRFREL3N2ZzEwLmR0ZCI+CjxzdmcgdmVyc2lvbj0iMS4wIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciCiB3aWR0aD0iMzIuMDAwMDAwcHQiIGhlaWdodD0iMzIuMDAwMDAwcHQiIHZpZXdCb3g9IjggOCAxNi4wMDAwMDAgMTYuMDAwMDAwIgogcHJlc2VydmVBc3BlY3RSYXRpbz0ieE1pZFlNaWQgbWVldCI+Cgo8ZyB0cmFuc2Zvcm09InRyYW5zbGF0ZSgwLjAwMDAwMCwzMi4wMDAwMDApIHNjYWxlKDAuMTAwMDAwLC0wLjEwMDAwMCkiCmZpbGw9IiNGRkZGRkYiIHN0cm9rZT0ibm9uZSI+CjxwYXRoIGQ9Ik0xMzkgMjIzIGMtMSAtNCAtMSAtMTEgMCAtMTUgMCAtNCAtMTAgLTEwIC0yMiAtMTMgLTEzIC00IC0yNCAtMTIKLTI1IC0xOCAwIC03IC00IC0yMiAtNyAtMzQgLTYgLTIwIC00IC0yMyAxOSAtMjMgMjggMCAzMiAxMCAxNiA0MCAtNyAxMyAtNgoxOSA1IDI0IDIzIDggMjEgLTcwIC0yIC04MyAtMTQgLTggLTggLTEwIDI3IC0xMCAzNSAwIDQxIDIgMjggMTAgLTI0IDEzIC0yNgo5MSAtMyA4MyAxMSAtNSAxMiAtMTEgNSAtMjQgLTE2IC0zMCAtMTIgLTQwIDE2IC00MCAyMyAwIDI1IDMgMTkgMjIgLTMgMTMgLTcKMjggLTcgMzUgLTEgNiAtMTIgMTQgLTI1IDE4IC0xMiAzIC0yMiAxMCAtMjAgMTYgMSA3IC00IDE0IC0xMCAxNiAtNyAzIC0xNCAxCi0xNCAtNHogbS0yNCAtNzMgYzMgLTUgLTEgLTEwIC0xMCAtMTAgLTkgMCAtMTMgNSAtMTAgMTAgMyA2IDggMTAgMTAgMTAgMiAwCjcgLTQgMTAgLTEweiBtOTAgMCBjMyAtNSAtMSAtMTAgLTEwIC0xMCAtOSAwIC0xMyA1IC0xMCAxMCAzIDYgOCAxMCAxMCAxMCAyCjAgNyAtNCAxMCAtMTB6Ii8+CjwvZz4KPC9zdmc+Cg==)](https://github.com/Arctos6135/RobotLib/blob/master/LICENSE)

## Overview

RobotLib is the library created and used by Team Arctos containing many common robot utilities to simplify robot programming.
It contains everything from logging to XBox controller utilities, and much more.

## Usage

### Setup

To use this library, first grab the jar from [the latest release](https://github.com/Arctos6135/RobotLib/releases/latest) and put it somewhere in your robot project, say in the `lib` directory.
Then, in `build.gradle`, under `dependencies`, add this line: `compile files('path/to/jar')`. Your new `dependencies` should look something like this:
```groovy
dependencies {
    compile wpi.deps.wpilib()
    compile wpi.deps.vendor.java()

    compile files('lib/RobotLib-0.2.0.jar')
    
    nativeZip wpi.deps.vendor.jni(wpi.platforms.roborio)
    nativeDesktopZip wpi.deps.vendor.jni(wpi.platforms.desktop)
    testCompile 'junit:junit:4.12'
}
```

## Documentation
All classes in the public API are documented with Javadocs. For every release, you can find the Javadocs as `RobotLib-Doc-<VERSION>.zip`. 

## 2020 Sponsors Coming Soon!
