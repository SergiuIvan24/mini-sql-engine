package commands;

import engine.Database;
import engine.Table;

import java.util.List;

public class InsertCommand implements Command {
    private String tableName;
    private List<String> values;
    private List<String> columns;

    public InsertCommand(String tableName, List<String> values, List<String> columns) {
        this.tableName = tableName;
        this.values = values;
        this.columns = columns;
    }

    @Override
    public String execute(Database database) {
        Table table = database.getOrCreateTable(tableName);
        table.Insert(values, columns);
        return "Inserted into " + tableName + ": " + values;
    }
}