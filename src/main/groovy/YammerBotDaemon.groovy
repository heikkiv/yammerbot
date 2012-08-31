import groovy.json.JsonSlurper

YammerBot bot = new YammerBot()
bot.setVerbose(true)
bot.setEncoding("utf-8")
def server = (System.getenv('IRC_SERVER')) ? System.getenv('IRC_SERVER') : 'irc.atw-inter.net'
def channel = (System.getenv('IRC_CHANNEL')) ? System.getenv('IRC_CHANNEL') : '#ep-dev'
print "Connecting to ${server} ..."
bot.connect(server)
println ' connected'
bot.joinChannel(channel)
println 'Joining ${channel}'

def token = (System.getenv('TOKEN')) ? System.getenv('TOKEN') : new File('token.txt').text
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
                bot.relayMessage(name, message.body.plain, message.web_url)
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
