package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultsProcessorTest {

    private ResultsProcessor processor;

    @BeforeEach
    void setup() {
        processor = new ResultsProcessor();
        // Вручную настройка значений из properties для теста
        ReflectionTestUtils.setField(processor, "pointsGroup1", 1);
        ReflectionTestUtils.setField(processor, "pointsGroup2", 2);
        ReflectionTestUtils.setField(processor, "pointsGroup3", 3);
    }

    @Test
    void testLoadAnswers() throws Exception {
        String data = "1 - А\n2 - Б\n3 - В\n10 - А";
        Map<Integer, String> answers = processor.loadAnswers(new ByteArrayInputStream(data.getBytes()));

        assertEquals(4, answers.size());
        assertEquals("А", answers.get(1));
        assertEquals("А", answers.get(10));
    }

    @Test
    void testCalculateScore() {
        Map<Integer, String> key = Map.of(
                1, "А", 2, "Б", 3, "В", 4, "Г",
                5, "А", 6, "Б", 7, "В", 8, "Г",
                9, "А", 10, "Б"
        );
        Map<Integer, String> student = Map.of(
                1, "А", 2, "Б", 3, "В", 4, "Г",
                5, "А", 6, "Б", 7, "Ошибка", 8, "Г",
                9, "А", 10, "Ошибка"
        );

        int score = processor.calculateScore(key, student);
        // Задания 1-4 (4 балла) + 5-6,8 (3 задания по 2 балла = 6) + 9 (3 балла)
        assertEquals(4 * 1 + 3 * 2 + 3, score);
    }
}