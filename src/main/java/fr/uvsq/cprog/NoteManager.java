package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
//import java.util.stream.Collectors;
import java.util.Collections;
//import java.util.stream.Stream;

public class NoteManager {

    public static void checkNotesFile() {
        String fileName = "notes.txt";
        Path filePath = Paths.get(fileName);

        try {
            // Vérifie si le fichier n'existe pas avant de le créer
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Le fichier \"" + fileName + "\" a été créé.");
            } else {
                System.out.println("Le fichier \"" + fileName + "\" existe déjà.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier \"" + fileName + "\": " + e.getMessage());
        }
    }

    public static void addNote(int number, String note) {
        try {
            String fileName = "notes.txt";
            Path filePath = Paths.get(fileName);

            List<String> lines = Files.readAllLines(filePath);

            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(number + " : ")) {
                    lines.set(i, number + " : " + line.substring((number + " : ").length()) + " " + note);
                    found = true;
                    break;
                }
            }

            if (!found) {
                lines.add(number + " : " + note);
                Collections.sort(lines);
            }

            Files.write(filePath, lines, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.err.println("Erreur lors de la modification du fichier \"notes.txt\": " + e.getMessage());
        }
    }

    public static void sortNotes() {
        try {
            String fileName = "notes.txt";
            Path filePath = Paths.get(fileName);

            List<String> lines = Files.readAllLines(filePath);

            Collections.sort(lines);

            Files.write(filePath, lines, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.err.println("Erreur lors du tri du fichier \"notes.txt\": " + e.getMessage());
        }
    }
}

    // public static void updateNote(int number, String note) {
    //     try {
    //         String fileName = "notes.txt";
    //         Path filePath = Paths.get(fileName);

    //         List<String> lines = Files.readAllLines(filePath);

    //         for (int i = 0; i < lines.size(); i++) {
    //             String line = lines.get(i);
    //             if (line.startsWith(number + " : ")) {
    //                 lines.set(i, number + " : " + note);
    //                 break;
    //             }
    //         }

//             Files.write(filePath, lines, StandardOpenOption.WRITE);
//         } catch (IOException e) {
//             System.err.println("Erreur lors de la modification du fichier \"notes.txt\": " + e.getMessage());
//         }
//     }
// }
