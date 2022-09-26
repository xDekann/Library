# Library
## Basic information
My own project (a web app) of a simplified library system. Written with the help of Spring Boot (maven), Spring Security, JPA, Hibernate, mySQL, Thymeleaf, HTML and Bootstrap. It allows authorized users to manage books owned by the library, rent them to the clients and manage rents of clients (add penalty, end rent). It also includes user management system. Certain content is visible based on roles (employee/admin).

![librarymodel](https://user-images.githubusercontent.com/106389146/192172291-7be6f7f5-95a8-4734-a3cb-64cfa848b82b.png)
## How to use
Download the content of the bookstore directory and simply import it to your IDE. Change the content of application.properties according to your mySQL configuration or user account settings, download the content of sql-files directory and import one of the databases from here:
 - Fully-empty-library.sql contains the database without any data.
 - Usable-library.sql contains the database with admin account created (Username: admin Password: admin), password is encrypted in database using Bcrypt. Roles are already added (Employee/Client/Admin)
 - Filled-library.sql a database with some data included, it contains admin user mentioned above, and also employee working account (Username: Susan Password: Susan).
 
After that just run the application. I have also compressed the project into the .jar file, with .properties setup of mine (check the project directory).

## Role permissions
![homesiteadmin](https://user-images.githubusercontent.com/106389146/192172424-b1e213ec-9986-45c7-b5a6-b670455b8410.PNG)

As mentioned in the Basic information section, app has a content action/visibility based on roles.
Same site from employee point of view:

![homesiteempl](https://user-images.githubusercontent.com/106389146/192172559-ab5fc3bf-8e2d-4157-bc9d-a60736f978f2.PNG)

Employee cannot see nor get via URL to users/employees management.

## More detailed information (what can I do in the app?)
- You can create users, employees, clients and manage them (enable/disable account, update, search, delete user).
- Add authors to the database and browse books that are written by them (connected in database).
- Add books and increase their quantity (if the new book delivery has come) or remove quantity if the book is destroyed/lost
- If the new (unique) book is added and you fill the authors text box, it automatically creates authors if they are not in database and connects the book to them, if they are existent, it just connets
- Check the book availability based on quantity (rented books are not counted as available)
- Rent the books to the clients, see their rents, add new rents, cancel the rents if they have returned the book, add penalty depending on the length of the rent (by default it is automatically added after 7 days of rent in the code), the end date of rent is being displayed as well as the start.
## Screenshots

![Books](https://user-images.githubusercontent.com/106389146/192173019-0d9ceaa2-3c8f-4a75-a148-d8b2593f0de6.PNG)

![Logout](https://user-images.githubusercontent.com/106389146/192173084-2279f579-4018-4685-95cc-e6881d0d51f1.PNG)

![Client-rents](https://user-images.githubusercontent.com/106389146/192173088-8b3f896e-3e0a-4198-9ab7-874a2f55d2ee.PNG)

## To do
The project still has some things to change and possible customer site additions.
