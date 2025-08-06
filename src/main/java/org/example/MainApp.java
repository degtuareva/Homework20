package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

@SpringBootApplication
public class MainApp implements CommandLineRunner {

    @Autowired
    private ResultsProcessor resultsProcessor;

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length < 2) {
            System.err.println("Пожалуйста, передайте пути к файлам: ключи и ответы ученика");
            return;
        }

        String keyFile = args[0];
        String answersFile = args[1];

        try (InputStream keyStream = new FileInputStream(keyFile);
             InputStream answersStream = new FileInputStream(answersFile)) {

            Map<Integer, String> keyAnswers = resultsProcessor.loadAnswers(keyStream);
            Map<Integer, String> studentAnswers = resultsProcessor.loadAnswers(answersStream);

            int score = resultsProcessor.calculateScore(keyAnswers, studentAnswers);
            System.out.println("Суммарное количество баллов: " + score);
        }
    }
}