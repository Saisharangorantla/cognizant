

public class Logger {
	private static Logger instance;
    private Logger() {
    	
    }
    public static Logger getInstance() {
    	if(instance==null) {
    		return instance=new Logger();
    	}
    	return instance;
    }
    
}
