import com.treasure_data.logger.TreasureDataLogger;

public class TdLogger {
	
	private static TreasureDataLogger LOG;
	
  	static {
	    try {
	      Properties props = System.getProperties();
	      props.load(TdLogger.class.getClassLoader().getResourceAsStream("treasure-data.properties"));
	      LOG = TreasureDataLogger.getLogger("messages");
	    } catch (IOException e) {
	      println e.message
	    }
	}
	
	public static void log(def data) {
		LOG.log('messages', data)
	}
	
}