LINK: https://github.com/kotommi/cyber-security-base-19
Link to this document with proper formatting: https://github.com/kotommi/cybersecuritybase-project/blob/master/report.md
Should work with tmcbeans (untested) or by installing maven and running
```
mvn spring-boot:run
```
in project root.

### FLAW 1
#### Cross-site scripting (XSS)
The application has a stored XSS vulnerability in the event registration system. When viewing all signups for the event, unescaped text is inserted into the page. A malicious user could sign up with a name or address that contains script tags enclosing harmful javascript and that script would run on any users browser that views the page. 
#### How to fix
Replace all the th:utext tags in list.html with th:text. This way Thymeleaf treats user inputted data as text (by escaping it) instead of as html. Additional validation could be done in the SignupController-class' method submitForm() to discard unwanted entries.

### FLAW 2
#### Broken Access Control
Users are supposed to only delete their own registrations, but by modifying the html in list.html they can send requests to /signup/{id} and delete any signup they want as long as they are logged in. 
#### How to fix
Validate that the user requesting the removal of a signup also created the signup in SignupController's removeSignup-method.

### FLAW 3
#### Broken Authentication
There are so many flaws of this category in the app. The session-cookies are generated with a simple running number "algorithm" so session-jacking is very easy. The session ids don't expire after a set time, only on explicit logout. The admin user and it's weak password is hardcoded in the app. Account registration accepts short passwords.
#### How to fix
Fixing sessions: Remove the sessionid generator class Manager.java and revert back to default settings in the Application.java class by removing the method calls to sethttponly() and setManager() in the file. Create a application.properties file and  setserver.servlet.session.timeout=60m to make the sessionIDs expire with timeout too.
Fixing passwords: Enforce reasonable password-length in the account registration form. Check passwords against a list of top10k most common passwords list to make sure they are not vulnerable.
Fixing admin: Remove the username "admin" and user proper role-based authentication system. 
Credential stuffing: Stop accepting login attemps for a while for an account after an x amount of attempts. Enable logging for this kind of attack.
...

FLAW 5:
<description of flaw 5>
<how to fix it>