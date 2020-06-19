# Subject

To compile and run Subject (my way), you will need

* Linux
* Git
* Java Development Kit, **preferably OpenJDK**
* Apache Maven
* Apache Derby

_Please note that you could simplify this setup by only using a package manager like APT to install everything needed system-wide. I however prefer to keep my toolchain portable and rebuildable even after toolchain updates from the package manager._

# Create the Subject project folder
Create a folder named **"subject"** somewhere on your filesystem. For the remainder of these instructions we will use ~/subject/.

`mkdir ~/subject`

# Java

Subject was built with Java 14. Download and extract the Linux/x64 tar.gz archive from https://jdk.java.net to ~/subject/

`cd ~/subject`

`tar -xvzf openjdk-<version>_linux-x64_bin.tar.gz`

Create a symlink for easy navigation

`ln -s jdk-<version> jdk`

# Apache Derby

Subject was built with Apache Derby 10.5.2.0. Download and extract the bin distribution tar.gz archive from https://db.apache.org/derby/derby_downloads.html to ~/subject/

Extract the archive

`cd ~/subject`

`tar -xvzf db-derby-<version>-bin.tar.gz`

Create a symlink for easy navigation

`ln -s db-derby-<version>-bin derby`

# Apache Maven

Subject was built with Apache Maven 3.6.3. Download and extract the bin distribution tar.gz archive from https://maven.apache.org/download.cgi to ~/subject/

Extract the archive

`cd ~/subject`

`tar -xvzf apache-maven-<version>-bin.tar.gz`

Create a symlink for easy navigation

`ln -s apache-maven-<version> maven`

# Environment variables
On Ubuntu Linux, add the following to ~/.profile
```
export JAVA_HOME=~/subject/jdk
export DERBY_HOME=~/subject/derby
export MAVEN_HOME=~/subject/maven
export PATH=$PATH:~/subject/jdk/bin
export PATH=$PATH:~/subject/derby/bin
export PATH=$PATH:~/subject/maven/bin
```

If you run a distribution other than Ubuntu Linux, consult you distribution documentation on how to permanently add environment variables.

**Logout and login again for these changes to be activated.**

# Clone the repo
`cd ~/subject`

`git clone https://github.com/cliffordcarnmo/subject.git`

# Toolchain check
The result of the above should be something like

`cd ~/subject`

`ls -l`
```
drwxrwxr-x 6 user user 4096 Nov  7  2019 apache-maven-3.6.3
drwxr-xr-x 9 user user 4096 Jun 10 16:04 db-derby-10.15.2.0-bin
lrwxrwxrwx 1 user user   22 Jun  9 18:04 derby -> db-derby-10.15.2.0-bin
lrwxrwxrwx 1 user user   10 May 27 09:38 jdk -> jdk-14.0.1
drwxrwxr-x 8 user user 4096 May 27 09:31 jdk-14.0.1
lrwxrwxrwx 1 user user   18 Jun 13 09:10 maven -> apache-maven-3.6.3
drwxrwxr-x 8 user user 4096 Jun 19 22:19 subject
```

and you should be able to run commands like

`java`

and

`mvn`

# Configure and start the database
Copy the file **derby.properties** to ~/subject/derby/

`cd ~/subject`

`cp subject/database/derby.properties ~/subject/derby/`

Change the following values in derby.properties

```
derby.drda.portNumber=<your port number>
derby.user.sa=<your password>
```

Start Derby

`startNetworkServer`

Connect to the database with your favourite database management tool or the built in command line tool

`ij`

When connecting to an Apache Derby database, specifying

`create=true`

at the end of the JDBC connection string creates the database on the first connect. Your JDBC connection string might look something like this

```
jdbc:derby://<your address (or localhost)>:<your port number>/SUBJECT;create=true
```

Run the script **~/subject/subject/database/populate-database.sql** to create the schema and tables.

# Setup Spring JPA

Modify the file **~/subject/subject/src/main/resources/application.properties** and add the following
```
spring.datasource.url = jdbc:derby://<your address>:<your port number>/SUBJECT
spring.datasource.username = sa
spring.datasource.password = <your password> (as set above in derby.properties)
```

# Compile and run Subject
`cd ~/subject/subject/`

`mvn spring-boot:run`

# Create a deployable archive (optional)
`cd ~/subject/subject/`

`mvn clean install`

This creates the file **~/subject/subject/target/subject-0.0.1.war** which is a standalone bundle with everything needed to run Subject.

Finally, to run Subject from the standalone bundle

`java -jar subject-0.0.1.war`

# Wrapping up
If you have come this far and successfully built and run Subject you deserve an [ice cream](https://www.gb.se/vara-marken/klassiker/sandwich.html).
