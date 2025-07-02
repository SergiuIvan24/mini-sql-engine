package commands;

import engine.Database;
import engine.Row;
import engine.Table;

import java.util.List;

public class SelectCommand implements Command {
    private String tableName;
    private String condition;

    public SelectCommand(String tableName) {
        this.tableName = tableName;
        this.condition = null; // Pentru SELECT * FROM table
    }

    @Override
    public String execute(Database database) {
        Table table = database.getOrCreateTable(tableName);
        List<Row> results = table.select(condition);
        return "Selected from " + tableName + ": " + results;
    }
}