package commands;

import engine.Database;
import engine.Table;

public class CreateTableCommand implements Command {
    private String tableName;
    private String[] columns;

    public CreateTableCommand(String tableName, String[] columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public String execute(Database database) {
        Table table = new Table(tableName, columns);
        database.addTable(tableName, table);
        return "Created table " + tableName + " with columns: " + String.join(", ", columns);
    }
}