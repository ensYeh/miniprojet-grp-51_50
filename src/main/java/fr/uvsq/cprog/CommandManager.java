package fr.uvsq.cprog;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.io.BufferedReader;
//import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.ArrayList;
//import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandManager {

    public static Map<Integer, Path> pressePapier = new HashMap<>();
    private static Map<Integer, Boolean> cutFiles = new HashMap<>();
    private static int uniqueElementNumber = 1;

    public static void cut(Directory repertoireCourant, int numeroElement) throws IOException {
        try {
            Path cheminComplet = repertoireCourant.directoryMap().get(numeroElement);

            if (cheminComplet != null) {
                if (Files.isDirectory(cheminComplet) && Files.list(cheminComplet).findFirst().isPresent()) {
                    // Récupérer tous les éléments du répertoire
                    List<Path> elements = Files.list(cheminComplet).collect(Collectors.toList());

                    // Ajouter le répertoire lui-même à la liste
                    elements.add(cheminComplet);

                    // Code pour traiter les répertoires et leurs éléments
                    for (Path element : elements) {
                        System.out.println("les element sont : " + element);
                        int elementNumero = generateUniqueElementNumber(); // Remplacez ceci par une logique pour
                                                                           // générer un numéro unique

                        // NoteManager.deleteNoteIfExists(elementNumero, repertoireCourant.getChemin());
                        pressePapier.put(elementNumero, element);
                        cutFiles.put(elementNumero, true); // Marquer le fichier ou le répertoire comme coupé
                        System.out
                                .println("Élément numéro " + elementNumero + " coupé et placé dans le presse-papiers.");
                    }

                } else {
                    // NoteManager.deleteNoteIfExists(numeroElement, repertoireCourant.getChemin());
                    pressePapier.put(numeroElement, cheminComplet);
                    cutFiles.put(numeroElement, true); // Marquer le fichier comme coupé
                    System.out.println("Élément numéro " + numeroElement + " coupé et placé dans le presse-papiers.");
                }
            } else {
                System.out.println("Aucun élément trouvé avec le numéro " + numeroElement);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode factice pour générer un numéro unique pour chaque élément coupé
    private static int generateUniqueElementNumber() {
        // Implémentez une logique pour générer un numéro unique (par exemple, utilisez
        // un compteur)
        return uniqueElementNumber;
    }

    public static void past(Directory repertoireCourant) {
        if (!pressePapier.isEmpty()) {
            for (Map.Entry<Integer, Path> entry : pressePapier.entrySet()) {
                Path elementACopier = entry.getValue();
                int numeroElement = entry.getKey();

                // Vérifier si le fichier est marqué comme coupé
                if (cutFiles.containsKey(numeroElement) && cutFiles.get(numeroElement)) {
                    // Coller le fichier seulement s'il est marqué comme coupé
                    Path destination = Paths.get(repertoireCourant.getChemin()).resolve(elementACopier.getFileName());

                    while (Files.exists(destination)) {
                        // Logique de gestion des conflits de noms
                        try {
                            System.out.print("Le fichier '" + destination.getFileName()
                                    + "' existe déjà. Voulez-vous écraser le fichier? (Y/N/R pour renommer): ");
                            String choixUtilisateur = new BufferedReader(new InputStreamReader(System.in)).readLine();

                            if (choixUtilisateur.equalsIgnoreCase("Y")) {
                                // Écraser le fichier existant
                                break; // Sortir de la boucle
                            } else if (choixUtilisateur.equalsIgnoreCase("N")) {
                                // Afficher un message et sortir de la boucle
                                System.out.println("Impossible de copier le fichier. Opération annulée.");
                                return; // Sortir de la méthode ou gérer de manière appropriée selon vos besoins
                            } else if (choixUtilisateur.equalsIgnoreCase("R")) {
                                // Demander à l'utilisateur de renommer le fichier à coller
                                System.out.print("Veuillez entrer un nouveau nom pour le fichier à coller: ");
                                String nouveauNom = new BufferedReader(new InputStreamReader(System.in)).readLine();
                                destination = Paths.get(repertoireCourant.getChemin()).resolve(nouveauNom);
                                // mettre a jour la hash map
                                // récupérer le NER de l'élément ajouté
                                // pour toutes les notes qui ont un number > on fait +1
                            } else {
                                // Gérer le cas où l'entrée de l'utilisateur n'est pas valide
                                System.out.println(
                                        "Entrée invalide. Veuillez entrer Y pour écraser, N pour annuler, ou R pour renommer.");
                            }
                        } catch (IOException e) {
                            System.err.println("Erreur lors de la lecture de l'entrée utilisateur : " + e.getMessage());
                        }
                    }

                    try {
                        if (Files.isDirectory(elementACopier)) {
                            copyDirectoryContents(elementACopier, destination, repertoireCourant);
                            Files.delete(elementACopier);
                        } else {
                            Files.move(elementACopier, destination);
                            System.out.println("Élément collé avec succès : " + destination);
                        }

                    } catch (IOException e) {
                        System.err.println("Erreur lors du collage de l'élément : " + e.getMessage());
                    }
                }
            }

            cutFiles.clear();
        } else {
            System.out.println("Erreur : Il n'y a pas d'élément à coller.");
        }
    }

    private static void copyDirectoryContents(Path source, Path target, Directory currentDir) throws IOException {
        // Créer le répertoire de destination s'il n'existe pas
        if (!Files.exists(target)) {
            Files.createDirectories(target);
        }

        // Parcourir tous les fichiers du répertoire source
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            System.out.println(stream);
            System.out.println("je suis la");
            for (Path entry : stream) {
                System.out.println(entry);
                Path entryTarget = target.resolve(entry.getFileName());
                int NER = currentDir.getKeyForValue(entry);
                // Appeler la fonction récursivement si l'entrée est un répertoire
                if (Files.isDirectory(entry)) {
                    copyDirectoryContents(entry, entryTarget, currentDir);
                } else {
                    // Copier le fichier
                    System.out.println("je suis ici");
                    List <String> notes =  NoteManager.getNotesForNumber(NER, currentDir.getChemin());
                    for (int i = 0; i< notes.size(); i++){
                        NoteManager.addNote(NER, notes.get(i),currentDir);
                    }
                    Files.copy(entry, entryTarget);
                }
                NoteManager.deleteNoteIfExists(NER,currentDir.getChemin());
                Files.delete(entry);

            }
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
                    System.err.println(
                            "Erreur lors de la récupération de la taille du sous-répertoire : " + e.getMessage());
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
                        System.err
                                .println("Erreur lors de la récupération de la taille du fichier : " + e.getMessage());
                    }
                }
            } else {
                System.out.println(
                        "L'élément avec le numéro " + numeroElement + " n'est ni un fichier régulier ni un répertoire");
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
