
package fr.uvsq.cprog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Debug;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteManager {

    private static final String FILE_NAME = "notes.json";

    public static void checkNotesFile() {
        try {
            Path filePath = Paths.get(FILE_NAME);

            // Vérifie si le fichier n'existe pas avant de le créer
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Le fichier \"" + FILE_NAME + "\" a été créé.");
            } else {
                System.out.println("Le fichier \"" + FILE_NAME + "\" existe déjà.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier \"" + FILE_NAME + "\": " + e.getMessage());
        }
    }

    public static void incrementNote(Integer X) {
        try {
            Path filePath = Paths.get(FILE_NAME);

            List<NoteEntry> noteEntries = readNotesFromJson(filePath);

            for (NoteEntry entry : noteEntries) {
                if (entry.getNumber() >= X) {
                    entry.setNumber(entry.getNumber() + 1);
                }
            }

            writeNotesToJson(filePath, noteEntries);
        } catch (IOException e) {
            System.err.println("Erreur lors de la modification du fichier \"" + FILE_NAME + "\": " + e.getMessage());
        }
    }

    public static void addNote(int number, String note) {
        try {
            Path filePath = Paths.get(FILE_NAME);

            List<NoteEntry> noteEntries = readNotesFromJson(filePath);

            for (NoteEntry entry : noteEntries)

                if (entry.getNumber() >= number){
                    entry.setNumber(entry.getNumber()+1);
                }
                System.out.println(number);



            noteEntries = readNotesFromJson(filePath);



            writeNotesToJson(filePath, noteEntries);

            boolean found = false;

            for (NoteEntry entry : noteEntries) {
                if (entry.getNumber() == number) {
                    entry.addNoteText(note);
                    found = true;
                    break;
                }
            }

            if (!found) {
                NoteEntry newEntry = new NoteEntry(number, note);
                noteEntries.add(newEntry);
                Collections.sort(noteEntries);
            }

            writeNotesToJson(filePath, noteEntries);
        } catch (IOException e) {
            System.err.println("Erreur lors de la modification du fichier \"" + FILE_NAME + "\": " + e.getMessage());
        }
    }

    public static void sortNotes() {
        try {
            Path filePath = Paths.get(FILE_NAME);

            List<NoteEntry> noteEntries = readNotesFromJson(filePath);

            Collections.sort(noteEntries);

            writeNotesToJson(filePath, noteEntries);
        } catch (IOException e) {
            System.err.println("Erreur lors du tri du fichier \"" + FILE_NAME + "\": " + e.getMessage());
        }
    }

    public static List<NoteEntry> readNotesFromJson(Path filePath) throws IOException {
        List<NoteEntry> noteEntries = new ArrayList<>();

        if (Files.exists(filePath) && Files.size(filePath) > 0) {
            List<String> jsonLines = Files.readAllLines(filePath);
            StringBuilder jsonString = new StringBuilder();
            for (String line : jsonLines) {
                jsonString.append(line);
            }

            Gson gson = new Gson();
            noteEntries = gson.fromJson(jsonString.toString(), new TypeToken<List<NoteEntry>>() {}.getType());
        }

        // Initialisation par défaut si noteEntries est nulle
        if (noteEntries == null) {
            noteEntries = new ArrayList<>();
        }

        return noteEntries;
    }

    private static void writeNotesToJson(Path filePath, List<NoteEntry> noteEntries) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonContent = gson.toJson(noteEntries);

        Files.write(filePath, Collections.singletonList(jsonContent), StandardOpenOption.WRITE);
    }
}

class NoteEntry implements Comparable<NoteEntry> {
    private int number;
    private List<String> notes;

    public NoteEntry(int number, String note) {
        this.number = number;
        this.notes = new ArrayList<>();
        this.notes.add(note);
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number){
        this.number = number;
    }
    public List<String> getNotes(){
        return notes;
    }

    public void addNoteText(String noteText) {
        notes.add(noteText);
    }


    @Override
    public int compareTo(NoteEntry other) {
        return Integer.compare(this.number, other.number);
    }

}
/*
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
//import java.util.stream.Collectors;
import java.util.Collections;
//import java.util.stream.Stream;

public class NoteManager {

    public static void checkNotesFile() {
        String fileName = "notes.json";
        Path filePath = Paths.get(fileName);

        try {
            // Vérifie si le fichier n'existe pas avant de le créer
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Le fichier \"" + fileName + "\" a été créé.");
            } else {
                System.out.println("Le fichier \"" + fileName + "\" existe déjà.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier \"" + fileName + "\": " + e.getMessage());
        }
    }

    public static void addNote(int number, String note) {
        try {
            String fileName = "notes.json";
            Path filePath = Paths.get(fileName);

            List<String> lines = Files.readAllLines(filePath);

            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(number + " : ")) {
                    lines.set(i, number + " : " + line.substring((number + " : ").length()) + " " + note);
                    found = true;
                    break;
                }
            }

            if (!found) {
                lines.add(number + " : " + note);
                Collections.sort(lines);
            }

            Files.write(filePath, lines, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.err.println("Erreur lors de la modification du fichier \"notes.json\": " + e.getMessage());
        }
    }

    public static void sortNotes() {
        try {
            String fileName = "notes.json";
            Path filePath = Paths.get(fileName);

            List<String> lines = Files.readAllLines(filePath);

            Collections.sort(lines);

            Files.write(filePath, lines, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.err.println("Erreur lors du tri du fichier \"notes.json\": " + e.getMessage());
        }
    }
}

*/


    // public static void updateNote(int number, String note) {
    //     try {
    //         String fileName = "notes.txt";
    //         Path filePath = Paths.get(fileName);

    //         List<String> lines = Files.readAllLines(filePath);

    //         for (int i = 0; i < lines.size(); i++) {
    //             String line = lines.get(i);
    //             if (line.startsWith(number + " : ")) {
    //                 lines.set(i, number + " : " + note);
    //                 break;
    //             }
    //         }

//             Files.write(filePath, lines, StandardOpenOption.WRITE);
//         } catch (IOException e) {
//             System.err.println("Erreur lors de la modification du fichier \"notes.txt\": " + e.getMessage());
//         }
//     }
// }
