package storage;

import engine.Row;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonStorage {
    private static final String PATH = "data/";

    static {
        new File(PATH).mkdirs();
    }

    public static List<Row> loadTable(String name) {
        File file = new File(PATH + name + ".json");
        if (!file.exists()) return new ArrayList<>();

        try {
            String json = Files.readString(file.toPath());
            Gson gson = new Gson();
            Row[] array = gson.fromJson(json, Row[].class);
            return new ArrayList<>(List.of(array));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveTable(String name, List<Row> rows) {
        File file = new File(PATH + name + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(rows, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}