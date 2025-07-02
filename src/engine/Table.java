package engine;

import java.util.ArrayList;
import java.util.List;
import storage.JsonStorage;

public class Table {
    private final String name;
    private final List<Row> rows = new ArrayList<>();
    private final List<String> columns = new ArrayList<>();

    public Table(String name) {
        this.name = name;
        load();
    }

    public Table(String name, String[] columnNames) {
        this.name = name;
        for (String column : columnNames) {
            columns.add(column);
        }
        save();
    }

    public void Insert(List<String> values, List<String> columns) {
        Row row = new Row();
        List<String> columnsToUse = columns != null ? columns : this.columns;

        for (int i = 0; i < values.size() && i < columnsToUse.size(); i++) {
            row.put(columnsToUse.get(i), values.get(i));
        }
        rows.add(row);
        save();
    }

    public String getName() {
        return name;
    }

    public List<String> getColumns() {
        return new ArrayList<>(columns);
    }

    public void load() {
        List<Row> loadedRows = JsonStorage.loadTable(name);
        rows.clear();
        rows.addAll(loadedRows);

        if (!rows.isEmpty()) {
            columns.clear();
            columns.addAll(rows.get(0).keySet());
        }
    }

    private void save() {
        JsonStorage.saveTable(name, rows);
    }

    public List<Row> select(String condition) {
        if (condition == null || condition.isEmpty()) {
            return new ArrayList<>(rows);
        }

        String[] parts = condition.split("=");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Conditie invalida: " + condition);
        }
        String column = parts[0].trim();
        String value = parts[1].trim();
        List<Row> result = new ArrayList<>();
        for (Row row : rows) {
            if (value.equals(row.get(column))) {
                result.add(row);
            }
        }
        return result;
    }

    public void delete(String condition) {
        if (condition == null || condition.isEmpty()) {
            rows.clear();
            save();
            return;
        }

        String[] parts = condition.split("=");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Conditie invalida: " + condition);
        }
        String column = parts[0].trim();
        String value = parts[1].trim();
        rows.removeIf(row -> value.equals(row.get(column)));
        save();
    }
}