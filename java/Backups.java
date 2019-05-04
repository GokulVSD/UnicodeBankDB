import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Scanner;

import dbdatabase.DB;
import Huffman.*;

public class Backups {

    private static String homeDir = System.getProperty("user.home");
    private static String backdir = homeDir + File.separator + "Documents" + File.separator + "DBDBackups";
    private File curdir;

    public Backups(){

        while (true) {
            if (!DB.isIndexBeingUsed()) {
                DB.useIndex();
                break;
            }
            waiting();
        }

        DB.customersCurrentlyBeingUsed = null;
        waiting();

        curdir= new File(homeDir + File.separator + "Documents" + File.separator + "DBDatabase");
        File fd = new File(backdir);
        if(!fd.exists()){
            fd.mkdirs();
        }
    }

    private void waiting(){
        try {
            Thread.sleep(200);
        } catch (Exception e){}
    }

    public void makeBackup(){
        String time = "" + LocalDateTime.now();
        String newbackdir = backdir + File.separator + time.substring(0,time.indexOf("T")) + " " + time.substring(time.indexOf("T") + 1,time.lastIndexOf(".")).replace(':','_');
        File fd = new File(newbackdir);
        if(!fd.exists()){
            fd.mkdirs();
        }
        for(File file: curdir.listFiles()){
            fd = new File(newbackdir,file.getName());
            try {
                fd.createNewFile();
            } catch (Exception e){
                e.printStackTrace();
            }
            BinaryStdIn.setInputFile(file);
            BinaryStdOut.setOutputFile(fd);
            Huffman.compress();
        }
    }

    public void restoreFromBackup(String name){
        File newbackdir = new File(backdir + File.separator + name);
        File fd;
        for(File file: curdir.listFiles()){
            file.delete();
        }
        for(File file: newbackdir.listFiles()){
            fd = new File(curdir,file.getName());
            try {
                fd.createNewFile();
            } catch (Exception e){
                e.printStackTrace();
            }
            BinaryStdIn.setInputFile(file);
            BinaryStdOut.setOutputFile(fd);
            Huffman.expand();
        }
    }

    static LinkedList<String > getStateTransferNames(){
        LinkedList<String > names = new LinkedList<>();
        try {
            File f = new File(backdir);
            for (File file : f.listFiles()) {
                names.add(file.getName());
            }
            if (names.size() == 0) return null;
            return names;
        } catch (Exception e){
            return null;
        }
    }

    public void save(){
        DB.doneUsingIndex();
        DB.customersCurrentlyBeingUsed = new LinkedList<>();
    }
}
