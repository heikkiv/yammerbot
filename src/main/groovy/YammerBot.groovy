import org.jibble.pircbot.*

public class YammerBot extends PircBot {
    
    private String channel = ''
	
    public YammerBot(String channel) {
		this.channel = channel
        this.setName('YammerBot')
        this.setLogin('YammerBot')
        this.setVersion('YammerBot 0.0.3')
        this.setAutoNickChange(true)
    }
    
    public void relayMessage(String sender, String msg, String url) {
        def message = "<yammer|${sender}> ${msg}"
        if(message.size() > 300) {
            message = message.substring(0, 300) + '... ' + shortenUrl(url)
        }
        this.log("Relaying: ${message}")
        this.sendMessage(channel, message)
        this.onMessage(channel, 'YammerBot', '', '', message)
    }
    
    def shortenUrl(String url) {
        def shortUrl = new URL("http://is.gd/create.php?format=simple&url=${url}").text
        return shortUrl
    }
	
	public void onDisconnect() {
		println "Disconnected from irc server"
		while (!isConnected()) {
		    try {
				println "Trying to reconnect"
		        reconnect();
				joinChannel(channel);
		    }
		    catch (Exception e) {
		        sleep(60000)
		    }
		}
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		def data = [:]
		data.put('channel', channel)
		data.put('sender', sender)
		data.put('login', login)
		data.put('hostname', hostname)
		data.put('message', message)
	}
    
}
