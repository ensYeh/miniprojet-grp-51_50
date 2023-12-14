package fr.uvsq.cprog;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("create", "mkdir", "find", "+", "visu", "-", "."))
                .build();

        Directory currentDir = new Directory(System.getProperty("user.dir"));
        int currentElement = 0;
        while (true) {
            NoteManager.sortNotes();
            System.out.println("\nContenu du répertoire courant :");
            Afficheur.displayCurrentDir(currentDir);
            System.out.println("\nChemin complet depuis la racine du système de fichiers :");
            System.out.println(currentDir.getChemin());
            System.out.println("\nEntrer votre commande :");

            String line = reader.readLine("> ");
            String[] parts = line.split(" ");
            if (parts.length > 3) {
                Pattern numeroPattern = Pattern.compile("[1-9][0-9]*");
                Matcher numeroMatcher = numeroPattern.matcher(parts[0]);
                if (numeroMatcher.matches()) { // Cas où commande contient un NER
                    int number = Integer.parseInt(parts[0]);
                    if (currentDir.contentMap.containsKey(number)) {
                        currentElement = number;
                        System.out.println("Sélection de l'élément numéro " + currentElement);

                    } else {
                        System.out.println("Le numéro donné n'est associé à aucun élément du répertoire");
                    }
                }
                if (parts[1].equals("+")) {
                    String str = line.split("\\+")[1];
                    NoteManager.addNote(currentElement, str, currentDir.getChemin());
                } else {
                    System.out.println("Unknown command: " + line);
                }
            } else if (parts.length == 1) {
                Pattern numeroPattern = Pattern.compile("[1-9][0-9]*");
                Matcher numeroMatcher = numeroPattern.matcher(parts[0]);

                if (numeroMatcher.matches()) {
                    int number = Integer.parseInt(parts[0]);
                    if (currentDir.contentMap.containsKey(number)) {
                        currentElement = number;
                        System.out.println("Sélection de l'élément numéro " + currentElement);
                    } else {
                        System.out.println("Le numéro donné n'est associé à aucun élément du répertoire");
                    }
                } else {
                    // Traitement des commandes
                    switch (parts[0]) {
                        case "-":
                            NoteManager.deleteNoteIfExists(currentElement);
                            break;

                        case "exit":
                            return;

                        default:
                            System.out.println("Unknown command: " + line);
                            break;
                    }
                }
            } else if (parts.length == 2) {
                Pattern numeroPattern = Pattern.compile("[1-9][0-9]*");
                Matcher numeroMatcher = numeroPattern.matcher(parts[0]);

                if (numeroMatcher.matches()) { // Cas où commande contient un NER
                    int number = Integer.parseInt(parts[0]);
                    if (currentDir.contentMap.containsKey(number)) {
                        currentElement = number;
                        System.out.println("Sélection de l'élément numéro " + currentElement);
                    } else {
                        System.out.println("Le numéro donné n'est associé à aucun élément du répertoire");
                    }
                    // Traitement des commandes
                    switch (parts[1]) {
                        case "exit":
                            return;
                        default:
                            System.out.println("Unknown command: " + line);
                            break;

                        case "visu":
                            CommandManager.visu(currentDir, currentElement);
                            break;

                        case ".":
                            Path path = currentDir.contentMap.get(currentElement);
                            currentDir.moveTo(path);
                            NoteManager.checkNotesFile(path.toString());
                            break;

                        case "-":
                            NoteManager.deleteNoteIfExists(currentElement);
                            break;
                    }
                } else {
                    // Traitement des commandes
                    switch (parts[0]) {
                        case "create":
                            // Implémentez la logique de création de fichier/répertoire si nécessaire
                            System.out.println("Création de fichier/répertoire (TBD)");
                            break;
                        case "mkdir":
                            CommandManager.mkdir(currentDir, line);
                            currentDir = new Directory(System.getProperty("user.dir"));
                            Path path = Paths.get(currentDir.getChemin() + "/" + parts[1]);
                            Integer NER = currentDir.getKeyForValue(path);
                            NoteManager.incrementNote(NER);
                            break;
                        case "exit":
                            return;
                        case "find":
                            String fileNameToFind = parts[1];
                            Path currentDirPath = Paths.get(currentDir.getChemin());
                            CommandManager.find(currentDirPath, fileNameToFind);
                            break;
                        default:
                            System.out.println("Unknown command: " + line);
                            break;
                    }
                }
            } else {
                Pattern numeroPattern = Pattern.compile("[1-9][0-9]*");
                Matcher numeroMatcher = numeroPattern.matcher(parts[0]);

                if (numeroMatcher.matches()) { // Cas où commande contient un NER
                    int number = Integer.parseInt(parts[0]);
                    if (currentDir.contentMap.containsKey(number)) {
                        currentElement = number;
                        System.out.println("Sélection de l'élément numéro " + currentElement);
                    } else {
                        System.out.println("Le numéro donné n'est associé à aucun élément du répertoire");
                    }
                    // Traitement des commandes
                    switch (parts[1]) {
                        case "+":
                            String str = line.split("\\+")[1];
                            NoteManager.addNote(currentElement, str,currentDir.getChemin());
                            break;
                        case "exit":
                            return;
                        default:
                            System.out.println("Unknown command: " + line);
                            break;
                    }
                }
            }
        }
    }
}

