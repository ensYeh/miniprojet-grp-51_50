package fr.uvsq.cprog;
import java.io.File;

public class ElementFichier {
    protected File fichier;

    public ElementFichier(String chemin) {
        this.fichier = new File(chemin);
    }
    public String obtenirCheminComplet() {
        return fichier.getAbsolutePath();
    }
}

    

