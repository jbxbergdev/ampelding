# Ampelding

A Jenkins traffic light based on Android Things.

[![Ampelding](https://img.youtube.com/vi/SrXkLx32RiA/0.jpg)](https://www.youtube.com/watch?v=SrXkLx32RiA)


### Usage

(Only Raspberry Pi support for now)

* Connect red light to BCM4
* Connect green light to BCM17
* Configure app via ADB:

```
adb shell am start \
    -n de.bolz.ampelding/.MainActivity \
    -e indicator_id 0 \
    -e base_url <Jenkins base URL> \
    -e job_name <job name> \
    -e jenkins_user <Jenkins user> \
    -e api_token <the user's api token>
```


### Soon to come:

 * Multi job support
 * Support for devices other than RPI
 * configuration persistence

Contributions welcome :-)