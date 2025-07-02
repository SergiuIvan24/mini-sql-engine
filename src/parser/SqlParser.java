package parser;

import commands.*;
import java.util.List;

public class SqlParser {

    public static Command parse(String query) {
        query = query.trim();

        if (query.toLowerCase().startsWith("create table")) {
            return parseCreateTable(query);
        } else if (query.toLowerCase().startsWith("insert into")) {
            return parseInsert(query);
        } else if (query.toLowerCase().startsWith("select")) {
            return parseSelect(query);
        } else if (query.toLowerCase().startsWith("delete from")) {
            return parseDelete(query);
        } else {
            throw new IllegalArgumentException("Unknown command: " + query);
        }
    }

    private static CreateTableCommand parseCreateTable(String query) {
        int idxOpen = query.indexOf('(');
        int idxClose = query.indexOf(')');

        if (idxOpen == -1 || idxClose == -1 || idxClose < idxOpen) {
            throw new IllegalArgumentException("Invalid CREATE TABLE syntax");
        }

        String before = query.substring(0, idxOpen).trim();
        String columnsPart = query.substring(idxOpen + 1, idxClose).trim();

        String[] parts = before.split("\\s+");
        if (parts.length < 3) throw new IllegalArgumentException("Missing table name");

        String tableName = parts[2];
        String[] columns = columnsPart.split(",");

        for (int i = 0; i < columns.length; i++) {
            columns[i] = columns[i].trim();
        }

        return new CreateTableCommand(tableName, columns);
    }

    private static InsertCommand parseInsert(String query) {
        int idxValues = query.toLowerCase().indexOf("values");
        if (idxValues == -1) throw new IllegalArgumentException("Missing VALUES");

        String before = query.substring(0, idxValues).trim();
        String valuesPart = query.substring(idxValues + 6).trim();

        if (!valuesPart.startsWith("(") || !valuesPart.endsWith(")")) {
            throw new IllegalArgumentException("VALUES must be inside parentheses");
        }

        valuesPart = valuesPart.substring(1, valuesPart.length() - 1);
        String[] valuesArray = valuesPart.split(",");
        for (int i = 0; i < valuesArray.length; i++) {
            valuesArray[i] = valuesArray[i].trim().replaceAll("'", "");
        }

        String[] parts = before.split("\\s+");
        if (parts.length < 3) throw new IllegalArgumentException("Invalid INSERT INTO syntax");

        String tableName = parts[2];

        return new InsertCommand(tableName, List.of(valuesArray), null);
    }

    private static SelectCommand parseSelect(String query) {
        String[] parts = query.trim().split("\\s+");
        if (parts.length != 4 || !parts[1].equals("*") || !parts[2].equalsIgnoreCase("from")) {
            throw new IllegalArgumentException("Invalid SELECT syntax");
        }

        String tableName = parts[3];
        return new SelectCommand(tableName);
    }

    private static DeleteCommand parseDelete(String query) {
        query = query.trim();
        String[] parts = query.split("\\s+");

        if (parts.length < 4 || !parts[0].equalsIgnoreCase("delete") || !parts[1].equalsIgnoreCase("from")) {
            throw new IllegalArgumentException("Invalid DELETE FROM syntax");
        }

        String tableName = parts[2];

        if (!query.toLowerCase().contains("where")) {
            throw new IllegalArgumentException("DELETE must contain WHERE");
        }

        int whereIdx = query.toLowerCase().indexOf("where");
        String condition = query.substring(whereIdx + 5).trim();

        String[] condParts = condition.split("=");
        if (condParts.length != 2) throw new IllegalArgumentException("Invalid WHERE clause");

        String column = condParts[0].trim();
        String value = condParts[1].trim().replaceAll("'", "");

        return new DeleteCommand(tableName, column, value);
    }
}