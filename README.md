##Project fmu-core

###JIRA Link:
[inera-certificate.atlassian.net](https://inera-certificate.atlassian.net/browse/FMU)

###Technologies required:
1. Language: _Java_ - Java Development Kit (preferably oracle jdk 1.7 and latest update, current 51, openjdk can cause problems)
2. IDE: _IntelliJ, or STS, or NetBeans_
3. Production Database: _MySql_ (preferably the latest version, e.g. 5.6)
4. Test Database: _hsqldb_
5. Code Management: _Git_
6. Dependency Management: _Maven 3.0_
    
###Code Base
1. Download and install Git.
2. Right the folder which you want to be the home for the codebase, and choose Git Bash.(For integration of IntelliJ with Git, refer to  the section below _Setting Up the Environment->IntelliJ_ point 4 onward)
3. Paste `git clone git@github.com:rasheedamir/fmu-core.git`
4. Give the password when prompted.
    
###Project Structure

    
###Setting up the Environment:
- JAVA

- Maven

- IntelliJ
    1. Install an IDE. Here we will assume the usage of IntelliJ. Download [IntelliJ IDEA](http://www.jetbrains.com/idea/download/index.html). Ultimate version needs to be bought. Install IntelliJ.
    2. Configure Groovy in IntelliJ.
        - At startup after IntelliJ installation, add support for the plugin of Grails.
        - Otherwise, you can later change it from _File -> Settings_. Then choose plugins, and check _Grails_.
    3. Enable Git in IntelliJ, as mentioned in the step above.
    4. After that, go to _Project->Settings_, under _Version Control_, click Git. On the right hand panel, you need to give the address to the git executable, named _git.exe_, present in `.../Git/bin/`.
    5. For the ssh drop down right below the executable field, its preferable to choose "Built In".
    6. For more information on this issue, refer to [jetbrains website](http://www.jetbrains.com/idea/webhelp/using-git-integration.html).
    7. For projects being developed on cross-platform operatins systems, windows uses CRLF line endings(a format) and Linux, OS X use LF line ending format. If not taken care of these line endings will be changed from one format to the other, causing in merge conflicts. There are 2 possible solutions:
        - If using from Git, you need to change the 'core.autocrlf' property in the Git config to 'true'(for Windows) or to 'input' in case of Linux.
        - From IntelliJ itself wth Git integrated, you need to place an xml file here: `.IntelliJIdea12\config\codestyles\Default _1_.xml`, which contains policies for the commiting.
More info on this topic can be found [here](http://stackoverflow.com/questions/3206843/how-line-ending-conversions-work-with-git-core-autocrlf-between-different-operat)

- Git
    1. Download latest version of git
        - [msgit for windows](https://code.google.com/p/msysgit/downloads/list?q=full+installer+official+git)
        - `sudo apt-get install git-core` - for ubuntu/debian
    2. Select point 3 _Run git and included unix tools from the windows command prompt_, when needed. _Path_ will be updated during installation.
    3. Open console and check `git --version`. The result should be like `git version 1.9.0.msysgit.0`.
    4. If git installation successful, generate ssh keys and add it to Bitbucket account, follow to the [official guide](https://confluence.atlassian.com/display/BITBUCKET/Set+up+SSH+for+Git).

- MySql
    - Window users:

    1. Download latest version of [MySql community server](http://dev.mysql.com/downloads/mysql/)
    2. Run `.exe` or `.msi` file and follow the instructions.
    3. Select _Developer default_.
    4. Specify password for _root_ user.

    Follow [this guide](http://www.mysqltutorial.org/install-mysql/), if there is any questions.

    - Ubuntu/Debian users: `sudo apt-get install mysql-server mysql-client`

###Database Setup
  1. Create a new database named `---`.
  2. MySql Settings:
      - username = `----`,
      - password = `----`,
      - host = `----`,
      - port = `3306`.
  3. Open the terminal and type `mysql -u root -p`
  4. Enter the password when prompted `---`
  5. Create database `create database ---` (The output should be "Query OK, 1 row affected")

###Using Codebase

    
###fmu-core from Command Prompt

    
###fmu-core on IntelliJ


###Debug on Intellij
   1. Open _Run -> Edit configurations_
   2. Click green `+` and choose _remote_
   3. Set name e.g. _debug_, 

       **Use following settings**:

       - _Transport_: `Socket`
       - _Debugger mode_: `Attach`
       - _Host_: `localhost`
       - _Port_: `5005` - it's default grails debug port

###Logs
  - Application logs can be found here: 
    
###TroubleShooting
  - There may be errors while running the application
