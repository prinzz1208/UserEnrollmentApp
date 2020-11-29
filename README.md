# UserEnrollmentApp
### Internship Selection Task
## Android Internship Assignment
Problem statement:
Develop an Android native application to enrol & display users. There are two
swipeable pages in this application. First page shows the list of saved users
and the second page is to add users.
Required:

* Users added in the second page must be reflected in the first page. List
must be ordered by added in descending order (Last added user must
be on the top).
* On tapping the delete icon, the user must be removed from the list.
* Both pages must be scrollable & the main page must be swipeable.
* Use any local storage to store user information.
* Apply proper validation for all fields in page 2.
* Application UI must be responsive.
Additional:
* Instead of local storage if you use a firebase database that will be an
additional score for you.
* Restrict users to add duplicate phone numbers.
* Prefer storyboard design instead of interface builder.
* Additional scores will be added if you can avoid using third party
libraries.

Evaluation will be based on the required & additional requirements.
Note: You must share project code with some VCS (GitHub, bitbucket etc).

***
### Solution
- App follows MVVM architecture.
- Firebase Firestore for storing User details.
- Firebase Storage for storing User profile Images.
