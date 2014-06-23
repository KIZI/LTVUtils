package eu.linkedtv.utils.files;

import java.io.File;

public class DirectoryReader {
    
    public static File[] readDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        
        return directory.listFiles(new VisibleFileFilter());
    }

}
