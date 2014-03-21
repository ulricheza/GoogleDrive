About our work
=

What we did
-

* Implemented a **register page**
* Implemented a **login page** with a **remember me option** (30 days)
* **High security level** (password encryption, page access firewall)
* **Used HTTP response codes** (HTTP_NOT_FOUND, HTTP_FORBIDDEN) and added a **custom error page**
* Used **Facelet templates** to design pages
* Files **online creation and edition, uploading, downloading** (in html format) **and sharing**
* Performed all actions with **ajax requests**
* **Used MVC pattern** to separate logic from view
* **Wrote unit and integration tests** to test our application
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

* NetBeans IDE 7.4
* GlassFish Server 4.0

External Dependencies
-
We used **two external librairies**:

* PrimeFaces 4.0
* Apache Commons Codec 1.9 (for password encryption)

These librairies are specified in the **pom.xml** file and will be **automatically downloaded** at compilation time.

Database
-
We used the **Java DB embedded database** of NetBeans (sample). 
Therefore, we **suggest using NetBeans** to deploy this project
in order to avoid running into configuration stuffs.

If you use another IDE (ex: Eclipse), you'll have to edit the **persistence.xml**
file to configure a new database.
