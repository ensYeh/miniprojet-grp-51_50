package fr.uvsq.cprog;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.io.BufferedReader;
//import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private static Map<Integer, Path> pressePapier = new HashMap<>();

    public static void cut(Directory repertoireCourant, int numeroElement) throws IOException {
        try {
            Path cheminComplet = repertoireCourant.directoryMap().get(numeroElement);
    
            if (cheminComplet != null) {
                // Vérifiez si le chemin est un répertoire et s'il n'est pas vide
                if (Files.isDirectory(cheminComplet) && Files.list(cheminComplet).findFirst().isPresent()) {
                    System.out.print(
                            "Le répertoire n'est pas vide. Êtes-vous sûr de vouloir le supprimer récursivement? (Y/N): ");
                    String confirmation = new BufferedReader(new InputStreamReader(System.in)).readLine();
    
                    if (!confirmation.equalsIgnoreCase("Y")) {
                        System.out.println("Opération annulée.");
                        return;
                    }
    
                    // Supprimer le répertoire récursivement
                    Directory.deleteDirectory(cheminComplet);
                } else {
                    // Supprime la note associée dans NoteManager
                    NoteManager.deleteNoteIfExists(numeroElement, repertoireCourant.getChemin());
                    
                    // Copier l'élément dans le presse-papiers
                    pressePapier.put(numeroElement, cheminComplet);
    
                    // Supprimer l'élément du répertoire courant
                    repertoireCourant.deleteElement(numeroElement);
    
                    System.out.println("Élément numéro " + numeroElement + " coupé et placé dans le presse-papiers.");
                }
            } else {
                System.out.println("Aucun élément trouvé avec le numéro " + numeroElement);
            }
        } catch (IOException e) {
            // Gérez l'exception ici
            e.printStackTrace();
        }
    }
    
     public static void afficherPressePapier() {
        System.out.println("\nContenu du presse-papiers :");
        if (pressePapier != null && !pressePapier.isEmpty()) {
            for (Map.Entry<Integer, Path> entry : pressePapier.entrySet()) {
                System.out.println("Numéro " + entry.getKey() + ": " + entry.getValue());
            }
        } else {
            System.out.println("Le presse-papiers est vide.");
        }
    }

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
            boolean fichierTrouve = Files.walk(currentDir)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .map(path -> currentDir.relativize(path).toString())
                    .anyMatch(matchingFile -> {
                        System.out.println(matchingFile);
                        return true; // Retourne toujours vrai pour indiquer que le fichier a été trouvé
                    });

            if (!fichierTrouve) {
                System.out.println("Le fichier que vous cherecher n'existe pas");
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche du fichier : " + e.getMessage());
        }
    }

    public static void visu(Directory repertoireCourant, int numeroElement) {
        try {
            Path cheminComplet = repertoireCourant.directoryMap().get(numeroElement);

            if (cheminComplet != null && Files.isDirectory(cheminComplet)) {
                // Afficher la taille du sous-répertoire
                try {
                    long taille = getDirectorySize(cheminComplet);
                    System.out.println("La taille du sous-répertoire est : " + taille + " octets");
                } catch (IOException e) {
                    System.err.println("Erreur lors de la récupération de la taille du sous-répertoire : " + e.getMessage());
                }
            } else if (cheminComplet != null && Files.isRegularFile(cheminComplet)) {
                String extension = getExtension(cheminComplet);

                if (extension != null && extension.equals("txt")) {
                    // Afficher le contenu du fichier texte
                    try {
                        Files.lines(cheminComplet).forEach(System.out::println);
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la lecture du fichier texte : " + e.getMessage());
                    }
                } else {
                    // Afficher la taille du fichier s'il n'est pas un fichier texte
                    try {
                        long taille = Files.size(cheminComplet);
                        System.out.println("La taille du fichier est : " + taille + " octets");
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la récupération de la taille du fichier : " + e.getMessage());
                    }
                }
            } else {
                System.out.println("L'élément avec le numéro " + numeroElement + " n'est ni un fichier régulier ni un répertoire");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération du chemin de l'élément : " + e.getMessage());
        }
    }

    // Méthode pour obtenir la taille d'un répertoire
    private static long getDirectorySize(Path directory) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            long size = 0;
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    size += Files.size(entry); // Taille des fichiers réguliers
                }
            }

            return size;
        }
    }

    private static String getExtension(Path file) {
        String fileName = file.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? null : fileName.substring(dotIndex + 1);
    }
}

