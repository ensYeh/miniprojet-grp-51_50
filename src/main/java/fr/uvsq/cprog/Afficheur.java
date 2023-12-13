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
        String fileName = "notes.json";
        Path filePath = Paths.get(fileName);

        // Utiliser NoteManager pour lire les notes depuis le fichier JSON
        List<NoteEntry> noteEntries = NoteManager.readNotesFromJson(filePath);

        // Créer une map pour associer les numéros de fichier aux notes
        Map<Integer, List<String>> notesMap = new HashMap<>();
        for (NoteEntry entry : noteEntries) {
            notesMap.put(entry.getNumber(), entry.getNotes());
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
                // Récupérer la liste de notes associée au numéro de fichier actuel
                List<String> notes = notesMap.get(numero);

                if (Files.isDirectory(path)) {
                    if (notes != null && !notes.isEmpty()) {
                        System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m" + " : " + String.join(", ", notes)); // Blue for directories
                    } else {
                        System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m");
                    }
                } else {
                    if (notes != null && !notes.isEmpty()) {
                        System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m" + " : " + String.join(", ", notes)); // Red for files
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
