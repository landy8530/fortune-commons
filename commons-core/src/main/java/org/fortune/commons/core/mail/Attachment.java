package org.fortune.commons.core.mail;

import java.io.File;

/**
 * @author landyl
 * @create 3:53 PM 03/24/2020
 */
public class Attachment {
    private String name;//file name
    private File file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
