package fr.uvsq.cprog;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("create", "mkdir", "find", "+"))
                .build();

        Directory currentDir = new Directory(System.getProperty("user.dir"));
        int currentElement = 0;
        while (true) {
            NoteManager.checkNotesFile();
            NoteManager.sortNotes();
            System.out.println("Contenu du répertoire courant :");
            Afficheur.displayCurrentDir(currentDir);
            System.out.println("\nChemin complet depuis la racine du système de fichiers :");
            System.out.println(currentDir.obtenirCheminComplet());

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
                    NoteManager.addNote(currentElement, str);
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
                        case "create":
                            // Implémentez la logique de création de fichier/répertoire si nécessaire
                            System.out.println("Création de fichier/répertoire (TBD)");
                            break;
                        case "mkdir":
                            CommandManager.mkdir(currentDir, line);
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
                        case "create":
                            // Implémentez la logique de création de fichier/répertoire si nécessaire
                            System.out.println("Création de fichier/répertoire (TBD)");
                            break;
                        case "mkdir":
                            CommandManager.mkdir(currentDir, line);
                            break;

                        case "exit":
                            return;
                        default:
                            System.out.println("Unknown command: " + line);
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
                            break;
                        case "exit":
                            return;
                        case "find":
                            CommandManager.find(currentDir.contentMap.get(currentElement));
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
                        case "create":
                            // Implémentez la logique de création de fichier/répertoire si nécessaire
                            System.out.println("Création de fichier/répertoire (TBD)");
                            break;
                        case "mkdir":
                            CommandManager.mkdir(currentDir, line);
                            break;
                        case "+":
                            String str = line.split("\\+")[1];

                            NoteManager.addNote(currentElement, str);
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













// import org.jline.reader.LineReader;
// import org.jline.reader.LineReaderBuilder;
// import org.jline.reader.impl.completer.StringsCompleter;
// import org.jline.terminal.Terminal;
// import org.jline.terminal.TerminalBuilder;

// import java.io.IOException;

// public class App {
//     private static Terminal terminal;
//     private static LineReader reader;
//     private static Directory currentDir;
//     private static int currentElement;

//     public static void main(String[] args) throws IOException {
//         initialize();

//         while (true) {
//             displayCurrentState();
//             String line = reader.readLine("> ");
//             processCommand(line);
//         }
//     }

//     private static void initialize() throws IOException {
//         terminal = TerminalBuilder.terminal();
//         reader = LineReaderBuilder.builder()
//                 .terminal(terminal)
//                 .completer(new StringsCompleter("create", "mkdir", "find", "+"))
//                 .build();
//         currentDir = new Directory(System.getProperty("user.dir"));
//         currentElement = 0;
//         NoteManager.checkNotesFile();
//     }

//     private static void displayCurrentState() throws IOException {
//         System.out.println("Contenu du répertoire courant :");
//         Afficheur.displayCurrentDir(currentDir);
//         System.out.println("\nChemin complet depuis la racine du système de fichiers :");
//         System.out.println(currentDir.obtenirCheminComplet());
//     }

//     private static void processCommand(String line) throws IOException {
//         String[] parts = line.split(" ");
//         CommandProcessor commandProcessor = new CommandProcessor(currentDir, currentElement, line, parts);
//         commandProcessor.process();
//     }
// }