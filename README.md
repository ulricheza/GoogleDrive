About our work
=

What we did
-

* Implemented a **register page**
* Implemented a **login page** with a **remember me option** (30 days)
* **High security level** (password encryption, page access firewall)
* **Used HTTP status codes** (HTTP_NOT_FOUND, HTTP_FORBIDDEN) and added a **custom error page**
* Used **Facelet templates** to design pages
* Files **online creation and edition, uploading, downloading** (in html format) **and sharing**
* Performed all actions with **ajax requests**
* **Used MVC pattern** to separate logic from views
* **Wrote unit and integration tests** to test our application
* **Used mocks** in our tests
* **Internationalization** (English and French)
* **Used git** to commit our work throughout the entire project

What we did not do
-

* Organise files in **folders**
* Uploading of files with **extensions other than .htm, .html or .txt**

Project Configuration
=

Environnement
-
This project was built with

* [NetBeans IDE 7.4](https://netbeans.org/community/releases/74/)
* [GlassFish Server 4.0](https://glassfish.java.net/)

External Dependencies
-
We mainly used **four external librairies**:

* [PrimeFaces 4.0](http://primefaces.org/) (for JSF components)
* [Apache Commons Codec 1.9](http://commons.apache.org/) (for password encryption)
* [Junit 4.0](http://junit.org/) (for unit tests)
* [Mockito 1.9.5](http://code.google.com/p/mockito/) (for mocks creation and stubbing)

These librairies are specified in the **pom.xml** file and will be **automatically downloaded** (along with their extra dependencies) at compilation time.

Database
-
We used the **Java DB embedded database** of NetBeans (sample). 
Therefore, we **suggest using NetBeans** to deploy this project
in order to avoid running into configuration stuffs.

If you use another IDE (ex: Eclipse), you'll have to edit the **persistence.xml**
file to configure a new database.

How to get the source code
=

* **With git** :  `git clone https://github.com/ulricheza/GoogleDrive.git`
* Or by **downloading the zip file** here on github
