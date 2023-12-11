package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandManager {
    public static void mkdir(Directory repertoireCourant, String line) throws IOException {
        // Créer un nouveau répertoire
        String[] parts = line.split(" ");
        if (parts.length == 2) {
            String nomRepertoire = parts[1];
            Path cheminComplet = Paths.get(repertoireCourant.obtenirCheminComplet());
            Path nouveauRepertoire = cheminComplet.resolve(nomRepertoire);
            Files.createDirectory(nouveauRepertoire);
            System.out.println("Répertoire créé : " + nouveauRepertoire.getFileName());
        } else {
            System.out.println("Utilisation incorrecte. Exemple : mkdir nomDuRepertoire");
        }
    }

    public static void find(Path path){

    }
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
}


