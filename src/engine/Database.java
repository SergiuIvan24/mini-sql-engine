package engine;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<String, Table> tables = new HashMap<>();

    public Table getOrCreateTable(String name) {
        return tables.computeIfAbsent(name, Table::new);
    }
    public void dropTable(String name) {
        tables.remove(name);
    }
    public void addTable(String name, Table table) {
        tables.put(name, table);
    }
}
