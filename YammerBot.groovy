import org.jibble.pircbot.*

public class YammerBot extends PircBot {
    
    public YammerBot() {
        this.setName('YammerBot')
        this.setLogin('YammerBot')
        this.setVersion('YammerBot 0.0.1')
        this.setAutoNickChange(true)
    }
    
    public void relayMessage(String sender, String msg) {
        def message = "<yammer|${sender}> ${msg}"
        this.log("Relaying: ${message}")
        this.sendMessage('#ep-dev', message)
    }
    
}