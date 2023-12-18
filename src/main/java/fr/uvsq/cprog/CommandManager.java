package fr.uvsq.cprog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Cette classe gère les opérations de copie, coupe, collage, création de
 * répertoires,
 * recherche de fichiers, visualisation du contenu, et autres commandes du
 * gestionnaire de fichiers.
 */

public class CommandManager {

  /**
    * Le presse-papiers qui stocke les éléments copiés ou coupés.
   */

  public static Map<Integer, Path> pressePapier = new HashMap<>();

  /**
    * Map indiquant si les fichiers correspondant aux numéros sont coupés (true) ou
    * copiés (false).
    */
  public static Map<Integer, Boolean> cutFiles = new HashMap<>();

  /**
    * Numéro unique pour chaque élément dans le gestionnaire de fichiers.
    */
  private static int uniqueElementNumber = 1;

  /**
    * Copie ou coupe les éléments spécifiés dans le presse-papiers.
    *
    * @param repertoireCourant Le répertoire courant où se trouvent les éléments à
    *                          copier ou couper.
    * @param numeroElement     Le numéro de l'élément à copier ou couper.
    * @throws IOException En cas d'erreur d'entrée/sortie lors de l'opération de
    *                     copie ou coupe.
    */

  public static void copyCut(Directory repertoireCourant, int numeroElement) throws IOException {
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
            int elementNumero = generateUniqueElementNumber();

            pressePapier.put(elementNumero, element);
            cutFiles.put(elementNumero, true); // Marquer le fichier ou le répertoire comme coupé
            System.out.println("Élément numéro " + elementNumero
                    + " coupé et placé dans le presse-papiers.");
          }

        } else {
          // NoteManager.deleteNoteIfExists(numeroElement, repertoireCourant.getChemin());
          pressePapier.put(numeroElement, cheminComplet);
          cutFiles.put(numeroElement, true); // Marquer le fichier comme coupé
          System.out.println("Élément numéro " + numeroElement
                  + " coupé et placé dans le presse-papiers.");
        }
      } else {
        System.out.println("Aucun élément trouvé avec le numéro " + numeroElement);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
    * Génère un numéro unique pour un élément.
    *
    * @return Le numéro unique généré.
    */

  public static int generateUniqueElementNumber() {
    return uniqueElementNumber;
  }

  /**
   * Colle les éléments du presse-papiers dans le répertoire courant.
   *
   * @param currentDir Le répertoire courant où coller les éléments.
   * @param copyCut    Indique si les éléments sont copiés ou coupés.
   * @throws IOException En cas d'erreur d'entrée/sortie lors de l'opération de
   *                     collage.
   */

  public static void past(Directory currentDir, String copyCut) throws IOException {
    if (!pressePapier.isEmpty()) {
      for (Map.Entry<Integer, Path> entry : pressePapier.entrySet()) {
        Path elementToCopy = entry.getValue();
        int numeroElement = entry.getKey();

        // Vérifier si le fichier est marqué comme coupé
        if (cutFiles.containsKey(numeroElement) && cutFiles.get(numeroElement)) {
          // Coller le fichier seulement s'il est marqué comme coupé
          Path destination = Paths.get(currentDir.getChemin()).resolve(elementToCopy.getFileName());
          while (Files.exists(destination)) {
            // Logique de gestion des conflits de noms
            try {
              System.out.print("Le fichier '" + destination.getFileName()
                      + "' existe déjà. Voulez-vous renommer le fichier? "
                      + "(Y/N (N annule l'action): ");
              String choixUtilisateur = new BufferedReader(new InputStreamReader(System.in))
                      .readLine();

              if (choixUtilisateur.equalsIgnoreCase("N")) {
                // Afficher un message et sortir de la boucle
                System.out.println("Impossible de copier le fichier. Opération annulée.");
                return; // Sortir de la méthode ou gérer de manière appropriée selon vos besoins
              } else if (choixUtilisateur.equalsIgnoreCase("Y")) {
                // Demander à l'utilisateur de renommer le fichier à coller
                System.out.print("Veuillez entrer un nouveau nom pour le fichier à coller: ");
                String nouveauNom = new BufferedReader(new InputStreamReader(System.in)).readLine();
                destination = Paths.get(currentDir.getChemin()).resolve(nouveauNom);
                // mettre a jour la hash map
                // récupérer le NER de l'élément ajouté
                // pour toutes les notes qui ont un number > on fait +1
              } else {
                // Gérer le cas où l'entrée de l'utilisateur n'est pas valide
                System.out.println(
                        "Entrée invalide. Veuillez entrer Y pour écraser, N pour annuler, "
                                + "ou R pour renommer.");
              }
            } catch (IOException e) {
              System.err.println("Erreur lors de la lecture de l'entrée utilisateur : "
                      + e.getMessage());
            }
          }
          if (Files.isDirectory(elementToCopy)) {
            copyDirectoryContents(elementToCopy, elementToCopy, destination, currentDir, copyCut);
          } else {
            Files.copy(elementToCopy, destination);
          }
          currentDir.contentMap = currentDir.directoryMap();
          Directory parentDir = new Directory(elementToCopy.getParent().toString());
          int ner = parentDir.getKeyForValue(elementToCopy);
          List<String> notes = NoteManager.getNotesForNumber(ner, parentDir.getChemin());
          ner = currentDir.getKeyForValue(destination);
          NoteManager.modifyNoteNumber(ner, currentDir.getChemin(), "+");
          Directory targetDir = new Directory(destination.getParent().toString());
          for (int i = 0; i < notes.size(); i++) {
            System.out.println("Note : " + notes.get(i) + " repo : " + targetDir.getChemin());
            if (NoteManager.checkNotesFile(currentDir.getChemin())) {
              System.out.println("Chemin : " + currentDir.getChemin());
              currentDir.contentMap = currentDir.directoryMap();
              NoteManager.modifyNoteNumber(ner, currentDir.getChemin(), "+");
              ner += 1;
            }
            NoteManager.addNote(ner, notes.get(i), targetDir);
          }
          if (copyCut.equals("cut")) {
            ner = parentDir.getKeyForValue(elementToCopy);
            System.out.println("ParentDir = " + parentDir.getChemin());
            NoteManager.deleteNoteIfExists(ner, parentDir.getChemin());
            NoteManager.modifyNoteNumber(ner, parentDir.getChemin(), "-");
            Files.delete(elementToCopy);
          }
        }
      }
      cutFiles.clear();
    } else {
      System.out.println("Erreur : Il n'y a pas d'élément à coller.");
    }
  }

  private static void
      copyDirectoryContents(Path source, Path premier, Path target, Directory currentDir,
                          String copyCut) throws IOException {
    // Créer le répertoire de destination s'il n'existe pas
    if (!Files.exists(target)) {
      Files.createDirectories(target);
      currentDir.contentMap = currentDir.directoryMap();
      if (source == premier) {
        // repertoire ou le fichier est collé
        int ner = currentDir.getKeyForValue(target);
        NoteManager.modifyNoteNumber(ner, currentDir.getChemin().toString(), "+");
      }
    }
    // Parcourir tous les fichiers du répertoire source
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
      for (Path entry : stream) {
        Path entryTarget = target.resolve(entry.getFileName());
        // Appeler la fonction récursivement si l'entrée est un répertoire
        if (Files.isDirectory(entry)) {
          copyDirectoryContents(entry, premier, entryTarget, currentDir, copyCut);
        } else {
          // Copier le fichier
          Files.copy(entry, entryTarget);
        }
        if (copyCut.equals("cut")) {
          Files.delete(entry);
        }
      }
    }
  }

  /**
   * Affiche le contenu du presse-papiers.
   */

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

  /**
   * Crée un nouveau répertoire dans le répertoire courant.
   *
   * @param repertoireCourant Le répertoire courant où créer le nouveau
   *                          répertoire.
   * @param line              La ligne de commande entrée par l'utilisateur.
   * @throws IOException En cas d'erreur d'entrée/sortie lors de la création du
   *                     répertoire.
   */

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

  /**
   * Recherche un fichier dans le répertoire courant et ses sous-répertoires.
   *
   * @param currentDir Le répertoire courant où effectuer la recherche.
   * @param fileName   Le nom du fichier à rechercher.
   */

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

  /**
   * Affiche des informations sur un élément dans le répertoire courant.
   *
   * @param repertoireCourant Le répertoire courant.
   * @param numeroElement     Le numéro de l'élément à afficher.
   */

  public static void visu(Directory repertoireCourant, int numeroElement) {
    try {
      Path cheminComplet = repertoireCourant.directoryMap().get(numeroElement);
      if (cheminComplet != null && Files.isDirectory(cheminComplet)) {
        // Afficher la taille du sous-répertoire
        try {
          long taille = getDirectorySize(cheminComplet);
          System.out.println("La taille du sous-répertoire est : " + taille + " octets");
        } catch (IOException e) {
          System.err.println("Erreur lors de la récupération de la taille du sous-répertoire : "
                  + e.getMessage());
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
            System.err.println("Erreur lors de la récupération de la taille du fichier : "
                    + e.getMessage());
          }
        }
      } else {
        System.out.println("L'élément avec le numéro " + numeroElement + " n'est ni un fichier "
                + "régulier ni un répertoire");
      }
    } catch (IOException e) {
      System.err.println("Erreur lors de la récupération du chemin de l'élément : "
              + e.getMessage());
    }
  }
  /**
   * Calcule la taille totale des fichiers réguliers dans le répertoire spécifié.
   *
   * @param directory Le chemin du répertoire.
   * @return La taille totale des fichiers réguliers dans le répertoire, en octets.
   * @throws IOException En cas d'erreur d'accès au répertoire.
   */

  public static long getDirectorySize(Path directory) throws IOException {
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

  /**
   * Modifie le presse-papiers avec une nouvelle map d'éléments.
   *
   * @param newpressePapier La nouvelle map d'éléments à mettre dans le
   *                        presse-papiers.
   */
  public static void setPressePapier(Map<Integer, Path> newpressePapier) {
    pressePapier = newpressePapier;
  }

  /**
   * Obtient l'extension d'un fichier à partir du chemin spécifié.
   *
   * @param file Le chemin du fichier.
   * @return L'extension du fichier, ou null si aucun fichier ou extension n'est trouvé.
   */
  public static String getExtension(Path file) {
    String fileName = file.getFileName().toString();
    // Ignorer les fichiers cachés sans extension
    if (fileName.startsWith(".") && fileName.lastIndexOf('.') == 0) {
      return null;
    }
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? null : fileName.substring(dotIndex + 1);
  }
}
