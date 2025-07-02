import commands.Command;
import engine.Database;
import parser.SqlParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database database = new Database();
        while(true) {
            System.out.print("Enter SQL command: ");
            String query = scanner.nextLine();
            Command command = SqlParser.parse(query);
            String result = command.execute(database);
            System.out.println(result);
        }
    }
}