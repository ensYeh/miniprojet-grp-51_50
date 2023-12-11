// App.java
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
                .completer(new StringsCompleter("create", "mkdir", "find"))
                .build();

        Directory currentDir = new Directory(System.getProperty("user.dir"));
        int currentElement = 0;
        while (true) {
            System.out.println("Contenu du répertoire courant :");
            Afficheur.displayCurrentDir(currentDir);
            System.out.println("\nChemin complet depuis la racine du système de fichiers :");
            System.out.println(currentDir.obtenirCheminComplet());

            String line = reader.readLine("> ");
            String[] parts = line.split(" ");
            if (parts.length > 3){
                System.out.println("");
            }
            else if (parts.length == 1) {
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
            }
            else if (parts.length == 2) {
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
            }
            else{
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
                }
            }
        }
    }
}



/*

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("create", "mkdir", ))
                .build();

        Directory currentDir = new Directory(System.getProperty("user.dir"));
        int currentElement = 0;
        while (true) {
            CommandManager.checkNotesFile();
            System.out.println("Contenu du répertoire courant :");
            Afficheur.displayCurrentDir(currentDir);
            System.out.println("\nChemin complet depuis la racine du système de fichiers :");
            System.out.println(currentDir.obtenirCheminComplet());

            String line = reader.readLine("> ");
            String[] parts = line.split(" ");
            if (parts.length <= 3) {
                // Si la ligne contient au plus 3 parties (commande ou numéro + éventuels arguments)
                if (parts.length == 1) {
                    // Si la ligne contient uniquement un mot, vérifier si c'est un numéro ou une commande connue
                    Pattern numeroPattern = Pattern.compile("[1-9][0-9]*");
                    Pattern commandePattern = Pattern.compile("mkdir|cut|copy|past|\\.\\.|visu|find|-"); // Ajoutez d'autres commandes au besoin

                    Matcher numeroMatcher = numeroPattern.matcher(parts[0]);
                    Matcher commandeMatcher = commandePattern.matcher(parts[0]);
                    if (numeroMatcher.matches()) {
                        int number = Integer.parseInt(parts[0]);
                        if (currentDir.contentMap.containsKey(number)) {
                            currentElement = number;
                        } else {
                            System.out.println("Le numéro donné n'est associé à aucun element du répertoire");
                        }
                    }
                    if (line == null || line.equalsIgnoreCase("exit")) {
                        break;
                    }
                    reader.getHistory().add(line);

                    if (line.equalsIgnoreCase("create")) {
                        // Implémentez la logique de création de fichier/répertoire si nécessaire
                        System.out.println("Création de fichier/répertoire (TBD)");
                    } else if (line.startsWith("mkdir")) {
                        CommandManager.mkdir(currentDir, line);
                    } else {
                        System.out.println("Unknown command: " + line);
                    }
                }
            }
        }
    }
}/

*/
