package fr.uvsq.cprog;

public class FileElement {
    private String name;
    private String content;
    private int number;

    private boolean isDirectory;

    // Constructeur pour les fichiers texte
    public FileElement( int number ,String name, String content) {
        this.number = number;
        this.name = name;
        this.content = content;
        this.isDirectory = false;
    }

    // Constructeur pour les répertoires
    public FileElement(String name) {
        this.name = name;
        this.isDirectory = true;
    }

    // Vérifie si l'élément est un répertoire
    public boolean isDirectory() {
        return isDirectory;
    }

    // Getters et Setters pour le nom, le contenu et le numero associé au fichier 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    
}

    
    
