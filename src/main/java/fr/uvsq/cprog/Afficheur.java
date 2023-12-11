package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Afficheur {
    public static void afficherContenuAvecNumeros(Directory repertoire) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(repertoire.obtenirCheminComplet()))) {
            int numero = 1;
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    System.out.println(numero + ". \u001B[34m" + path.getFileName() + "\u001B[0m"); // Blue for directories
                } else {
                    System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m"); // Red for files
                }
                numero++;
            }
        }
    }
}


