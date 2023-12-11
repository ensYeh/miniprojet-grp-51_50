package fr.uvsq.cprog;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("create"))
                .build();

        while (true) {
            // Crée une instance de la classe Directory pour le répertoire courant
            Directory repertoireCourant = new Directory(System.getProperty("user.dir"));

            System.out.println("Contenu du répertoire courant :");
            Afficheur.afficherContenuAvecNumeros(repertoireCourant);

            System.out.println("\nChemin complet depuis la racine du système de fichiers :");
            System.out.println(repertoireCourant.obtenirCheminComplet());

            String line = reader.readLine("> ");
            if (line == null || line.equalsIgnoreCase("exit")) {
                break;
            }
            reader.getHistory().add(line);

            if (line.equalsIgnoreCase("create")) {
                // Implementez la logique de création de fichier/répertoire si nécessaire
                System.out.println("Création de fichier/répertoire (TBD)");
            } else {
                System.out.println("Unknown command: " + line);
            }
        }
    }
}



// import org.jline.reader.LineReader;
// import org.jline.reader.LineReaderBuilder;
// import org.jline.reader.impl.DefaultParser;
// import org.jline.reader.impl.completer.StringsCompleter;
// import org.jline.terminal.Terminal;
// import org.jline.terminal.TerminalBuilder;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.Scanner;
// import java.util.stream.Stream;

// public class App {
//     public static void main(String[] args) throws IOException {
//         // Chemin du répertoire racine sur Windows (ajustez selon votre système d'exploitation)
//        // String cheminRepertoireRacine = "C:\\";

//         Terminal terminal = TerminalBuilder.terminal();
//         LineReader reader = LineReaderBuilder.builder()
//                 .terminal(terminal)
//                 .completer(new StringsCompleter("create"))
//                 .build();

//         while (true) {
//             // Crée une instance de la classe Repertoire pour le répertoire courant
//             Directory repertoireCourant = new Directory(System.getProperty("user.dir"));

//             System.out.println("Contenu du répertoire courant :");
//             repertoireCourant.listerContenu();

//             System.out.println("\nChemin complet depuis la racine du système de fichiers :");
//             System.out.println(repertoireCourant.obtenirCheminComplet());

        
//             String line = reader.readLine("> ");
//             if (line == null || line.equalsIgnoreCase("exit")) {
//                 break;
//             }
//             reader.getHistory().add(line);

//             if (line.equalsIgnoreCase("create")) {
//                 // Implementez la logique de création de fichier/répertoire si nécessaire
//                 System.out.println("Création de fichier/répertoire (TBD)");
//             } else {
//                 System.out.println("Unknown command: " + line);
//             }
//         }
//     }
// }






























/**
 * Hello world!
 *
 */
// public class App {
//     //public static boolean marche = true;

//     public static void main(String[] args) throws IOException {
//         Terminal terminal = TerminalBuilder.terminal();
//         LineReader reader = LineReaderBuilder.builder()
//                 .terminal(terminal)
//                 .completer(new StringsCompleter("create"))
//                 .build();

        

//         while (true) {
//             System.out.println(getDirectoryHierarchy());
//             String line = reader.readLine("> ");
//             if (line == null || line.equalsIgnoreCase("exit")) {
//                 break;
//             }
//             reader.getHistory().add(line);

//             if (line.equalsIgnoreCase("create")) {
//                 // System.out.println(“TBD”);
//             } else {
//                 System.out.println("Unknown command: " + line);
//             }
//         }
//     }

//     public static String getDirectoryHierarchy() {
//         return "Current absolute path is : " + System.getProperty("user.dir");
//     }
// }