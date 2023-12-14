package fr.uvsq.cprog;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;

public class CommandManager {
    public static void mkdir(Directory repertoireCourant, String line) throws IOException {
        // Créer un nouveau répertoire
        String[] parts = line.split(" ");
        if (parts.length == 2) {
            String nomRepertoire = parts[1];
            Path cheminComplet = Paths.get(repertoireCourant.getChemin());
            Path nouveauRepertoire = cheminComplet.resolve(nomRepertoire);
            Files.createDirectory(nouveauRepertoire);
            System.out.println("Répertoire créé : " + nouveauRepertoire.getFileName());
        } else {
            System.out.println("Utilisation incorrecte. Exemple : mkdir nomDuRepertoire");
        }
    }

    public static void find(Path currentDir, String fileName) {
        try {
            Files.walk(currentDir)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .forEach(file -> {
                        Path relativePath = currentDir.relativize(file);
                        System.out.println(relativePath.toString());
                    });

        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche du fichier : " + e.getMessage());
        }
    }

    public static void visu(Directory repertoireCourant, int numeroElement) {
        try {
            Path cheminComplet = repertoireCourant.directoryMap().get(numeroElement);

            if (cheminComplet != null) {
                if (Files.isRegularFile(cheminComplet)) {
                    String extension = getExtension(cheminComplet);

                    if (extension != null && extension.equals("txt")) {
                        // Afficher le contenu du fichier texte
                        try {
                            Files.lines(cheminComplet)
                                    .forEach(System.out::println);
                        } catch (IOException e) {
                            System.err.println("Erreur lors de la lecture du fichier texte : " + e.getMessage());
                        }
                    } else {
                        // Afficher la taille si ce n'est pas un fichier texte
                        try {
                            long taille = Files.size(cheminComplet);
                            System.out.println("La taille du fichier est : " + taille + " octets");
                        } catch (IOException e) {
                            System.err.println("Erreur lors de la récupération de la taille du fichier : " + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("L'élément avec le numéro " + numeroElement + " n'est pas un fichier régulier");
                }
            } else {
                System.out.println("Aucun élément trouvé avec le numéro " + numeroElement);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération du chemin de l'élément : " + e.getMessage());
        }
    }

    private static String getExtension(Path file) {
        String fileName = file.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? null : fileName.substring(dotIndex + 1);
    }

      
}





    
    


