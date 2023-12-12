package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Afficheur {

    public static void displayCurrentDir(Directory directory) throws IOException {
        String fileName = "notes.txt";
        Path filePath = Paths.get(fileName);

        // Lire les lignes du fichier notes.txt
        List<String> lines = Files.readAllLines(filePath);

        // Créer une map pour associer les numéros de fichier aux notes
        Map<Integer, String> notesMap = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(" : ", 2);
            if (parts.length == 2) {
                try {
                    int fileNumber = Integer.parseInt(parts[0].trim());
                    notesMap.put(fileNumber, parts[1].trim());
                } catch (NumberFormatException e) {
                    // Gérer l'erreur si le numéro de fichier n'est pas un entier
                    System.out.println("Erreur de format dans le fichier notes.txt");
                }
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory.obtenirCheminComplet()))) {
            List<Path> elements = new ArrayList<>();
            int numero = 1;

            for (Path path : stream) {
                elements.add(path);
            }
            Collections.sort(elements);
            int i = 0;

            for (Path path : elements) {
                // Récupérer la note associée au numéro de fichier actuel
                String note = notesMap.get(numero);

                if (Files.isDirectory(path)) {
                    if (note != null) {
                        System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m" + " : " + note); // Blue for directories
                    } else {
                        System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m");
                    }
                } else {
                    if (note != null) {
                        System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m" + " : " + note); // Red for files
                    } else {
                        System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m");
                    }
                }
                i++;
                numero++;
            }
        }
    }
}












/*import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
// import java.util.HashMap;
import java.util.List;
// import java.util.Map;
// import java.util.function.Function;
// import java.util.stream.Collectors;

public class Afficheur {
    public static void displayCurrentDir(Directory directory) throws IOException {
        String fileName = "notes.txt";
        Path filePath = Paths.get(fileName);

        List<String> lines = Files.readAllLines(filePath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory.obtenirCheminComplet()))) {
            List<Path> elements = new ArrayList<>();
            int numero = 1;

            for (Path path : stream) {
                elements.add(path);
            }
            Collections.sort(elements);
            int i = 0;
            for (Path path : elements) {
                if (i < lines.size() && !lines.isEmpty()) {
                    if (Files.isDirectory(path)) {
                        System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m" + " :" + lines.get(i).split(" : ")[1]); // Blue for
                        // directories
                    } else {
                        System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m" + " :" + lines.get(i).split(" : ")[1]); // Red for files
                    }
                } else {
                    if (Files.isDirectory(path)) {
                        System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m"); // Blue for
                        // directories
                    } else {
                        System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m"); // Red for files
                    }
                }
                i++;
                numero++;
            }
        }
    }
}
*/
