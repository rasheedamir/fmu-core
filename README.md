##Project fmu-core

##JIRA Link:
[Inera FMU](https://inera-certificate.atlassian.net/browse/FMU)

##Technology Stack (Tools & Frameworks):

1. Language: _Java 1.7_
2. IDE: _IntelliJ (Recommended), STS, or NetBeans_
3. Production Database: _MySql 5.6.4+_
4. Test Database: _H2_
5. Code Management: _Git_
6. Dependency & Build Management: _Maven 3.0_
7. Database Migration Tool: _Liquibase 3_
8. Logging Abstraction: _SFL4J_
9. Logging Implementation: _logback_
10. Unit Testing: _JUnit 4_ — The de-facto standard for unit testing Java applications.
11. Persistence (Data Access): _JPA_, _Hibernate 4_ (One of the most popular JPA implementations.), _Spring Data JPA_ (Makes it easy to easily implement JPA based repositories), _Spring ORMs_ (Core ORM support from the Spring Framework)
12. BPM: _Activiti 5.15_
13. Mocking: _Mockito_
14. Application Server: _Tomcat_
15. Object to JSON Mapping: _Jackson_
16. Date Time Utility: _Joda-Time_
17. Assertion Library: _Hamcrest_ - A library of matcher objects (also known as constraints or predicates) allowing assertThat style JUnit assertions.
18. Integration Test Support: _Spring Test_ — integration test support for Spring applications.
19. JDBC Connection Pool: _HikariCP_ - [HikariCP](http://brettwooldridge.github.io/HikariCP/) is a high performance JDBC connection pool.
20. Properties file format: _YAML_
21. @ToString: [_lombok_](http://projectlombok.org/)
22. AOP: _Spring AOP_ Logging, Transactions, 
23. Profiles: _Maven & Spring Profiles_
24. 

###Code Base
1. Download and install Git.
2. Right the folder which you want to be the home for the codebase, and choose Git Bash.(For integration of IntelliJ with Git, refer to  the section below _Setting Up the Environment->IntelliJ_ point 4 onward)
3. Paste `git clone git@github.com:rasheedamir/fmu-core.git`
4. Give the password when prompted.
    
###Project Structure

    
##Setting up the Environment:

###- JAVA

###- Maven

###- IntelliJ
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

###- Git
1. Download latest version of git
    - [msgit for windows](https://code.google.com/p/msysgit/downloads/list?q=full+installer+official+git)
    - `sudo apt-get install git-core` - for ubuntu/debian
2. Select point 3 _Run git and included unix tools from the windows command prompt_, when needed. _Path_ will be updated during installation.
3. Open console and check `git --version`. The result should be like `git version 1.9.0.msysgit.0`.
4. If git installation successful, generate ssh keys and add it to Bitbucket account, follow to the [official guide](https://confluence.atlassian.com/display/BITBUCKET/Set+up+SSH+for+Git).


###- MySql
- Window users:

1. Download latest version of [MySql community server](http://dev.mysql.com/downloads/mysql/)
2. Run `.exe` or `.msi` file and follow the instructions.
3. Select _Developer default_.
4. Specify password for _root_ user.

Follow [this guide](http://www.mysqltutorial.org/install-mysql/), if there is any questions.

- Ubuntu/Debian users: `sudo apt-get install mysql-server mysql-client`

###- Database Setup
  1. Create a new database named `---`.
  2. MySql Settings:
      - username = `----`,
      - password = `----`,
      - host = `----`,
      - port = `3306`.
  3. Open the terminal and type `mysql -u root -p`
  4. Enter the password when prompted `---`
  5. Create database `create database ---` (The output should be "Query OK, 1 row affected")

DROP DATABASE `fmu`;

CREATE DATABASE `fmu` CHARACTER SET utf8 COLLATE utf8_general_ci;

###Using Codebase

    
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

##Logs
  - Application logs can be found here: 
    
##TroubleShooting
  - There may be errors while running the application

##Generating Liquibase ChangeLog

##Running/Debugging the Application

###Add _'resources'_ directory to classpath in IntelliJ 13

1. Click on the Project view or unhide it by clicking on the "1: Project" button on the left border of the window or by pressing Alt + 1
2. Find your project or sub-module and click on it to highlight it, then press F4, or right click and choose "Open Module Settings"
3. Click on the dependencies tab
4. Click the "+" button on the right and select "Jars or directories..."
5. Find your path and click OK
6. In the dialog with "Choose Categories of Selected File", choose classes (even if it's properties), press OK and OK again

You can now run your application and it will have the selected path in the classpath.

###As a "main" Java class
From your IDE, right-click on the "Application" class at the root of your Java package hierarchy, and run it directly. You should also be able to debug it as easily.

The application will be available on http://localhost:8080.

###As a Maven project
You can launch the Java server with Maven:

mvn spring-boot:run -Pprod

The application will be available on http://localhost:8080

If you want more information on using Maven, please go to http://maven.apache.org

##Profiles

fmu-core comes with two "profiles":

  - _"dev"_ for development: it focuses on ease of development and productivity
  - _"prod"_ for production: it focuses on performance and scalability

Those profiles come in two different configurations:

1. The Maven profiles are used at build time. For example mvn -Pprod package will package a production application.
2. The Spring profiles work a run time. Some Spring beans will behave differently, depending on the profile.

Spring profiles are set by Maven, so we have a consistency between the two methods: of course, you should have a "prod" profile on Maven and Spring at the same time.

###dev
In default mode, fmu-core will use the "dev" profile
If you run the application without Maven, launch the "Application" class (you can probably run it easily from your IDE by right-clicking on it).

If you run the application with Maven, run mvn -Pdev spring-boot:run

###prod
In production, fmu-core has to run with the "prod" profile
Use Maven to build the application with the "prod" profile: mvn -Pprod spring-boot:run

##Plugins

###EditorConfig
EditorConfig helps developers define and maintain consistent coding styles between different editors and IDEs. Read more [here](http://editorconfig.org/)

mvn resources:resources liquibase:update -P<profile_name>
Invoking the resources is necessary in order to have the liquibase.properties placeholders filtered. The -P option tells Maven the profile to use and thus the set of values (from the filter properties file) to use for filtering.

mvn resources:resources liquibase:diff -Pprod
