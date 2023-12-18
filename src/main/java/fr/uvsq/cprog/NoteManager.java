
package fr.uvsq.cprog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestionnaire de notes qui fournit des méthodes pour interagir avec les
 * fichiers de notes.
 */

public class NoteManager {

    /**
     * Nom du fichier de notes JSON.
     */

    public static final String FILE_NAME = "notes.json";

    /**
     * Récupère les notes pour un numéro donné.
     *
     * @param number Le numéro de la note à récupérer.
     * @param path   Le chemin du répertoire dans lequel se trouve le fichier de
     *               notes.
     * @return La liste des notes pour le numéro donné.
     */

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

    /**
     * Vérifie si le fichier de notes existe déjà dans le répertoire actuel.
     *
     * @param path Le chemin du répertoire dans lequel se trouve le fichier de
     *             notes.
     * @return true si le fichier de notes a été créé, false sinon.
     */

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

    /**
     * Modifie le numéro de toutes les notes supérieures ou égales à X.
     *
     * @param X    Le numéro à partir duquel les notes doivent être modifiées.
     * @param path Le chemin du répertoire dans lequel se trouve le fichier de
     *             notes.
     * @param val  La valeur à ajouter ou à soustraire au numéro de la note.
     */

    public static void modifyNoteNumber(Integer X, String path, String val) {
        try {
            Path filePath = Paths.get(path, FILE_NAME);

            List<NoteEntry> noteEntries = readNotesFromJson(filePath);
            for (NoteEntry entry : noteEntries) {
                if (entry.getNumber() >= X) {
                    if (val.equals("+")) {
                        entry.setNumber(entry.getNumber() + 1);
                    } else if (val.equals("-")) {
                        entry.setNumber(entry.getNumber() - 1);
                    }
                }
            }

            writeNotesToJson(filePath, noteEntries);
        } catch (IOException e) {
            System.err.println("Erreur lors de la modification du fichier \"" + FILE_NAME + "\": " + e.getMessage());
        }
    }

    /**
     * Ajoute une note pour un numéro donné.
     *
     * @param number     Le numéro de la note à laquelle ajouter une note.
     * @param note       La note à ajouter.
     * @param currentDir Le répertoire actuel dans lequel se trouve le fichier de
     *                   notes.
     */

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

    /**
     * Supprime une note pour un numéro donné.
     *
     * @param number     Le numéro de la note à laquelle supprimer une note.
     * @param note       La note à supprimer.
     * @param currentDir Le répertoire actuel dans lequel se trouve le fichier de
     *                   notes.
     */

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

    /**
     * Supprime le fichier de notes s'il est vide.
     *
     * @param path Le chemin du répertoire dans lequel se trouve le fichier de
     *             notes.
     * @return true si le fichier de notes a été supprimé, false sinon.
     */

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

    /**
     * Trie les notes par numéro.
     */

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

    /**
     * Lit les entrées de notes depuis le fichier JSON et les retourne sous forme de
     * liste.
     *
     * @param filePath Le chemin du fichier JSON de notes.
     * @return Une liste d'objets NoteEntry.
     * @throws IOException En cas d'erreur lors de la lecture du fichier.
     */

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

    /**
     * Écrit les entrées de notes dans le fichier JSON.
     *
     * @param filePath    Le chemin du fichier JSON de notes.
     * @param noteEntries La liste d'objets NoteEntry à écrire dans le fichier.
     * @throws IOException En cas d'erreur lors de l'écriture dans le fichier.
     */

    public static void writeNotesToJson(Path filePath, List<NoteEntry> noteEntries) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonContent = gson.toJson(noteEntries);

        // Écrire le contenu JSON dans le fichier avec un format correct
        Files.write(filePath, jsonContent.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Fichier \"" + FILE_NAME + "\" mis à jour avec succès.");
    }

}

/**
 * Représente une entrée de note associée à un numéro et à une liste de notes.
 */

class NoteEntry implements Comparable<NoteEntry> {

    /**
     * Le numéro associé à cette entrée de note.
     */

    private int number;
    /**
     * La liste des notes associées à ce numéro.
     */
    private List<String> notes;

    /**
     * Constructeur de la classe NoteEntry.
     *
     * @param number Le numéro associé à cette entrée de note.
     * @param note   Le texte initial de la note.
     */

    public NoteEntry(int number, String note) {
        this.number = number;
        this.notes = new ArrayList<>();
        this.notes.add(note);
    }

    /**
     * Obtient le numéro associé à cette entrée de note.
     *
     * @return Le numéro associé à cette entrée de note.
     */

    public int getNumber() {
        return number;
    }

    /**
     * Modifie le numéro associé à cette entrée de note.
     *
     * @param number Le nouveau numéro à associer à cette entrée de note.
     */

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Obtient la liste des notes associées à ce numéro.
     *
     * @return La liste des notes associées à ce numéro.
     */

    public List<String> getNotes() {
        return notes;
    }

    /**
     * Ajoute une nouvelle note à la liste des notes associées à ce numéro.
     *
     * @param noteText Le texte de la nouvelle note à ajouter.
     */

    public void addNoteText(String noteText) {
        notes.add(noteText);
    }

    /**
     * Compare cette entrée de note à une autre en se basant sur les numéros.
     *
     * @param other L'autre entrée de note à comparer.
     * @return Une valeur négative, nulle ou positive si cette entrée est plus
     *         petite, égale ou plus grande que l'autre.
     */

    @Override
    public int compareTo(NoteEntry other) {
        return Integer.compare(this.number, other.number);
    }

}
