
package fr.uvsq.cprog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
//import org.jetbrains.annotations.Debug;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteManager {

    public static final String FILE_NAME = "notes.json";

    public static List<String> getNotesForNumber(int number, String path) {
        try {
            Path filePath = Paths.get(path, FILE_NAME);

            List<NoteEntry> noteEntries = readNotesFromJson(filePath);

            for (NoteEntry entry : noteEntries) {
                if (entry.getNumber() == number) {
                    return entry.getNotes();
                }
            }

            System.out.println("Aucune note trouvée avec le numéro " + number + ".");
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier \"" + FILE_NAME + "\": " + e.getMessage());
        }

        return Collections.emptyList(); // Retourner une liste vide si aucune note n'est trouvée
    }

    public static boolean checkNotesFile(String path) {
        boolean fileCreated = false;
        try {
            // Construire le chemin complet du fichier notes.json dans le répertoire actuel
            Path filePath = Paths.get(path, FILE_NAME);
            System.out.println(filePath);

            // Vérifier si le fichier n'existe pas avant de le créer
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                fileCreated = true;
                System.out.println("Le fichier \"" + FILE_NAME + "\" a été créé.");
            } else {
                System.out.println("Le fichier \"" + FILE_NAME + "\" existe déjà.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier \"" + FILE_NAME + "\": " + e.getMessage());
        }
        return fileCreated;
    }

    public static void modifyNoteNumber(Integer X, String path, String val) {
        try {
            Path filePath = Paths.get(path, FILE_NAME);

            List<NoteEntry> noteEntries = readNotesFromJson(filePath);
            for (NoteEntry entry : noteEntries) {
                if (entry.getNumber() >= X) {
                    if (val.equals("+")){
                        entry.setNumber(entry.getNumber() + 1);
                    }
                    else if (val.equals("-")){
                        entry.setNumber(entry.getNumber() - 1);
                    }
                }
            }

            writeNotesToJson(filePath, noteEntries);
        } catch (IOException e) {
            System.err.println("Erreur lors de la modification du fichier \"" + FILE_NAME + "\": " + e.getMessage());
        }
    }

    public static void addNote(int number, String note, Directory currentDir) {
        try {
            Path filePath = Paths.get(currentDir.getChemin(), FILE_NAME);
            List<NoteEntry> noteEntries = readNotesFromJson(filePath);

            boolean noteAlreadyExists = false;

            for (NoteEntry entry : noteEntries) {
                if (entry.getNumber() == number) {
                    // Vérifier si la note existe déjà avant de l'ajouter
                    if (!entry.getNotes().contains(note)) {
                        entry.addNoteText(note);
                        noteAlreadyExists = true;
                    } else {
                        // La note existe déjà, pas besoin d'ajouter une nouvelle fois
                        return;
                    }
                    break;
                }
            }

            if (!noteAlreadyExists) {
                NoteEntry newEntry = new NoteEntry(number, note);
                noteEntries.add(newEntry);
                Collections.sort(noteEntries);
            }

            writeNotesToJson(filePath, noteEntries);

        } catch (IOException e) {
            System.err.println("Erreur lors de la modification du fichier" + FILE_NAME + " : " + e.getMessage());
        }
    }


    public static void deleteNoteIfExists(int number, String chemin) {
        try {
            Path filePath = Paths.get(chemin + "/" + FILE_NAME);

            List<NoteEntry> noteEntries = readNotesFromJson(filePath);

            boolean noteDeleted = false;

            // Supprime l'entrée correspondante au numéro
            for (NoteEntry entry : noteEntries) {
                if (entry.getNumber() == number) {
                    noteEntries.remove(entry);
                    noteDeleted = true;
                    break;
                }
            }

            if (noteDeleted) {
                // Écrivez la liste mise à jour dans le fichier existant
                writeNotesToJson(filePath, noteEntries);

                System.out.println("Note supprimée avec succès.");
            } else {
                System.out.println("Aucune note trouvée avec le numéro " + number + ".");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de la note dans le fichier \"" + FILE_NAME + "\": "
                    + e.getMessage());
        }
    }

    public static boolean checkAndDeleteEmptyNotesFile(String path) {
        try {
            Path filePath = Paths.get(path, FILE_NAME);

            // Vérifier si le fichier existe et n'est pas vide
            if (Files.exists(filePath) && Files.size(filePath) == 2) {
                Files.delete(filePath); // Supprimer le fichier vide
                System.out.println("Le fichier \"" + FILE_NAME + "\" était vide et a été supprimé.");
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la vérification et suppression du fichier \"" + FILE_NAME + "\": "
                    + e.getMessage());
            return false;
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
        filePath = filePath.toAbsolutePath();

        if (Files.exists(filePath) && Files.size(filePath) > 0) {
            List<String> jsonLines = Files.readAllLines(filePath);
            StringBuilder jsonString = new StringBuilder();
            for (String line : jsonLines) {
                jsonString.append(line);
            }

            // Supprimer tout ce qui se trouve à l'extérieur des crochets
            int startBracket = jsonString.indexOf("[");
            int endBracket = jsonString.lastIndexOf("]");
            if (startBracket != -1 && endBracket != -1) {
                jsonString.delete(0, startBracket);
                jsonString.delete(endBracket - startBracket + 1, jsonString.length());
            }

            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(jsonString.toString()));
            reader.setLenient(true);

            noteEntries = gson.fromJson(reader, new TypeToken<List<NoteEntry>>() {
            }.getType());
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

        // Écrire le contenu JSON dans le fichier avec un format correct
        Files.write(filePath, jsonContent.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Fichier \"" + FILE_NAME + "\" mis à jour avec succès.");
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

    public void setNumber(int number) {
        this.number = number;
    }

    public List<String> getNotes() {
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
