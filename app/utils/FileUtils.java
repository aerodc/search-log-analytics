package utils;

import actors.LogWriterActor;
import akka.actor.ActorRef;
import play.Environment;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

@Singleton
public class FileUtils {

    private final ActorRef logWriterActor;
    private final Environment environment;

    @Inject
    public FileUtils(@Named(LogWriterActor.ACTOR_NAME) ActorRef logWriterActor,
                     Environment environment) {
        this.logWriterActor = logWriterActor;
        this.environment = environment;
    }

    public void decompressGzip(File input, File output) throws IOException {
        this.removeExistingFile(output);
        long start = Instant.now().getEpochSecond();
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(input))) {
            try (FileOutputStream out = new FileOutputStream(output)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }
        }
        long end = Instant.now().getEpochSecond();
        Logger.info("It took {} seconds to decompress {} to {}", end - start, input.getName(), output.getName());
    }

    public void splitFiles(String directory, File file) throws IOException {
        String subDir = file.getParent() + "/" + directory;
        deleteDirectory(subDir);
        createDirectory(subDir);
        FileReader fr = new FileReader(file);
        try (BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                logWriterActor.tell(new LogWriterActor.Message(subDir, line), ActorRef.noSender());
            }
        }
        fr.close();
    }

    private void createDirectory(String dirName) {
        File directory = new File(dirName);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private void removeExistingFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    private boolean deleteDirectory(String dirName) {
        File directoryToBeDeleted = new File(dirName);
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                file.delete();
            }
        }
        return directoryToBeDeleted.delete();
    }

    public List<String> listFileNames(String dirName) {
        return Arrays.stream(Objects.requireNonNull(environment.getFile(dirName).listFiles())).map(File::getName)
                .collect(Collectors.toList());
    }

    public long countLines(String file) throws IOException {
        Path path = environment.getFile(file).toPath();
        try (Stream<String> stringStream = Files.lines(path)) {
            return stringStream.count();
        }
    }

    public long countLinesBeginWith(String file, String beginWith) throws IOException {
        Path path = environment.getFile(file).toPath();
        try (Stream<String> stringStream = Files.lines(path)) {
            return stringStream.filter(line -> line.startsWith(beginWith))
                    .count();
        }
    }

    public List<String> getLines(String file) throws IOException {
        Path path = environment.getFile(file).toPath();
        try (Stream<String> stringStream = Files.lines(path)) {
            return stringStream.map(line -> line.split("\\t")[1]).collect(Collectors.toList());
        }
    }

    public List<String> getLinesBeginWith(String file, String beginWith) throws IOException {
        Path path = environment.getFile(file).toPath();
        try (Stream<String> stringStream = Files.lines(path)) {
            return stringStream.filter(line -> line.startsWith(beginWith))
                    .map(line -> line.split("\\t")[1]).collect(Collectors.toList());
        }
    }
}
