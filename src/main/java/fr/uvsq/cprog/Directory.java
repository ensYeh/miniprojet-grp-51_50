package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.DirectoryStream;
//import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Cette classe représente un répertoire dans le gestionnaire de fichiers.
 * Elle offre des fonctionnalités pour obtenir des informations sur le contenu
 * du répertoire,
 * changer le chemin du répertoire, et déplacer le répertoire vers un nouveau
 * chemin.
 */
public class Directory {
  /**
   * Map associant chaque numéro à son chemin correspondant dans le répertoire.
   */

  Map<Integer, Path> contentMap;

  /**
   * Le chemin du répertoire.
   */
  private String chemin;

  /**
   * Constructeur de la classe Directory.
   *
   * @param chemin Le chemin du répertoire.
   * @throws IOException En cas d'erreur d'entrée/sortie lors de la création du
   *                     répertoire.
   */

  public Directory(String chemin) throws IOException {
    this.chemin = chemin;
    this.contentMap = directoryMap();
  }

  /**
   * Constructeur de la classe Directory avec une map de contenu spécifiée.
   *
   * @param chemin     Le chemin du répertoire.
   * @param contentMap Map associant chaque numéro à son chemin correspondant.
   */

  public Directory(String chemin, Map<Integer, Path> contentMap) {
    this.chemin = chemin;
    this.contentMap = contentMap;
  }

  /**
   * Obtient la map associant chaque numéro à son chemin correspondant dans le
   * répertoire.
   *
   * @return La map de contenu du répertoire.
   * @throws IOException En cas d'erreur d'entrée/sortie lors de la récupération
   *                     du contenu du répertoire.
   */

  public Map<Integer, Path> directoryMap() throws IOException {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(this.getChemin()))) {
      List<Path> elements = new ArrayList<>();
      for (Path path : stream) {
        elements.add(path);
      }
      Collections.sort(elements);
      // Crée une map associant chaque numéro à son chemin correspondant
      return elements.stream()
            .collect(Collectors.toMap(
                    path -> elements.indexOf(path) + 1,
                    Function.identity()));
    } catch (IOException e) {
      System.err.println("Erreur lors de la création du répertoire : " + e.getMessage());
      return Collections.emptyMap(); // Ou retournez une valeur par défaut appropriée
    }
  }

  /**
   * Obtient le chemin du répertoire.
   *
   * @return Le chemin du répertoire.
   */

  public String getChemin() {
    return this.chemin;
  }

  /**
   * Modifie le chemin du répertoire.
   *
   * @param chemin Le nouveau chemin du répertoire.
   */

  public void setChemin(String chemin) {
    this.chemin = chemin;
  }

  /**
   * Déplace le répertoire vers un nouveau chemin spécifié.
   *
   * @param nouveauChemin Le nouveau chemin du répertoire.
   * @throws IOException En cas d'erreur d'entrée/sortie lors de l'opération de
   *                     déplacement.
   */

  public void moveTo(Path nouveauChemin) throws IOException {
    // Vérifier si le nouveau chemin correspond à un répertoire
    if (Files.isDirectory(nouveauChemin)) {
      this.chemin = nouveauChemin.toString();
      this.contentMap = directoryMap();
    } else {
      // Si ce n'est pas un répertoire, vous pouvez lancer une exception ou prendre
      // d'autres mesures selon vos besoins.
      System.out.println("Le chemin spécifié ne correspond pas à un répertoire.");
    }
  }

  /**
   * Obtient la clé associée à une valeur (chemin) dans la map de contenu du
   * répertoire.
   *
   * @param value Le chemin dont on cherche la clé.
   * @return La clé associée au chemin spécifié, ou null si la valeur n'est pas
   *         trouvée.
   */

  public Integer getKeyForValue(Path value) {
    System.out.println(value);
    for (Map.Entry<Integer, Path> entry : contentMap.entrySet()) {
      if (entry.getValue().equals(value)) {
        return entry.getKey();
      }
    }
    // Retourner null si la valeur n'est pas trouvée
    return null;
  }
}
