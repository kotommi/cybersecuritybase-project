LINK: https://github.com/kotommi/cyber-security-base-19
Should work with tmcbeans (untested) or by installing maven and running
```
mvn spring-boot:run
```
in project root.

FLAW 1:

### Cross-site scripting (XSS)
The application has a stored XSS vulnerability in the event registration system. When viewing all signups for the event, unescaped text is inserted into the page. A malicious user could sign up with a name or address that contains script tags enclosing harmful javascript and that script would run on any users browser that views the page. 
### How to fix
Replace all the th:utext tags in list.html with th:text. This way Thymeleaf treats user inputted data as text (by escaping it) instead of as html. Additional validation could be done in the SignupController-class' method submitForm() to discard unwanted entries.

FLAW 2:
<description of flaw 2>
<how to fix it>

...

FLAW 5:
<description of flaw 5>
<how to fix it>