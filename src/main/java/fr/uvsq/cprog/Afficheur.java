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

/**
 *  Classe d'affichage du répertoire courant.
 */
public class Afficheur {

  /**
   * Affiche le contenu du répertoire spécifié, en associant les notes lues depuis
   * le fichier JSON "notes.json" avec les éléments du répertoire.
   *
   * @param directory Le répertoire à afficher.
   * @throws IOException En cas d'erreur lors de la lecture des notes depuis le
   *                     fichier JSON.
   */
  public static void displayCurrentDir(Directory directory) throws IOException {
    String fileName = directory.getChemin() + "/" + "notes.json";
    System.out.println(fileName);
    Path filePath = Paths.get(fileName);

    // Utiliser NoteManager pour lire les notes depuis le fichier JSON
    List<NoteEntry> noteEntries = NoteManager.readNotesFromJson(filePath);

    // Créer une map pour associer les numéros de fichier aux notes
    Map<Integer, List<String>> notesMap = new HashMap<>();
    for (NoteEntry entry : noteEntries) {
      notesMap.put(entry.getNumber(), entry.getNotes());
    }

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths
            .get(directory.getChemin()))) {
      List<Path> elements = new ArrayList<>();
      int numero = 1;

      for (Path path : stream) {
        elements.add(path);
      }
      Collections.sort(elements);
      for (Path path : elements) {
        // Récupérer la liste de notes associée au numéro de fichier actuel
        List<String> notes = notesMap.get(numero);

        if (Files.isDirectory(path)) {
          if (notes != null && !notes.isEmpty()) {
            System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m" + " : "
                  + String.join(", ", notes)); // Blue for directories
          } else {
            System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m");
          }
        } else {
          if (notes != null && !notes.isEmpty()) {
            System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m" + " : "
                  + String.join(", ", notes)); // Red for files
          } else {
            System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m");
          }
        }
        numero++;
      }
    }
  }
}
