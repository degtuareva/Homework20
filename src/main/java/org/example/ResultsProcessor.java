package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component("resultsProcessor")
public class ResultsProcessor {

    @Value("${ege.points.group1}")
    private int pointsGroup1; // задания 1-4

    @Value("${ege.points.group2}")
    private int pointsGroup2; // задания 5-8

    @Value("${ege.points.group3}")
    private int pointsGroup3; // задания 9-10

    /**
     * Парсит файл с ответами в Map: номер задания -> ответ
     */
    public Map<Integer, String> loadAnswers(InputStream inputStream) throws IOException {
        Map<Integer, String> answers = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Формат строки: "1 - А" (с пробелами вокруг дефиса)
                String[] parts = line.split("-");
                if (parts.length != 2) continue;
                int questionNumber = Integer.parseInt(parts[0].trim());
                String answer = parts[1].trim();
                answers.put(questionNumber, answer);
            }
        }
        return answers;
    }

    /**
     * Вычисляет суммарные баллы, сравнивая ответы студента и ключ
     */
    public int calculateScore(Map<Integer, String> answerKey, Map<Integer, String> studentAnswers) {
        int totalScore = 0;

        for (Map.Entry<Integer, String> entry : answerKey.entrySet()) {
            Integer questionNumber = entry.getKey();
            String correctAnswer = entry.getValue();
            String studentAnswer = studentAnswers.get(questionNumber);
            if (studentAnswer != null && studentAnswer.equalsIgnoreCase(correctAnswer)) {
                totalScore += pointsForQuestion(questionNumber);
            }
        }
        return totalScore;
    }

    /**
     * Возвращает баллы за конкретное задание по его номеру и весам из конфигурации
     */
    private int pointsForQuestion(int questionNumber) {
        if (questionNumber >= 1 && questionNumber <= 4) {
            return pointsGroup1;
        } else if (questionNumber >= 5 && questionNumber <= 8) {
            return pointsGroup2;
        } else if (questionNumber >= 9 && questionNumber <= 10) {
            return pointsGroup3;
        }
        return 0;
    }
}