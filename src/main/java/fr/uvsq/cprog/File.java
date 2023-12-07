package fr.uvsq.cprog;

public class File {
    private String name;
    private String content;
    private int number;


    // Constructeur pour les fichiers texte
    public File( int number ,String name, String content) {
        this.number = number;
        this.name = name;
        this.content = content; 
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

    
    
