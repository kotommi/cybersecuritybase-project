LINK: https://github.com/kotommi/cybersecuritybase-project

Link to this document with proper formatting:
https://github.com/kotommi/cybersecuritybase-project/blob/master/report.md

OWASP Top 10 list used: https://www.owasp.org/images/7/72/OWASP_Top_10-2017_%28en%29.pdf.pdf

Should work with tmcbeans (untested) or by installing maven and running

```
mvn spring-boot:run
```

in project root.

### FLAW 1

#### Cross-site scripting (XSS)

The application has a stored XSS vulnerability in the event registration system. When viewing all signups for the event, unescaped text loaded from the database is inserted into the page. A malicious user could sign up with a name or address that contains script tags enclosing harmful javascript and that script would run on any users browser that views the page, which could lead to the user downloading malware or leaking their session id.

#### How to fix

Replace all the th:utext tags in list.html with th:text. This way Thymeleaf treats user inputted data as text (by escaping it) instead of as html. Additional validation could be done in the SignupController-class' method submitForm() to discard unwanted entries.

### FLAW 2

#### Broken Access Control

Users are supposed to only delete their own registrations, but for example by modifying the html of delete button in list.html they can send requests to /signup/{id} and delete any signup they want as long as they are logged in.

#### How to fix

Validate that the user requesting the removal of a signup also created the signup in SignupController's removeSignup-method. The ORM repository knows about the one-to-many mapping between accounts and signups so you can just check what account made the request (done already in the add signup method) and check which account owns the signup.

### FLAW 3

#### Broken Authentication

There are so many flaws of this category in the app.

The session-cookies are generated with a simple running number "algorithm" so session-jacking is very easy by just starting from a number and incrementing it until you are suddenly logged in.

The session ids don't expire after a set time, only on explicit logout. This means session-hijacking and Cross-Site Request Forgery attacks are very effective long after the user is done dealing with the site unless they explicitly log out.

The admin user and it's weak password is hardcoded in the app which means it is trivial for anyone with the tools to fuzz their way in as an admin.

Account registration accepts short or weak passwords. This means users of this application are more likely to have their account compromised which you directly are not responsible for but still isn't a good look for owner of the app.

#### How to fix

Fixing sessions: Remove the sessionid generator class Manager.java and revert back to default settings in the Application.java class by removing the method calls to sethttponly() and setManager() in the file. Create a application.properties file and setserver.servlet.session.timeout=60m to make the sessionIDs expire with timeout too.

Fixing passwords: Enforce reasonable password-length in the account registration form. Check passwords against a list of top10k most common passwords list to make sure they are not vulnerable.

Fixing admin: Remove the username "admin" and user proper role-based authentication system by creating a new table for them in the database. Consider loading admin credentials from a separate config file or manually inserting them into the database instead of having them in code.

Credential stuffing: Stop accepting login attemps for a while for an account after x amount of attempts. Enable logging for this kind of attack to see which accounts might be compromised so you are able to potentially freeze accounts and notify their users.

### FLAW 4

#### Insufficient logging and monitoring

Right now the application logs nothing and has no alert system. This means that attacks such as credential stuffing won't be noticed and can be run by attackers as long as they want with no way of noticing. Also normal bugs won't be seen either. So basically an attacker can keep trying with no risk of detection for as long as they want, which increases the chance of a breach happening a lot (guarantees it'll happen at some point). You also want to be able to see application errors immediately on production servers so you can respond to problems fast.

#### How to fix

Configure a proper logging implementation such as Log4j 2 to log all login requests, access control failures, server-side input validation failures and application errors. Set up a notification framework to send alerts to sysadmins on suspicious activity or other important errors, so they can be responded to in a timely manner. Make sure logs are in a standardized format and they are stored in a safe place for a long enough time so they can be analyzed easily and there will be a history of logs to review in case a breach happened earlier and the actual attack happens later.

### FLAW 5

#### Sensitive Data Exposure

The application is weak to man in the middle attacks because there is no https/ ssl encryption set up for the connections between the client and server. This means any login attempt by a user will leak their username and password to a snooping 3rd party.

Currently registered user's names and addresses are shown to all logged in users with no consent being asked, which violates the gdpr and is also completely unnecessary.

#### How to fix

Setup ssl in Spring by getting a certificate from Lets Encrypt and configure the server to use it by consulting the documentation. Redirect all http traffic to https in the app configuration.

Encrypt all personal data in the database so they won't be exposed in case of a database breach.

Filter the signup list view so only admins that need to see all participants can see them and others can only see (and delete) their own registrations. Easiest way to do this is probably to just get the list of signups from an account from the ORM and pass that to the model instead of passing all signups to the model.

Have a way to delete user data from their request.
