package com.example.datajarreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@SpringBootApplication
public class DataJarReaderApplication implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(DataJarReaderApplication.class);

  FileSystem fileSystem;


  public static void main(String[] args) {
    SpringApplication.run(DataJarReaderApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    ClassLoader cl = this.getClass().getClassLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] resources = resolver.getResources("classpath:/data/**/*.txt");
    for (Resource resource : resources) {
      loadTestData(resource);
    }

  }

  private void loadTestData(Resource resource) throws IOException {

    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

      String line;

      while ((line = reader.readLine()) != null) {
        log.info(line);
      }
    }
  }
}
