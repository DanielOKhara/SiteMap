package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class FileCreator {
    TreeSet<String> siteMap;
    TreeSet<String> fileLinks;
    CopyOnWriteArraySet<String> parsedMap;
    String mainLink;
    public FileCreator (CopyOnWriteArraySet<String> map, String mainLink){
        siteMap = new TreeSet<>(Comparator.comparing(o -> o.replaceAll("\t", "")));
        fileLinks = new TreeSet<>();
        parsedMap = map;
        this.mainLink = mainLink;
        createCollections();
        createFile();
    }
    private void createCollections() {
        for(String url : parsedMap) {
            String[] parts = url.replace(mainLink, "").split("/");
            if(Objects.equals(parts[0], "")) {
                siteMap.add(url);
            } else if (url.contains("zzzFile - ")) {
                siteMap.add(url);
            } else {
                int level = url.replace(mainLink, "").split("/").length;
                siteMap.add("\t".repeat(level) + url);
            }
        }
    }
    private void createFile(){
        String name = mainLink.replaceAll("(http|https)://", "")
                .split("\\.")[0];
        String directory = "app/data/";
        File data = new File(directory);
        if(!data.exists()){
            data.mkdir();
        }
        File file = new File(directory + name + ".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.write(Paths.get(directory + name + ".txt"), siteMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
