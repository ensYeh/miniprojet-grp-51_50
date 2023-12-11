// package fr.uvsq.cprog;

// import java.io.IOException;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
// import java.util.Arrays;

// public class CommandProcessor {
//     private final Directory currentDir;
//     private final int currentElement;
//     private final String line;
//     private final String[] parts;

//     public CommandProcessor(Directory currentDir, int currentElement, String line, String[] parts) {
//         this.currentDir = currentDir;
//         this.currentElement = currentElement;
//         this.line = line;
//         this.parts = parts;
//     }

//     public void process() throws IOException {
//         if (parts.length == 1 && !processSingleCommand(parts[0])) {
//             System.out.println("Unknown command: " + line);
//         } else if (parts.length == 2 && !processTwoPartCommand(parts[0], parts[1])) {
//             System.out.println("Unknown command: " + line);
//         } else if (parts.length > 3 && !processMultiPartCommand()) {
//             System.out.println("Unknown command: " + line);
//         }
//     }

//     private boolean processSingleCommand(String command) throws IOException {
//         switch (command) {
//             case "create":
//                 System.out.println("Création de fichier/répertoire (TBD)");
//                 return true;
//             case "exit":
//                 System.exit(0);
//             default:
//                 return false;
//         }
//     }

//     private boolean processTwoPartCommand(String part1, String part2) throws IOException {
//         switch (part1) {
//             case "mkdir":
//                 CommandManager.mkdir(currentDir, part1 + " " + part2);
//                 return true;
//             case "exit":
//                 System.exit(0);
//             case "find":
//                 CommandManager.find(currentDir.contentMap.get(currentElement));
//                 return true;
//             default:
//                 return false;
//         }
//     }

//     private boolean processMultiPartCommand() throws IOException {
//         Pattern numeroPattern = Pattern.compile("[1-9][0-9]*");
//         Matcher numeroMatcher = numeroPattern.matcher(parts[0]);

//         if (numeroMatcher.matches()) {
//             int number = Integer.parseInt(parts[0]);
//             if (currentDir.contentMap.containsKey(number)) {
//                 int currentElement = number;
//                 System.out.println("Sélection de l'élément numéro " + currentElement);
//             } else {
//                 System.out.println("Le numéro donné n'est associé à aucun élément du répertoire");
//                 return false;
//             }
//             if (parts[1].equals("+")) {
//                 String str = line.split("\\+")[1];
//                 NoteManager.addNote(currentElement, str);
//             } else {
//                 System.out.println("Unknown command: " + line);
//             }

//             switch (parts[1]) {
//                 case "create":
//                     System.out.println("Création de fichier/répertoire (TBD)");
//                     break;
//                 case "mkdir":
//                     CommandManager.mkdir(currentDir, parts[1] + " " + parts[2]);
//                     break;
//                 case "exit":
//                     System.exit(0);
//                 default:
//                     return false;
//             }
//         } else {
//             return false;
//         }
//         return true;
//     }
// }
