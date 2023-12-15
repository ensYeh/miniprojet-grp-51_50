package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Directory{

    Map<Integer,Path> contentMap;
    private String chemin;
    public Directory(String chemin) throws IOException {
        this.chemin = chemin;
        this.contentMap = directoryMap();
    }

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
                            Function.identity()
                    ));
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du répertoire : " + e.getMessage());
            return Collections.emptyMap(); // Ou retournez une valeur par défaut appropriée
        }
    }

    // public Map<Integer, Path> directoryMap() throws IOException {

    //     try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(this.getChemin()))) {
    //         List<Path> elements = new ArrayList<>();
    //         for (Path path : stream) {
    //             elements.add(path);
    //         }
    //         Collections.sort(elements);
    //         // Crée une map associant chaque numéro à son chemin correspondant
    //         return elements.stream()
    //                 .collect(Collectors.toMap(
    //                         path -> elements.indexOf(path) + 1,
    //                         Function.identity()
    //                 ));
    //     }
    // }

    public String getChemin(){
        return this.chemin;
    }
    
    public void moveTo(Path nouveauChemin) throws IOException {
        // Vérifier si le nouveau chemin correspond à un répertoire
        if (Files.isDirectory(nouveauChemin)) {
            this.chemin = nouveauChemin.toString();
            this.contentMap = directoryMap();
        } else {
            // Si ce n'est pas un répertoire, vous pouvez lancer une exception ou prendre d'autres mesures selon vos besoins.
            System.out.println("Le chemin spécifié ne correspond pas à un répertoire.");
        }
    }

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

    public void deleteElement(int numeroElement) throws IOException {
        Path elementPath = contentMap.get(numeroElement);
        if (elementPath != null) {
            Files.delete(elementPath);
            // Mettez à jour la map après la suppression du fichier
            contentMap = directoryMap();
            System.out.println("Élément numéro " + numeroElement + " supprimé.");
        } else {
            System.out.println("Aucun élément trouvé avec le numéro " + numeroElement);
        }
    }

    public static void deleteDirectory(Path directory) throws IOException {
    Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            // Handle the case where the visit of a file fails
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (exc == null) {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            } else {
                // Directory iteration failed
                throw exc;
            }
        }
    });
}

    
}






