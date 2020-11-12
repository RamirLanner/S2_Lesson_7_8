package MyProfLogger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyProfLogger {
    private final Logger log;

    public MyProfLogger(String name) {
        //log = LogManager.getLogger(name);
        log = LogManager.getLogger(name);
    }

    public void addMessageInfo(String msg){
        log.info(msg);
    }

    public void addMessageError(String msg){
        log.error(msg);

    }
}
