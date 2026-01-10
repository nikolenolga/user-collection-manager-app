package ru.aston.finalproject.service.writer;

import lombok.NonNull;
import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.parser.Parsing;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static ru.aston.finalproject.util.ConstantMethods.checkedStringOnEmpty;
import static ru.aston.finalproject.util.Message.FAILED_TO_CREATE_FILE_X;

public class FileWriter<T> {

    private static final int FLUSH_THRESHOLD = 1000;
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @NonNull
    private final Parsing<T> parser;

    public FileWriter(@NonNull Parsing<T> parser) {
        this.parser = parser;
    }

    public void write(@NonNull List<T> items, String filePath) {
        validateInput(items, filePath);

        if (items.isEmpty()) {
            ensureDirectoryExists(filePath);
            return;
        }

        try {
            writeItemsToFile(items, filePath);
        } catch (IOException e) {
            throw new AppException(
                    String.format("Failed to write %d items to file '%s'", items.size(), filePath)
            );
        }
    }

    private void validateInput(List<T> items, String filePath) {
        Objects.requireNonNull(items, "Items list cannot be null");
        checkedStringOnEmpty(filePath, "filePath");
    }

    private void ensureDirectoryExists(String filePath) {
        Path path = Paths.get(filePath);
        Path parent = path.getParent();
        if (parent != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new AppException(
                        String.format(FAILED_TO_CREATE_FILE_X, filePath)
                );
            }
        }
    }

    private void writeItemsToFile(List<T> items, String filePath) throws IOException {
        try (BufferedWriter writer = createWriter(filePath)) {
            writeFormattedItems(items, writer);
        }
    }

    private BufferedWriter createWriter(String filePath) throws IOException {
        return new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(filePath, true),
                        StandardCharsets.UTF_8
                )
        );
    }

    private void writeFormattedItems(List<T> items, BufferedWriter writer) throws IOException {
        for (int i = 0; i < items.size(); i++) {
            writeFormattedLine(writer, items.get(i));
            flushIfNeeded(writer, i + 1);
        }
        writer.flush();
    }

    private void writeFormattedLine(BufferedWriter writer, T item)
            throws IOException {
        String formattedLine = formatLine(item);
        writer.write(formattedLine);
    }

    private String formatLine(T item) {
        return parser.parseToString(item) + LINE_SEPARATOR;
    }

    private void flushIfNeeded(BufferedWriter writer, int currentLine) throws IOException {
        if (currentLine % FLUSH_THRESHOLD == 0) {
            writer.flush();
        }
    }
}