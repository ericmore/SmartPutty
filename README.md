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

`gradle build`

output is under SmartPutty\build\distributions\SmartPutty.zip

cd to the unzipped build, "SmartPutty" folder and run the following cmd, will launch the application as following.
`java -jar SmartPutty`


HomeScreen:

![](https://github.com/ericmore/SmartPutty/blob/dev/doc/image/homescreen.jpg)

User Guide (Demo):
https://github.com/ericmore/SmartPutty/wiki

Development

* Import as git repository to eclipse.
* [Optional] Import as gradle project from Intellij IDEA, if you prefer using IDEA.
* Set compile JDK and compile level to be Java 8 (project use Java 8 new features)
* Run Application with UI/MainFrame.java with [VM Options]:-splash:icon/splash.jpg


Develope Guide (Intellij IDEA)

refer to 'Guide.docx' in root repository

