package com.example.datajarreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataJarReaderApplication implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(DataJarReaderApplication.class);

  FileSystem fileSystem;


  public static void main(String[] args) {
    SpringApplication.run(DataJarReaderApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Path path = getTestDataPath();
    List<String> data = loadTestData(path);
    log.info("data {}", data);
  }

  private Path getTestDataPath() throws URISyntaxException, IOException {

    Path path;

    URI uri = this.getClass().getResource("/data").toURI();
    log.info("uri: {}", uri);

    if (uri.getScheme().equals("jar")) {
      fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
      log.info("fileSystem: {}", fileSystem);
      path = fileSystem.getPath("/data");
    } else { // not in a jar, probably running in iDE
      path = Paths.get(uri);
    }

    return path;
  }

  private List<String> loadTestData(Path testDataDirectory) throws IOException {
    try (Stream<Path> path = Files.walk(testDataDirectory).sorted()) {
      return loadTrackData(path);
    }
  }

  private List<String> loadTrackData(Stream<Path> walk) throws IOException {

    List<String> data = new ArrayList<>();

    for (Iterator<Path> it = walk.iterator(); it.hasNext();) {

      Path path = it.next();
      
      if (path.getFileName().toString().endsWith(".txt")) {
        
        log.info("text file path: {}", path);
        
        List<String> dataFromFile = getData(path);
        data.addAll(dataFromFile);
        
      } else {
        
        log.info("non text file path: {}", path);

      }

    }
    
    return data;
  }
  
  private List<String> getData(Path path) throws IOException {

    List<String> data = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

      String line;
      
      while ((line = reader.readLine()) != null) {
        data.add(line);          
      }

    }
    return data;
  }
}
