import groovy.json.JsonSlurper
import org.apache.commons.lang.StringEscapeUtils

def server = (System.getenv('IRC_SERVER')) ? System.getenv('IRC_SERVER') : 'irc.atw-inter.net'
def channel = (System.getenv('IRC_CHANNEL')) ? System.getenv('IRC_CHANNEL') : '#ep-dev'

YammerBot bot = new YammerBot(channel)
bot.setVerbose(true)
bot.setEncoding("utf-8")
print "Connecting to ${server} ..."
bot.connect(server)
println ' connected'
bot.joinChannel(channel)
println "Joining ${channel}"

def token = (System.getenv('TOKEN')) ? System.getenv('TOKEN') : ''
def users = [:]
int id = getLatestMessageId(token);
while(true) {
    try {
        def responseAsText = new URL("https://www.yammer.com/api/v1/messages.json?newer_than=${id}&access_token=${token}").text
        def response = new JsonSlurper().parseText(responseAsText)
        if(response.messages.size() > 0) {
            bot.log("${response.messages.size()} new yammer messages")
            id = response.messages[0].id
            response.messages.reverse().each { message ->
                def name = getUser(message.sender_id, users, token).full_name
				def text = message.body.plain
				if(message.replied_to_id) {
					def opUrl = 'https://www.yammer.com/applifier.com/messages/' + message.replied_to_id
					def shortUrl = shortenUrl(opUrl)
					text += " (Reply to ${shortUrl})" 
				}
                bot.relayMessage(name, text, message.web_url)
            }
        } else {
            bot.log('No new yammer messages')
        }
        sleep(1000*60)
    } catch(Exception ex) {
        ex.printStackTrace()
    } 
}

def getLatestMessageId(String token) {
    def responseAsText = new URL("https://www.yammer.com/api/v1/messages.json?access_token=${token}").text
    def response = new JsonSlurper().parseText(responseAsText)
    return response.messages[0].id
}

def getUser(int id, def users, String token) {
    if(users[id]) {
        return users[id]
    } else {
        def user = loadUser(id, token)
        users[id] = user
        return user
    }
}

def loadUser(int id, String token) {
    def rsp = new URL("https://www.yammer.com/api/v1/users/${id}.json?access_token=${token}").text
    def slurper = new JsonSlurper()
    return slurper.parseText(rsp)
}

def shortenUrl(String longUrl) {
	try {
		return new URL("http://is.gd/api.php?longurl=${StringEscapeUtils.escapeHtml(longUrl)}").text 
	} catch(Exception e) {
		println e.message 
		return longUrl
	}
}
