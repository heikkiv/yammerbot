import groovy.json.JsonSlurper

YammerBot bot = new YammerBot()
bot.setVerbose(true)
bot.setEncoding("utf-8")
print 'Connecting to irc1.inet.fi ...'
bot.connect("irc1.inet.fi")
println ' connected'
bot.joinChannel("#ep-dev")
println 'Joining #ep-dev'

def token = new File('token.txt').text
def users = [:]
int id = getLatestMessageId();
while(true) {
    try {
        def responseAsText = new URL("https://www.yammer.com/api/v1/messages.json?newer_than=${id}&access_token=${token}").text
        def response = new JsonSlurper().parseText(responseAsText)
        if(response.messages.size() > 0) {
            bot.log("${response.messages.size()} new yammer messages")
            id = response.messages[0].id
            response.messages.each { message ->
                def name = getUser(message.sender_id, users).full_name
                bot.relayMessage(name, message.body.plain)
            }
        } else {
            bot.log('No new yammer messages')
        }
        sleep(1000*60)
    } catch(Exception ex) {
        ex.printStackTrace()
    } 
}

def getLatestMessageId() {
    def responseAsText = new URL("https://www.yammer.com/api/v1/messages.json?access_token=${token}").text
    def response = new JsonSlurper().parseText(responseAsText)
    return response.messages[0].id
}

def getUser(int id, users) {
    if(users[id]) {
        return users[id]
    } else {
        def user = loadUser(id)
        users[id] = user
        return user
    }
}

def loadUser(int id) {
    def rsp = new URL("https://www.yammer.com/api/v1/users/${id}.json?access_token=MfIXH1kmM7htvLYvRt7uQ").text
    def slurper = new JsonSlurper()
    return slurper.parseText(rsp)
}
