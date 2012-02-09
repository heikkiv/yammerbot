import org.jibble.pircbot.*

public class YammerBot extends PircBot {
    
    public YammerBot() {
        this.setName('YammerBot')
        this.setLogin('YammerBot')
        this.setVersion('YammerBot 0.0.2')
        this.setAutoNickChange(true)
    }
    
    public void relayMessage(String sender, String msg, String url) {
        def message = "<yammer|${sender}> ${msg}"
        if(message.size() > 300) {
            message = message.substring(0, 300) + '... ' + shortenUrl(url)
        }
        this.log("Relaying: ${message}")
        this.sendMessage('#ep-dev', message)
    }
    
    def shortenUrl(String url) {
        def shortUrl = new URL("http://is.gd/create.php?format=simple&url=${url}").text
        return shortUrl
    }
    
}