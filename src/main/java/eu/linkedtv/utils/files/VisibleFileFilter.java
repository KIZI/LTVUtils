package eu.linkedtv.utils.files;

import java.io.File;
import java.io.FileFilter;

public class VisibleFileFilter implements FileFilter {

    public boolean accept(File pathname) {
        return !pathname.getName().startsWith(".");
    }
    
}
