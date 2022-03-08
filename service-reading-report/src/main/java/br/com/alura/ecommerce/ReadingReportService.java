package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class ReadingReportService {

    private final KafkaDispatcher<User> orderKafkaDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) {
        var readingReportService = new ReadingReportService();
        try (var service = new KafkaService<>(
                ReadingReportService.class.getSimpleName(),
                "USER_GENERATE_READING_REPORT",
                readingReportService::parse,
                User.class,
                Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, User> record) {
        System.out.println("------------------------------------------");
        System.out.println("Processing report for user " + record.value());
    }
}
