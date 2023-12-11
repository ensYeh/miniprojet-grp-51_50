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
import java.util.function.Function;
import java.util.stream.Collectors;

public class Afficheur {
    public static Map<Integer, Path> afficherContenuAvecNumeros(Directory repertoire) throws IOException {

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(repertoire.obtenirCheminComplet()))) {
            List<Path> elements = new ArrayList<>();
            int numero = 1;

            for (Path path : stream) {
                elements.add(path);
            }

            Collections.sort(elements);

            for (Path path : elements) {
                if (Files.isDirectory(path)) {
                    System.out.println(numero + ". \u001B[34m" + path.getFileName() + "/\u001B[0m"); // Blue for directories
                } else {
                    System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m"); // Red for files
                }
                numero++;
            }

            // Crée une map associant chaque numéro à son chemin correspondant
            return elements.stream()
                .collect(Collectors.toMap(
                    path -> elements.indexOf(path) + 1,
                    Function.identity()
                ));
        }
    }
}

    //     Map<Integer, Path> numerosFichiers = new HashMap<>();

    //     try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(repertoire.obtenirCheminComplet()))) {
    //         int numero = 1;
    //         for (Path path : stream) {
    //             numerosFichiers.put(numero, path);

    //             if (Files.isDirectory(path)) {
    //                 System.out.println(numero + ". \u001B[34m" + path.getFileName() + "\u001B[0m"); // Blue for directories
    //             } else {
    //                 System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m"); // Red for files
    //             }
    //             numero++;
    //         }
    //     }

    //     return numerosFichiers;
    // }



























// package fr.uvsq.cprog;

// import java.io.IOException;
// import java.nio.file.DirectoryStream;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// public class Afficheur {
//     public static void afficherContenuAvecNumeros(Directory repertoire) throws IOException {
//         try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(repertoire.obtenirCheminComplet()))) {
//             int numero = 1;
//             for (Path path : stream) {
//                 if (Files.isDirectory(path)) {
//                     System.out.println(numero + ". \u001B[34m" + path.getFileName() + "\u001B[0m"); // Blue for directories
//                 } else {
//                     System.out.println(numero + ". \u001B[31m" + path.getFileName() + "\u001B[0m"); // Red for files
//                 }
//                 numero++;
//             }
//         }
//     }
// }


