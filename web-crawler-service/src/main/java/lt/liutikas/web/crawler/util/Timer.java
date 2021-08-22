package lt.liutikas.web.crawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class Timer {

    private static final Logger LOG = LoggerFactory.getLogger(Timer.class);

    private Instant start;

    public void start() {
        start = Instant.now();
    }

    public long elapsedMilli() {
        Instant now = Instant.now();

        return now.toEpochMilli() - start.toEpochMilli();
    }

}
