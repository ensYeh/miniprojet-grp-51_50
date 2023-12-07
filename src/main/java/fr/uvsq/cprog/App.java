package fr.uvsq.cprog;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App {
    public static boolean marche = true;

    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("create"))
                .build();

        while (true) {
            System.out.println(getDirectoryHierarchy());
            String line = reader.readLine("> ");
            if (line == null || line.equalsIgnoreCase("exit")) {
                break;
            }
            reader.getHistory().add(line);

            if (line.equalsIgnoreCase("create")) {
                // System.out.println(“TBD”);
            } else {
                System.out.println("Unknown command: " + line);
            }
        }
    }

    public static String getDirectoryHierarchy() {
        return "Current absolute path is : " + System.getProperty("user.dir");
    }
}