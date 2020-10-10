#SmartPutty

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

windows 64 bit OS -  SmartPutty\build\distributions\SmartPutty-x64.zip 

`gradle -b build-x86.gradle build` for win-32 bit jar

windows 32 bit OS -  SmartPutty\build\distributions\SmartPutty-x32.zip

**Run it**

unzip the distribution cd into root folder(must root folder) from cmd

`cd SmartPutty\build\distributions\SmartPutty-x64`
`java -jar SmartPutty-x64.jar`

HomeScreen:

![homescreen](https://user-images.githubusercontent.com/5425276/95345562-7057b680-08ed-11eb-870f-b72e2c504d0a.jpg)

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

Issues
1.  if you mean below crash error when open putty, that is newer JDK bug, lower down the jdk minor version can fix it. I've tested jdk1.8.0_111 works well, but jdk1.8.0_261 will crash.
 
 `A fatal error has been detected by the Java Runtime Environment:
 EXCEPTION_ACCESS_VIOLATION (0xc0000005) at pc=0x00007ffbeb286ca0, pid=5956, tid=0x0000000000003734
 JRE version: Java(TM) SE Runtime Environment (8.0_261-b12) (build 1.8.0_261-b12)`