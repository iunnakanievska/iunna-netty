iunna-netty
===========

==========
Installing
==========

mvn install command should be executed to create jar.

=====
Usage
=====
App gives port number as parameter. If port hasn't been specified - default 8080 port will be used.

One of the following commands may be used:
	hello - to get greetings message
	redirect?url=<url> - for redirection onto address specified in request "url" option
	status - to obtain the statistic
	unexpected request causes help page
	
=====
Notes
=====
Data aren't persisted, so willn't be available after application was closed.
