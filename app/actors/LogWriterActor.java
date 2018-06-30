package actors;

import akka.actor.UntypedAbstractActor;
import com.typesafe.config.Config;
import play.Logger;
import transformers.LogLineToPartialFileName;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Actor that receive a line of hn_logs.tsv and write it to the split partial file
 */
public class LogWriterActor extends UntypedAbstractActor {
    public static final String ACTOR_NAME = "log-writer-actor";
    private final LogLineToPartialFileName logLineToFileName;

    @Inject
    public LogWriterActor(Config config) {
        logLineToFileName = new LogLineToPartialFileName(config.getInt("split.log.hour.range"));
    }

    public static class Message {
        private String directory;
        private String line;
        public Message(String directory, String line) {
            this.directory = directory;
            this.line = line;
        }
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (!Message.class.isAssignableFrom(message.getClass())) {
            Logger.error("could not handle the message : {}", message);
            unhandled(message);
            return;
        }
        Message msg = Message.class.cast(message);
        writeLogToFile(msg.directory, msg.line);
    }

    private void writeLogToFile(String directory, String logLine) throws IOException {
        String[] array = logLine.split("\\t");
        String dateTime = array[0];
        String logFileName = logLineToFileName.apply(dateTime);
        File file = new File(directory, logFileName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.append(logLine);
            bw.newLine();
        }
    }
}
