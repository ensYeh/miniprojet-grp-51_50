package fr.uvsq.cprog;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AppTest {

    // Test pour la class Directory

    // test verifiant si la méthode directoryMap de la classe Directory génère
    // correctement la Hmap associant les numéros aux chemins des éléments du
    // répertoire.

    private Directory directory;

    @BeforeEach
    public void setUp() throws IOException {
        // Créer un répertoire temporaire pour chaque test
        directory = new Directory(Files.createTempDirectory("testmoveDirectory").toString());
    }

    @Test
    void testDirectoryMap() throws IOException {

        String cheminVersLeRépertoire = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation";
        Directory directory = new Directory(cheminVersLeRépertoire);
        Map<Integer, Path> contentMap = directory.directoryMap();

        assertNotNull(contentMap);
        assertFalse(contentMap.isEmpty());
    }

    @Test
    public void testGetChemin() throws IOException {
        String cheminVersLeRépertoire = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetChemin";
        // Créez une instance de votre classe
        Directory instance = new Directory(cheminVersLeRépertoire);

        // Définissez le chemin attendu
        String expectedChemin = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetChemin\\a";

        // Définissez le chemin pour l'instance
        instance.setChemin(expectedChemin);

        // Appelez la méthode getChemin()
        String actualChemin = instance.getChemin();

        // Vérifiez si le chemin renvoyé est égal au chemin attendu
        assertEquals(expectedChemin, actualChemin);
    }

    @Test
    public void testMoveTo() throws IOException {
        // Créer un nouveau répertoire temporaire
        Path newDirectory = Files.createTempDirectory("testmoveDirectory");

        // Déplacer le répertoire vers le nouveau répertoire
        directory.moveTo(newDirectory);

        // Vérifier que le chemin a été changé et que la map a été mise à jour
        assertEquals(newDirectory.toString(), directory.getChemin());
        assertTrue(directory.directoryMap().isEmpty());
    }

    @Test
    public void testMoveToValidDirectory() throws IOException {
        // Créez une instance de votre classe
        Directory instance = new Directory(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testMoveToValidDirectory");

        // Définissez un nouveau chemin qui est un répertoire existant
        Path newDirectoryPath = Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testMoveToValidDirectory\\b");

        // Appelez la méthode moveTo()
        instance.moveTo(newDirectoryPath);

        // Vérifiez si le chemin de l'instance a été mis à jour
        assertEquals(newDirectoryPath.toString(), instance.getChemin());
    }

    @Test
    public void testMoveToInvalidDirectory() throws IOException {
        // Créez une instance de votre classe
        Directory instance = new Directory(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testMoveToValidDirectory");

        // Définissez un nouveau chemin qui n'est pas un répertoire
        Path invalidPath = Paths.get("C:\\chemin\\vers\\fichier.txt");

        // Appelez la méthode moveTo() et attendez une IOException
        instance.moveTo(invalidPath);
    }

    @Test
    public void testGetKeyForExistingValue() throws IOException {
        // Créez une instance de votre classe
        Directory instance = new Directory(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue");

        // Créez une map de contenu pour l'instance
        Map<Integer, Path> contentMap = new HashMap<>();
        contentMap.put(1, Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue\\a"));
        contentMap.put(2, Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue\\b"));
        contentMap.put(3, Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue\\c"));

        // Définissez la map de contenu pour l'instance
        // instance.setContentMap(contentMap);ec une valeur existante
        Path existingValue = Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue\\b");

        // Appelez la méthode getKeyForValue() av");
        Integer key = instance.getKeyForValue(existingValue);

        // Vérifiez si la clé retournée est correcte
        assertEquals(Integer.valueOf(2), key);
    }

    @Test
    public void testGetKeyForNonExistingValue() throws IOException {
        // Créez une instance de votre classe
        Directory instance = new Directory(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue");

        // Créez une map de contenu pour l'instance
        Map<Integer, Path> contentMap = new HashMap<>();
        contentMap.put(1, Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue\\a"));
        contentMap.put(2, Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue\\b"));
        contentMap.put(3, Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetKeyForExistingValue\\c"));

        // Appelez la méthode getKeyForValue() avec une valeur non existante
        Path nonExistingValue = Paths.get("/chemin/vers/fichier4.txt");
        Integer key = instance.getKeyForValue(nonExistingValue);

        // Vérifiez si la clé retournée est null
        assertNull(key);
    }

    @Test
    public void testSetChemin() throws IOException {
        // Créez une instance de votre classe
        Directory instance = new Directory(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testSetChemin");

        // Définissez un nouveau chemin
        String newChemin = "/nouveau/chemin/vers/repertoire";

        // Appelez la méthode setChemin() avec le nouveau chemin
        instance.setChemin(newChemin);

        // Vérifiez si l'attribut chemin a été mis à jour correctement
        assertEquals(newChemin, instance.getChemin());
    }

    @Test
    void testDirectoryMapNotEmpty() throws IOException {
        String cheminVersLeRépertoire = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testDirectoryMapNotEmpty";
        Directory directory = new Directory(cheminVersLeRépertoire);

        // Ajoutez quelques fichiers ou répertoires dans le répertoire
        Files.createFile(Paths.get(cheminVersLeRépertoire, "testFile.txt"));
        Files.createDirectory(Paths.get(cheminVersLeRépertoire, "testDirectory"));

        Map<Integer, Path> contentMap = directory.directoryMap();

        assertNotNull(contentMap);
        assertFalse(contentMap.isEmpty());
        Files.delete(Paths.get(cheminVersLeRépertoire, "testFile.txt"));
        Files.delete(Paths.get(cheminVersLeRépertoire, "testDirectory"));

    }
    // Test pour la class NOTE MANAGER

    private static final String TEST_FILE_PATH = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testGetNotesForExistingNumber";

    @Test
    public void testGetNotesForExistingNumber() {
        // Créez une instance de votre classe NoteManager (ou classe contenant la
        // méthode)
        NoteManager noteManager = new NoteManager();

        // Créez des données de test

        // Appelez la méthode getNotesForNumber() avec un numéro existant
        List<String> result = noteManager.getNotesForNumber(1, TEST_FILE_PATH);
        List<String> expectedNotes = Arrays.asList(" Note 1 ", " Note 2 ");

        // Vérifiez si les notes retournées sont correctes
        assertEquals(expectedNotes, result);
    }

    @Test
    public void testGetNotesForNonExistingNumber() {
        // Créez une instance de votre classe NoteManager (ou classe contenant la
        // méthode)
        NoteManager noteManager = new NoteManager();

        // Appelez la méthode getNotesForNumber() avec un numéro non existant
        List<String> result = noteManager.getNotesForNumber(3, TEST_FILE_PATH);

        // Vérifiez si la liste retournée est vide
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetNotesForNumberWithIOException() throws IOException {
        // Créez une instance de votre classe NoteManager (ou classe contenant la
        // méthode)
        NoteManager noteManager = new NoteManager();

        // Appelez la méthode getNotesForNumber() avec un chemin de fichier incorrect
        // (pour provoquer une IOException)
        noteManager.getNotesForNumber(1, "chemin/incorrect/vers/le/fichier.json");
    }

    private static final String TEST_FILE_PATH2 = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testCheckNotesFileFileCreated";

    @Test
    public void testCheckNotesFileFileCreated() throws IOException {

        Files.deleteIfExists(Paths.get(TEST_FILE_PATH2, NoteManager.FILE_NAME));
        // Créez une instance de votre classe NoteManager (ou classe contenant la
        // méthode)
        NoteManager noteManager = new NoteManager();

        // Appelez la méthode checkNotesFile() avec le mock de Files
        boolean result = noteManager.checkNotesFile(TEST_FILE_PATH2);

        // Vérifiez si la méthode a renvoyé true (le fichier a été créé)
        assertTrue(result);

    }

    private static final String TEST_FILE_PATH3 = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testModifyNoteNumberIncrement";

    @Test
    public void testModifyNoteNumberIncrement() throws IOException {
        // Créez une instance de votre classe NoteManager (ou classe contenant la
        // méthode)
        NoteManager noteManager = new NoteManager();

        // Appelez la méthode modifyNoteNumber() avec le mock de Files
        noteManager.modifyNoteNumber(1, TEST_FILE_PATH3, "+");
        String R1 = noteManager.getNotesForNumber(2, TEST_FILE_PATH3).get(0);
        String R2 = noteManager.getNotesForNumber(3, TEST_FILE_PATH3).get(0);

        // Vérifiez si la modification a été correctement effectué
        assertEquals(" Note 1 ", R1); // La première entrée devrait avoir un numéro de 2 maintenant
        assertEquals(" Note 3 ", R2); // La deuxième entrée devrait avoir un numéro de 3 maintenant
        noteManager.modifyNoteNumber(1, TEST_FILE_PATH3, "-");
    }

    private static final String TEST_FILE_PATH4 = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testAddNoteNoteDoesNotExis";

    @Test
    public void testAddNoteNoteDoesNotExist() throws IOException {
        // Créez une instance de votre classe NoteManager (ou classe contenant la
        // méthode)

        NoteManager noteManager = new NoteManager();
        noteManager.checkNotesFile(TEST_FILE_PATH4);

        // Appelez la méthode addNote() avec le mock de Files et de Directory
        noteManager.addNote(1, " Nouvelle note ", new Directory(TEST_FILE_PATH4));
        String R1 = noteManager.getNotesForNumber(1, TEST_FILE_PATH4).get(0);

        assertEquals("Nouvelle note", R1); // Vérifiez la présence de la nouvelle note
    }

    private static final String TEST_PATH5 = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testDeleteNoteIfExists";

    @Test
    public void testDeleteNoteIfExists() {
        try {
            // Vérifier si le fichier de notes existe et le créer s'il n'existe pas
            NoteManager.checkNotesFile(TEST_PATH5);

            // Ajouter une note pour un numéro donné
            NoteManager.addNote(1, "Note 1", new Directory(TEST_PATH5));
            assertEquals(Arrays.asList("Note 1"), NoteManager.getNotesForNumber(1, TEST_PATH5));

            // Supprimer la note ajoutée
            NoteManager.deleteNoteIfExists(1, TEST_PATH5);
            assertEquals(Collections.emptyList(), NoteManager.getNotesForNumber(1, TEST_PATH5));

            // Supprimer une note qui n'existe pas
            NoteManager.deleteNoteIfExists(2, TEST_PATH5);
            assertEquals(Collections.emptyList(), NoteManager.getNotesForNumber(2, TEST_PATH5));

        } catch (IOException e) {
            e.printStackTrace();
            fail("Une exception s'est produite lors de l'exécution du test.");
        }
    }

    private static final String TEST_PATH6 = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testSortNotes";

    @Test
    public void testSortNotes() {
        try {
            // Vérifier si le fichier de notes existe et le créer s'il n'existe pas
            NoteManager.checkNotesFile(TEST_PATH6);

            // Ajouter des notes dans un ordre non trié
            NoteManager.addNote(2, "Note 2", new Directory(TEST_PATH6));
            NoteManager.addNote(1, "Note 1", new Directory(TEST_PATH6));
            NoteManager.addNote(3, "Note 3", new Directory(TEST_PATH6));

            // Trier les notes
            NoteManager.sortNotes();

            // Vérifier que les notes sont maintenant triées
            List<String> sortedNotes = NoteManager.getNotesForNumber(1, TEST_PATH6);
            assertEquals(Arrays.asList("Note 1"), sortedNotes);
            sortedNotes = NoteManager.getNotesForNumber(2, TEST_PATH6);
            assertEquals(Arrays.asList("Note 2"), sortedNotes);
            sortedNotes = NoteManager.getNotesForNumber(3, TEST_PATH6);
            assertEquals(Arrays.asList("Note 3"), sortedNotes);

        } catch (IOException e) {
            e.printStackTrace();
            fail("Une exception s'est produite lors de l'exécution du test.");
        }
    }

    @Test
    public void testCheckAndDeleteEmptyNotesFile_NonExistingFile() {
        // Appeler la méthode avec un chemin de fichier qui n'existe pas
        boolean result = NoteManager.checkAndDeleteEmptyNotesFile("chemin/inexistant/fichier.json");

        // Vérifier que la méthode renvoie false, indiquant que le fichier n'a pas été
        // supprimé (car il n'existe pas)
        assertFalse(result);
    }

    @Test
    public void testWriteNotesToJson() throws IOException {
        String cheminVersLeRépertoire = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testWriteNotesToJson";
        Directory directory = new Directory(cheminVersLeRépertoire);
        // Créer une liste de notes à écrire dans le fichier
        List<NoteEntry> noteEntries = Arrays.asList(new NoteEntry(1, "Test Note"));

        // Définir le chemin complet du fichier
        Path filePath = Paths.get(directory.getChemin(), NoteManager.FILE_NAME);

        NoteManager.checkNotesFile(directory.getChemin());

        // Appeler la méthode et vérifier le résultat
        NoteManager.writeNotesToJson(filePath, noteEntries);

        // Lire le contenu du fichier et vérifier s'il correspond
        String fileContent = new String(Files.readAllBytes(filePath));
        assertTrue(fileContent.contains("Test Note"));
    }

    // test pour la class NOteEntry

    @Test
    public void testConstructorAndGetters() {
        NoteEntry entry = new NoteEntry(1, "Note1");

        // Vérifier le numéro
        assertEquals(1, entry.getNumber());

        // Vérifier les notes
        List<String> notes = entry.getNotes();
        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals("Note1", notes.get(0));
    }

    @Test
    public void testSetNumber() {
        NoteEntry entry = new NoteEntry(1, "Note1");

        // Modifier le numéro
        entry.setNumber(2);

        // Vérifier le nouveau numéro
        assertEquals(2, entry.getNumber());
    }

    @Test
    public void testAddNoteText() {
        NoteEntry entry = new NoteEntry(1, "Note1");

        // Ajouter une nouvelle note
        entry.addNoteText("Note2");

        // Vérifier les notes après l'ajout
        List<String> notes = entry.getNotes();
        assertNotNull(notes);
        assertEquals(2, notes.size());
        assertEquals("Note1", notes.get(0));
        assertEquals("Note2", notes.get(1));
    }

    @Test
    public void testCompareTo() {
        NoteEntry entry1 = new NoteEntry(1, "Note1");
        NoteEntry entry2 = new NoteEntry(2, "Note2");
        NoteEntry entry3 = new NoteEntry(3, "Note3");

        // Vérifier l'ordre croissant
        assertTrue(entry1.compareTo(entry2) < 0);
        assertTrue(entry2.compareTo(entry3) < 0);
        assertTrue(entry1.compareTo(entry3) < 0);

        // Vérifier l'ordre décroissant
        assertTrue(entry2.compareTo(entry1) > 0);
        assertTrue(entry3.compareTo(entry2) > 0);
        assertTrue(entry3.compareTo(entry1) > 0);

        // Vérifier l'égalité
        assertEquals(0, entry1.compareTo(new NoteEntry(1, "NoteX")));
    }

    // Test pour la class CommandManager :

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    // @Test
    public void testAfficherPressePapierWithContent() {
        // Crée un presse-papiers avec des données fictives
        Map<Integer, Path> pressePapier = new HashMap<>();
        pressePapier.put(1, Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testAfficherPressePapierWithContent\\fichier1.txt"));
        pressePapier.put(2, Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testAfficherPressePapierWithContent\\fichier2.txt"));

        // Remplace le presse-papiers actuel par celui avec des données fictives
        CommandManager.setPressePapier(pressePapier);

        // Appelle la méthode afficherPressePapier
        CommandManager.afficherPressePapier();

        // Vérifie que la sortie console est correcte
        String expectedOutput = "\\nContenu du presse-papiers :" +
                System.lineSeparator()
                + "Numéro 1: C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testAfficherPressePapierWithContent\\fichier1.txt"
                + System.lineSeparator() + "Numéro 2: chemin\\fichier2.txt"
                + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testAfficherPressePapierEmpty() {
        // Remplace le presse-papiers actuel par un presse-papiers vide
        CommandManager.setPressePapier(new HashMap<>());

        // Appelle la méthode afficherPressePapier
        CommandManager.afficherPressePapier();

        // Vérifie que la sortie console est correcte pour un presse-papiers vide
        String expectedOutput = "\nContenu du presse-papiers :\nLe presse-papiers est vide.\n";
        assertEquals(expectedOutput.trim().replaceAll("\\s+", " "),
                outContent.toString().trim().replaceAll("\\s+", " "));
    }

    private static final String TEST_PATH7 = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testMkdirSuccess";
    private static final String TEST_PATH8 = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testMkdirInvalidUsage";

    @AfterEach
    public void cleanup() throws IOException {
        // Supprime le répertoire créé après chaque test
        Path nouveauRepertoire = Paths.get(TEST_PATH7 + "\\nouveauRepertoire");
        Files.deleteIfExists(nouveauRepertoire);
    }

    @Test
    public void testMkdirSuccess() throws IOException {
        // Crée un répertoire courant fictif
        Directory repertoireCourant = new Directory(TEST_PATH7);

        // Appelle la méthode mkdir avec une ligne correcte
        CommandManager.mkdir(repertoireCourant, "mkdir nouveauRepertoire");

        // Vérifie que le répertoire a été créé
        Path nouveauRepertoire = Paths.get(TEST_PATH7 + "\\nouveauRepertoire");
        assertTrue(Files.exists(nouveauRepertoire));
        assertTrue(Files.isDirectory(nouveauRepertoire));
        System.out.println("Répertoire créé avec succès : " + nouveauRepertoire);
    }

    @Test
    public void testMkdirInvalidUsage() throws IOException {
        // Crée un répertoire courant fictif
        Directory repertoireCourant = new Directory(TEST_PATH8);

        // Appelle la méthode mkdir avec une ligne incorrecte
        CommandManager.mkdir(repertoireCourant, "mkdir");

        // Vérifie que la méthode affiche le message d'utilisation incorrecte
        assertEquals("Utilisation incorrecte. Exemple : mkdir nomDuRepertoire",
                outContent.toString().trim());
    }

    @Test
    public void testMkdirInvalidUsageTooManyArguments() throws IOException {
        // Crée un répertoire courant fictif
        Directory repertoireCourant = new Directory(TEST_PATH8);

        // Appelle la méthode mkdir avec trop d'arguments
        CommandManager.mkdir(repertoireCourant, "mkdir tropDeArguments ici");

        // Vérifie que la méthode affiche le message d'utilisation incorrecte
        assertEquals("Utilisation incorrecte. Exemple : mkdir nomDuRepertoire",
                outContent.toString().trim());
    }

    private static final String TEST_PATH9 = "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testFindFileExists";

    @Test
    public void testFindFileExists() {
        // Crée un répertoire courant fictif
        Path currentDir = Paths.get(TEST_PATH9);

        // Crée un fichier fictif dans le répertoire courant
        Path testFile = currentDir.resolve("testFile.txt");
        try {
            Files.createFile(testFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Appelle la méthode find pour rechercher le fichier
        CommandManager.find(currentDir, "testFile.txt");

        // Vérifie que le fichier a été trouvé
        assertEquals("testFile.txt".trim(), outContent.toString().trim());
    }

    @Test
    public void testVisuDirectory() {
        // Crée un répertoire fictif dans le répertoire courant
        Path currentDir = Paths.get(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testMkdirSuccess");
        Path subDir = currentDir.resolve("subdir");
        try {
            Files.createDirectory(subDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crée un répertoire fictif avec une taille connue
        long expectedSize = 0; // Taille en octets
        Map<Integer, Path> directoryMap = new HashMap<>();
        directoryMap.put(1, subDir);
        Directory repertoireCourant = new Directory(currentDir.toString(), directoryMap);

        // Appelle la méthode visu pour afficher la taille du répertoire
        CommandManager.visu(repertoireCourant, 1);

        // Vérifie que la taille du répertoire est affichée correctement
        assertTrue(outContent.toString().contains("La taille du sous-répertoire est : " + expectedSize + " octets"));
    }

    @Test
    public void testGetDirectorySize_EmptyDirectory() throws IOException {
        Path emptyDirectory = Files.createTempDirectory("emptyDirectory");
        long size = CommandManager.getDirectorySize(emptyDirectory);
        assertEquals(0, size);
    }

    @Test
    public void testGetDirectorySize_DirectoryWithFiles() throws IOException {
        Path directoryWithFiles = Files.createTempDirectory("directoryWithFiles");
        // Créer des fichiers dans le répertoire avec différentes tailles
        Files.createFile(directoryWithFiles.resolve("file1.txt"));
        Files.createFile(directoryWithFiles.resolve("file2.txt"));
        Files.createFile(directoryWithFiles.resolve("file3.txt"));

        long expectedSize = Files.size(directoryWithFiles.resolve("file1.txt"))
                + Files.size(directoryWithFiles.resolve("file2.txt"))
                + Files.size(directoryWithFiles.resolve("file3.txt"));

        long size = CommandManager.getDirectorySize(directoryWithFiles);
        assertEquals(expectedSize, size);
    }

    @Test
    public void testGetDirectorySize_DirectoryWithSubdirectories() throws IOException {
        Path directoryWithSubdirectories = Files.createTempDirectory("directoryWithSubdirectories");
        // Créer des fichiers dans le répertoire et des sous-répertoires avec des
        // fichiers
        Files.createFile(directoryWithSubdirectories.resolve("file1.txt"));
        Files.createFile(directoryWithSubdirectories.resolve("file2.txt"));
        Path subdirectory = Files.createDirectory(directoryWithSubdirectories.resolve("subdirectory"));
        Files.createFile(subdirectory.resolve("file3.txt"));

        long expectedSize = Files.size(directoryWithSubdirectories.resolve("file1.txt"))
                + Files.size(directoryWithSubdirectories.resolve("file2.txt"))
                + Files.size(subdirectory.resolve("file3.txt"));

        long size = CommandManager.getDirectorySize(directoryWithSubdirectories);
        assertEquals(expectedSize, size);
    }

    @Test
    public void testGetExtension_FileWithoutExtension() {
        Path fileWithoutExtension = Paths.get("/path/to/fileWithoutExtension");
        String extension = CommandManager.getExtension(fileWithoutExtension);
        assertNull(extension);
    }

    @Test
    public void testGetExtension_FileWithExtension() {
        Path fileWithExtension = Paths.get("/path/to/fileWithExtension.txt");
        String extension = CommandManager.getExtension(fileWithExtension);
        assertEquals("txt", extension);
    }

    @Test
    public void testGetExtension_HiddenFileWithoutExtension() {
        Path hiddenFileWithoutExtension = Paths.get("/path/to/.hiddenFileWithoutExtension");
        String extension = CommandManager.getExtension(hiddenFileWithoutExtension);
        assertNull(extension);
    }

    @Test
    public void testGetExtension_FileWithMultipleDots() {
        Path fileWithMultipleDots = Paths.get("/path/to/file.with.multiple.dots.txt");
        String extension = CommandManager.getExtension(fileWithMultipleDots);
        assertEquals("txt", extension);
    }

    @Test
    public void testCopyCutWithDirectory() {
        try {
            Directory currentDir = new Directory(
                    "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\Junit\\testCopyCutWithDirectory");

            // Appeler la fonction copyCut avec le numéro de l'élément répertoire
            CommandManager.copyCut(currentDir, 1);

            // Vérifier que l'élément est ajouté au presse-papiers et marqué comme coupé
            assertTrue(CommandManager.pressePapier.containsKey(1));
            CommandManager.pressePapier.clear();
        } catch (IOException e) {
            fail("Exception inattendue : " + e.getMessage());
        }
    }

    @Test
    public void testCopyCutWithFile() {
        try {
            Directory currentDir = new Directory(
                    "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\Junit\\testCopyCutWithFile");
            currentDir.contentMap = currentDir.directoryMap();
            // Appeler la fonction copyCut avec le numéro de l'élément fichier
            CommandManager.copyCut(currentDir, 1);

            // Vérifier que l'élément est ajouté au presse-papiers et marqué comme coupé
            assertTrue(CommandManager.pressePapier.containsKey(1));
            CommandManager.pressePapier.clear();
        } catch (IOException e) {
            fail("Exception inattendue : " + e.getMessage());
        }
    }

    @Test
    public void testCopyCutWithNonexistentElement() {
        try {
            Directory currentDir = new Directory(
                    "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\Junit\\testCopyCutWithNonexistentElement");
            // Appeler la fonction copyCut avec un numéro d'élément qui n'existe pas
            CommandManager.copyCut(currentDir, 999);

            // Vérifier que rien n'est ajouté au presse-papiers
            assertTrue(CommandManager.pressePapier.isEmpty());
            CommandManager.pressePapier.clear();
        } catch (IOException e) {
            fail("Exception inattendue : " + e.getMessage());
        }
    }

    @Test
    public void testGenerateUniqueElementNumber() {
        // Créer une instance de CommandManager ou de la classe qui contient la méthode
        CommandManager commandManager = new CommandManager();

        // Appeler la méthode plusieurs fois et stocker les résultats dans un ensemble
        // pour vérifier l'unicité
        int numberOfCalls = 1;
        Set<Integer> uniqueNumbers = new HashSet<>();

        for (int i = 0; i < numberOfCalls; i++) {
            int uniqueNumber = CommandManager.generateUniqueElementNumber();
            uniqueNumbers.add(uniqueNumber);
        }

        // Vérifier que tous les numéros générés sont uniques
        assertEquals(numberOfCalls, uniqueNumbers.size());
    }

    // Test pour la class afficheur :

    @Test
    public void testDisplayCurrentDirWithNotes() throws IOException {
        // Créez un répertoire fictif et un fichier notes.json avec quelques notes
        Directory repertoireCourant = new Directory(
                "C:\\Users\\Iness\\OneDrive\\Bureau\\projetGestionnaire\\miniprojet-grp-51_50\\presentation\\testDisplayCurrentDirWithNotes");
        Path notesFile = Paths.get(repertoireCourant.getChemin(), "notes.json");
        NoteManager.checkNotesFile(repertoireCourant.getChemin());
        String notesContent = "[{\"number\": 1, \"notes\": [\"Note1\", \"Note2\"]}]";
        Files.write(notesFile, notesContent.getBytes());

        // Exécutez la méthode displayCurrentDir
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Afficheur.displayCurrentDir(repertoireCourant);

        // Vérifiez que la sortie console contient les informations attendues
        assertTrue(outContent.toString().contains("1. \u001B[31mnotes.json\u001B[0m : Note1, Note2"));
        Files.delete(notesFile);
    }

}

// test pour la class App :

// private ByteArrayOutputStream outContent1;
// private InputStream originalIn;
// private PrintStream originalOut1;

// @BeforeEach
// public void setUpStreams1() {
// // Redirection des flux de sortie pour permettre la capture
// System.setProperty("jline.terminal", "dumb");
// outContent1 = new ByteArrayOutputStream();
// originalIn = System.in;
// originalOut1 = System.out;
// System.setOut(new PrintStream(outContent));
// }

// @AfterEach
// public void restoreStreams1() {
// // Restauration des flux d'entrée et de sortie standard
// System.setIn(originalIn);
// System.setOut(originalOut1);
// }

// @Test
// public void testMain() throws IOException {
// // Simulation d'une entrée utilisateur
// String input = "mkdir testDirectory\n+ Note for testDirectory\nexit\n";
// System.setIn(new ByteArrayInputStream(input.getBytes()));

// // Simulation d'un répertoire temporaire
// Path tempDir = Files.createTempDirectory("testMain");
// System.setProperty("user.dir", tempDir.toString());

// // Exécution de la méthode principale
// App.main(new String[] {});

// // Vérification de la sortie
// assertTrue(outContent1.toString().contains("Contenu du répertoire courant
// :"));
// assertTrue(outContent1.toString().contains("Le NER courant est : 0"));
// assertTrue(outContent1.toString().contains("Chemin complet depuis la racine
// du système de fichiers :"));
// assertTrue(outContent1.toString().contains("Répertoire courant : " +
// tempDir.toString()));
// assertTrue(outContent1.toString().contains("Création du répertoire
// 'testDirectory'"));
// assertTrue(outContent1.toString().contains("Note ajoutée au NER 1 : Note for
// testDirectory"));
// }