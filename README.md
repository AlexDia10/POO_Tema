**IMDB Management System**

This project is a console-based IMDB Management System that allows users, contributors, and admins to interact with a simulated IMDB-like database of actors, movies, and series.

**Overview**

The system supports three types of users:

Regular Users: Can search, review productions, and manage personal favorites.
Contributors: Can add, remove, or update actors, movies, and series. They can also handle user requests.
Admins: Have full control over users, actors, productions, and system requests.
All data (users, actors, productions, requests) is loaded from JSON files at startup and used to simulate a persistent database.

**Key Functionalities**

Login System
Users are prompted to log in with username and password.
User data is loaded from users.json.
Depending on the user's role (Regular, Contributor, Admin), different menus and permissions are displayed.

**Main Menu Options**

After login, users can perform different actions based on their roles.

**Data Loading**

On startup, the system reads:
users.json (list of users and their roles)
actors.json (list of actors and their details)
productions.json (list of movies and series)
requests.json (pending user requests)
Data is parsed using Jackson library, with some custom deserializers for flexibility.

**Actor, Movie, Series Search**

Users can search for:
Actors by name.
Movies by title.
Series by title.
Results show all matching items and their details.

**Favorites Management**

Regular users and contributors can add or remove productions and actors to or from their personal favorites list.

**Reviews**

Regular users can add reviews to productions.
They can also delete reviews they previously created.

**Requests System**

Regular users can submit requests, such as asking for new actors, movies, or corrections to existing data.
Contributors and admins can view, process, and resolve these requests.

**Admin and Contributor Management**

Contributors can add and remove actors or productions (movies and series) from the system.
Admins can manage both actors and productions, as well as user accounts, adding or removing users as needed.
