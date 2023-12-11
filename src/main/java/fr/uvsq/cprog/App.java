// App.java
package fr.uvsq.cprog;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("create", "mkdir"))
                .build();

        Directory repertoireCourant = new Directory(System.getProperty("user.dir"));

        while (true) {
            CommandManager.checkNotesFile();
            System.out.println("Contenu du répertoire courant :");
            Map<Integer, Path> numerosFichiers = Afficheur.afficherContenuAvecNumeros(repertoireCourant);

            System.out.println("\nChemin complet depuis la racine du système de fichiers :");
            System.out.println(repertoireCourant.obtenirCheminComplet());

            String line = reader.readLine("> ");
            if (line == null || line.equalsIgnoreCase("exit")) {
                break;
            }
            reader.getHistory().add(line);

            if (line.equalsIgnoreCase("create")) {
                // Implémentez la logique de création de fichier/répertoire si nécessaire
                System.out.println("Création de fichier/répertoire (TBD)");
            } else if (line.startsWith("mkdir")) {
                CommandManager.mkdir(repertoireCourant, line);
            } else {
                System.out.println("Unknown command: " + line);
            }
        }
    }
}



// // App.java
// package fr.uvsq.cprog;

// import org.jline.reader.LineReader;
// import org.jline.reader.LineReaderBuilder;
// import org.jline.reader.impl.completer.StringsCompleter;
// import org.jline.terminal.Terminal;
// import org.jline.terminal.TerminalBuilder;

// import java.io.IOException;
// import java.nio.file.Path;
// import java.util.Map;

// public class App {
//     public static void main(String[] args) throws IOException {
//         Terminal terminal = TerminalBuilder.terminal();
//         LineReader reader = LineReaderBuilder.builder()
//                 .terminal(terminal)
//                 .completer(new StringsCompleter("create"))
//                 .build();

//         while (true) {
//             // Crée une instance de la classe Directory pour le répertoire courant
//             Directory repertoireCourant = new Directory(System.getProperty("user.dir"));

//             System.out.println("Contenu du répertoire courant :");
//             Map<Integer, Path> numerosFichiers = Afficheur.afficherContenuAvecNumeros(repertoireCourant);

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

















// package fr.uvsq.cprog;

// import org.jline.reader.LineReader;
// import org.jline.reader.LineReaderBuilder;
// import org.jline.reader.impl.completer.StringsCompleter;
// import org.jline.terminal.Terminal;
// import org.jline.terminal.TerminalBuilder;

// import java.io.IOException;

// public class App {
//     public static void main(String[] args) throws IOException {
//         Terminal terminal = TerminalBuilder.terminal();
//         LineReader reader = LineReaderBuilder.builder()
//                 .terminal(terminal)
//                 .completer(new StringsCompleter("create"))
//                 .build();

//         while (true) {
//             // Crée une instance de la classe Directory pour le répertoire courant
//             Directory repertoireCourant = new Directory(System.getProperty("user.dir"));

//             System.out.println("Contenu du répertoire courant :");
//             Afficheur.afficherContenuAvecNumeros(repertoireCourant);

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































