import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/3115:44
 */
public class JULTest {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("JULTest");
        logger.info("info");
        logger.warning("warning");
        logger.finest("finest");
        LogManager logManager = LogManager.getLogManager();
        logManager.addLogger(logger);
    }
}
