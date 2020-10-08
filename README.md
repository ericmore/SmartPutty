#SmartPutty
![homescreen](https://user-images.githubusercontent.com/5425276/95345562-7057b680-08ed-11eb-870f-b72e2c504d0a.jpg)
<p>
SSH,SCP,SFTP,VNC remote to Linux Machine from Windows.


SmartPutty for windows platform provides rock-solid terminal emulation for computing professionals, raising productivity with advanced session management and a host of ways to save time and streamline repetitive tasks. SmartPutty provides secure remote access, file transfer, VNC access, Microsoft Remote Desktop
</p>

**Installing**

**Clone a copy of the repo:**

`git clone https://github.com/ericmore/SmartPutty.git`

**Change to the SmartPutty directory**

`cd SmartPutty`

**Build it**

`gradle build` for win-64 bit jar

windows 32 bit OS -  SmartPutty\build\libs\SmartPutty-x64.jar 

`gradle -b build-x86.gradle build` for win-32 bit jar

windows 64 bit OS -  SmartPutty\build\libs\SmartPutty-x64.jar



HomeScreen:

![](https://github.com/ericmore/SmartPutty/blob/dev/doc/image/homescreen.jpg)

User Guide (Demo):
https://github.com/ericmore/SmartPutty/wiki

Development

* Import as git repository to eclipse.
* [Optional] Import as gradle project from Intellij IDEA, if you prefer using IDEA.
* Set compile JDK and compile level to be *Java 8*
* java -jar SmartPutty-x64/x86.jar

Developer Guide (Intellij IDEA)VERSION
1. Mark lib and libArch as Library(swt.x64 for 64bit dev machine, swt.x86 for 32bit dev machine)
2. Database is using H2 file base, so the schema will be created first time in your local disk ~/smartputty.db

H2 Intellij Database Configuration
![h2](https://user-images.githubusercontent.com/5425276/95461537-ea994100-09a8-11eb-9e5d-5be10ca94a1a.PNG)

