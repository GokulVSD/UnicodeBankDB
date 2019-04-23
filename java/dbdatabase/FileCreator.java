package dbdatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

 public class FileCreator {

    private String fileName;
    protected String content;

    protected FileCreator(String fileName){

        this.fileName = fileName;
    }

    /**creates file with fileName in '[user's home directory]/Documents/DBDatabase/'
     * with content. if content is null, creates an empty file.
     */
    protected void createFile(){

        String homeDir = System.getProperty("user.home");
        String dir = homeDir + File.separator + "Documents" + File.separator + "DBDatabase";
        File fd = new File(dir);
        if(!fd.exists()){
            fd.mkdirs();
        }
        fd = new File(dir,fileName);
        FileWriter fw;
        try{
            fw = new FileWriter(fd);
            fw.write(content);
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
