package fr.uvsq.cprog;

import java.util.ArrayList;
import java.util.List;
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
