package commands;
import engine.Database;

public interface Command {
    String execute(Database database);
}
