package fr.uvsq.cprog;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
//import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("press", "copy", "past", "cut", "mkdir", "find", "+", "visu", "-", ".",
                        ".."))
                .build();

        Directory currentDir = new Directory(System.getProperty("user.dir"));
        int currentElement = 0;
        String copyCut = null;
        while (true) {
            NoteManager.sortNotes();
            System.out.println("\nContenu du répertoire courant :");
            Afficheur.displayCurrentDir(currentDir);
            System.out.println("\nChemin complet depuis la racine du système de fichiers :");
            System.out.println(currentDir.getChemin());
            System.out.println("Le NER courant est : " + currentElement);
            System.out.println("\nEntrer votre commande :");

            String line = reader.readLine("> ");
            String[] parts = line.split(" ");
            if (parts.length >= 3) {
                Pattern numeroPattern = Pattern.compile("[1-9][0-9]*");
                Matcher numeroMatcher = numeroPattern.matcher(parts[0]);
                if (numeroMatcher.matches()) { // Cas où commande contient un NER
                    int number = Integer.parseInt(parts[0]);
                    if (currentDir.contentMap.containsKey(number)) {
                        currentElement = number;
                        // System.out.println("Sélection de l'élément numéro " + currentElement);

                    } else {
                        System.out.println("Le numéro donné n'est associé à aucun élément du répertoire");
                    }
                }
                if (parts[1].equals("+")) {
                    String str = line.split("\\+")[1];
                    if (NoteManager.checkNotesFile(currentDir.getChemin())) {
                        // System.out.println("ici c'est pas normal non plus");
                        currentDir.contentMap = currentDir.directoryMap();
                        Path path = Paths.get(currentDir.getChemin() + "/" + "notes.json");
                        Integer NER = currentDir.getKeyForValue(path);
                        if (NER <= currentElement) {
                            NoteManager.addNote(currentElement + 1, str, currentDir);
                        } else {
                            NoteManager.addNote(currentElement, str, currentDir);
                        }
                    } else {

                        NoteManager.addNote(currentElement, str, currentDir);
                    }
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
                        // System.out.println("Sélection de l'élément numéro " + currentElement);
                    } else {
                        System.out.println("Le numéro donné n'est associé à aucun élément du répertoire");
                    }
                } else {
                    // Traitement des commandes
                    switch (parts[0]) {

                        case "h":
                            for (Map.Entry<Integer, Path> entry : currentDir.contentMap.entrySet()) {
                                // Récupérer la clé et la valeur de l'entrée
                                int key = entry.getKey();
                                Path value = entry.getValue();

                                // Afficher la clé et la valeur
                                System.out.println("Clé : " + key + ", Valeur : " + value);
                            }
                            ;
                            break;

                        case "past":
                            CommandManager.past(currentDir,copyCut);
                            CommandManager.pressePapier.clear();
                            break;

                        case "..":
                            currentDir.moveTo(Paths.get(currentDir.getChemin()).getParent());
                            break;

                        case "-":
                            NoteManager.deleteNoteIfExists(currentElement, currentDir.getChemin());
                            if (NoteManager.checkAndDeleteEmptyNotesFile(currentDir.getChemin())) {
                                // System.out.println("true");
                                currentDir.contentMap = currentDir.directoryMap();
                            }
                            break;

                        case ".":
                            Path path = currentDir.contentMap.get(currentElement);
                            currentDir.moveTo(path);

                            break;

                        case "visu":
                            CommandManager.visu(currentDir, currentElement);
                            break;

                        case "cut":
                            copyCut = "cut";
                            CommandManager.copyCut(currentDir, currentElement);
                            break;
                        case "copy":
                            copyCut = "copy";
                            CommandManager.copyCut(currentDir, currentElement);
                            break;
                        case "exit":
                            return;

                        case "press":
                            CommandManager.afficherPressePapier();
                            break;

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
                        // System.out.println("Sélection de l'élément numéro " + currentElement);
                    } else {
                        System.out.println("Le numéro donné n'est associé à aucun élément du répertoire");
                    }
                    // Traitement des commandes
                    switch (parts[1]) {

                        default:
                            System.out.println("Unknown command: " + line);
                            break;

                        case "visu":
                            CommandManager.visu(currentDir, currentElement);
                            break;

                        case "cut":
                            copyCut = "cut";
                            CommandManager.copyCut(currentDir, currentElement);
                            break;
                        case "copy":
                            copyCut = "copy";
                            CommandManager.copyCut(currentDir, currentElement);
                            break;

                        case ".":
                            Path path = currentDir.contentMap.get(currentElement);
                            currentDir.moveTo(path);

                            break;

                        case "..":
                            currentDir.moveTo(Paths.get(currentDir.getChemin()).getParent());

                            break;

                        case "-":
                            NoteManager.deleteNoteIfExists(currentElement, currentDir.getChemin());
                            if (NoteManager.checkAndDeleteEmptyNotesFile(currentDir.getChemin())) {
                                // System.out.println("true");
                                currentDir.contentMap = currentDir.directoryMap();
                            }
                            break;
                    }
                } else {
                    // Traitement des commandes
                    switch (parts[0]) {
                        case "mkdir":
                            CommandManager.mkdir(currentDir, line);
                            currentDir.contentMap = currentDir.directoryMap();
                            Path path = Paths.get(currentDir.getChemin() + "/" + parts[1]);
                            Integer NER = currentDir.getKeyForValue(path);
                            NoteManager.modifyNoteNumber(NER, currentDir.getChemin(), "+");
                            break;

                        case "find":
                            String fileNameToFind = parts[1];
                            Path currentDirPath = Paths.get(currentDir.getChemin());
                            CommandManager.find(currentDirPath, fileNameToFind);
                            break;

                        case "+":
                            String str = line.split("\\+")[1];
                            if (NoteManager.checkNotesFile(currentDir.getChemin())) {

                                currentDir.contentMap = currentDir.directoryMap();
                                path = Paths.get(currentDir.getChemin() + "/" + "notes.json");
                                NER = currentDir.getKeyForValue(path);
                                if (NER <= currentElement) {
                                    NoteManager.addNote(currentElement + 1, str, currentDir);
                                } else {
                                    NoteManager.addNote(currentElement, str, currentDir);
                                }
                            } else {
                                NoteManager.addNote(currentElement, str, currentDir);
                            }
                            break;

                        default:
                            System.out.println("Unknown command: " + line);
                            break;
                    }
                }
            }
        }
    }
}
