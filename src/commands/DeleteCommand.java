package commands;

import engine.Database;
import engine.Table;

public class DeleteCommand implements Command {
    private String tableName;
    private String condition;

    public DeleteCommand(String tableName, String column, String value) {
        this.tableName = tableName;
        this.condition = column + "=" + value;
    }

    @Override
    public String execute(Database database) {
        Table table = database.getOrCreateTable(tableName);
        table.delete(condition);
        return "Deleted rows from " + tableName + " where " + condition;
    }
}