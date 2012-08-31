# YammerBot - IRC bot for yammer

YammerBot is a simple IRC for piping your [yammer](http://yammer.com) messages to irc. It's written in Groovy using [PircBot](http://www.jibble.org/pircbot.php) and meant to run on heroku.

## Configuration

### IRC

Set the `IRC_SERVER` and `IRC_CHANNEL` environment variables.

### Yammer

Create a yammer application and do the oauth dance.

Set your yammer application oauth token to the `TOKEN` environment variable.
