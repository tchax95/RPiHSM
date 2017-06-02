# RPiHSM-IoT

This application receives the commands from an implementation of the RPiHSM-API.
The commands are received throught the serial cable port thanks a Pi4J. 

All the information and reports (JavaDocs, JaCoCo Test, Surefire report, JDepend) of the project can be found in the *site* directory after the execution of `mvn clean package site`  on the home directory of the project.

All the given information can be also found in the project documentation.
# Licenses
All dependencies used in this project come with a valid license.

1. [json-simple](https://code.google.com/archive/p/json-simple/): The Apache Software License, Version 2.0

2. [Pi4j](http://pi4j.com/): GNU General Lesser Public License (LGPL) version 3.0

3. [Commons IO](https://commons.apache.org/proper/commons-io/): Apache License, Version 2.0

4. [json](http://www.json.org/): Provided without support or warranty

5. [libpam4j](http://kohsuke.org/):	The MIT license

6. [Keyczar](https://github.com/google/keyczar/blob/master/LICENSE):  Apache License Version 2.0

7. [junit](http://junit.org/junit4/):  Eclipse Public License 1.0

For more information about the project dependencies look at the JDepend file generated by maven.
# Prerequisites
These are the software prerequisites to use the RPiHSM-IoT application.

1. [Java Development Kit 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)

2. [Apache Maven 3.3.9](https://maven.apache.org/)

3. [Git](https://git-scm.com/)

4. [Google keyczar](https://github.com/google/keyczar)

# Configuration
To use the Google Keyczar library the first step to do is to clone the project from the [github proejct](https://github.com/google/keyczar). Then, a new branch with the last library version as name, must be created so that in the compile process errors do not appear.
```bash
git checkout -b Java_release_0.71j
```
To compile the source code so that it can be installed on Maven the following command must be executed in the folder `java/code` of the Keyczar project.
```bash
mvn -e clean compile test package
```
To complete the process, the generated jar must be installed on Maven.
```bash
Mvn install:install-file -Dfile=keyczar-0.71j-031417.jar -DgroupId=org.keyczar -DartifactId=keyczar
-Dversion=0. 71j  -Dpackaging=jar
```
# Raspberry Pi Configuration
To run the application on the Raspberry Pi some specific operations must be executed.
## OS installation and configuration
If the Raspberry Pi  have a valid operation system installed please go to the next step.


1. Download the RASPBIAN JESSIE WITH PIXEL OS and creates a bootable SD card.
2. Modify the cmdline.txt (in the SD card) at the following line

 ```bash
 dwc_otg.lpm_enable=0 console=serial0,115200 console=tty1 root=PARTUUID=402e4a57-02 rootfstype=ext4 elevator=deadline fsck.repair=yes rootwait quiet init=/usr/lib/raspi-config/init_resize.sh splash plymouth.ignore-serial-consoles
 ```
 in
  ```bash
  dwc_otg.lpm_enable=0 console=tty1,115200 kgdboc=tty1,115200 console=tty1,115200 root=PARTUUID=402e4a57-02 rootfstype=ext4 elevator=deadline fsck.repair=yes rootwait quiet init=/usr/lib/raspi-config/init_resize.sh splash plymouth.ignore-serial-consoles
  ```
To execute the next steps, the user interface (HDMI cable, keyboard and mouse) is needed.

## Create new default user
The Pi user is a well know name (change the Pi password!) and it has to much permissions so to have higher security, a new user with the less groups of Pi must be created.
```bash
sudo adduser hsm
```
The goal of this user is to be automatically logged in when the Raspberry Pi starts.
## No password for sudo
So that the hsm user can execute the sudo command without the password (start the application on the RPi start up) the following file /etc/sudoers.d/010_pi-nopasswd must be modified by adding this line in the end of the file.
```bash
hsm ALL=(ALL) NOPASSWD: ALL
```
## Key sets folder
The application needs a specific directory to store the key sets so the following command must be executed.
```bash
sudo mkdir /etc/skel/keyset
```
All the subdirectories of skel will be copied in the home directory of a new user.
## New users
Create all the users that the final application will use with this command (where $user is the name of the user to create).
```bash
sudo adduser $user
```
Then for each user execute these commands (where $user is the name of the created user).

Change the owner of the folder
```bash
sudo chgrp -R hsm /home/$user 
```
Set full rights on the $user and hsm user so that the others RPi users have no rights 
```bash
sudo chmod -R 770 /home/$user
```


## Auto login for HSM user
To auto login the hsm user, so that the IoT application can be executed when the Raspberry Pi starts up, the following commands must be executed.
```bash
sudo cp /lib/systemd/system/getty@.service /etc/systemd/system/autologinhsm@.service
sudo ln -s /etc/systemd/system/autologinhsm@.service /etc/systemd/system/getty.target.wants/getty@tty8.service
sudo nano /etc/systemd/system/getty.target.wants/getty@tty8.service
```
Now the following line that start with ExecStart= must be modified in :
```bash
ExecStart=-/sbin/agetty --autologin hsm %I $TERM
```
Then, this line must be added in the end of the file.
```bash
Alias=getty.target.wants/getty@tty8.service
```
To apply all commands the system must be restarted
```bash
sudo reboot
```

## Ask username and password for login
Now the OS has the auto login enabled and to disable it the following steps must be executed.
```bash
sudo raspi-config
```

1. Boot Options -> Desktop / CLI -> Desktop (Desktop GUI, requiring user to login)

Now the Raspberry Pi will always ask for username and password after the boot.

## Execute the IoT application when RPi starts
Put the IoT application in the home folder of the hsm user then add the following line in the end of the file /home/hsm/.profile.

```
sudo java -jar RPiHSM-IoT-0.1.jar &
```
## Enable Serial Interface
To enable the serial interface the following steps must be executed.
```bash
sudo raspi-config
```
1. Interfacing Options -> Serial -> Disable serial login shell -> Enable serial interface

This disables the possibility to login with the serial interface.

## Test the system
Test the application and if it does not work, the following update must be executed.
```bash
sudo apt-get install libpam-runtime 
sudo apt-get install libpam-modules 
sudo apt-get install libpam-modules-bin 
sudo apt-get install libpam0g-dev 
```
Remember to delete the WiFi password from the /etc/wpa_supplicant/wpa_supplicant.conf and to turn of the wifi when the update operations are done.


## Disables the GUI
To save energy, resources and to have more security the Raspberry GUI must be disabled.
```bash
sudo raspi-config
```

1. Boot Options -> Desktop / CLI -> Console (Text console, requiring user to login)

## Disables other services
To disable the wifi and the bluetooth these lines must be added in the file /etc/modprobe.d/raspi-blacklist.conf.
```bash
#Disable WiFi
blacklist brcmfmac
blacklist brcmutil

#Disable Bluetooth
blacklist btbcm
blacklist hci_uart
```

To disable the HDMI interface, USB ports and RJ45 port these lines must be added at the end of the file /etc/rc.local
```bash
#Disable HDMI
/opt/vc/bin/tvservice -o

#Disable USB and RJ45
echo 0 > /sys/devices/platform/soc/3f980000.usb/buspower

exit 0
```