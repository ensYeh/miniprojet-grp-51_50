package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Directory{

    Map<Integer,Path> contentMap;
    private String chemin;
    public Directory(String chemin) throws IOException {
        this.chemin = chemin;
        this.contentMap = directoryMap();
    }

    public Map<Integer, Path> directoryMap() throws IOException {

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(this.getChemin()))) {
            List<Path> elements = new ArrayList<>();
            for (Path path : stream) {
                elements.add(path);
            }
            Collections.sort(elements);
            // Crée une map associant chaque numéro à son chemin correspondant
            return elements.stream()
                    .collect(Collectors.toMap(
                            path -> elements.indexOf(path) + 1,
                            Function.identity()
                    ));
        }
    }

    public String getChemin(){
        return this.chemin;
    }
    
    public void moveTo(Path nouveauChemin) throws IOException {
        this.chemin = nouveauChemin.toString();
        this.contentMap = directoryMap();
    }
    public Integer getKeyForValue(Path value) {
        System.out.println(value);
        for (Map.Entry<Integer, Path> entry : contentMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        // Retourner null si la valeur n'est pas trouvée
        return null;
    }

    
}






