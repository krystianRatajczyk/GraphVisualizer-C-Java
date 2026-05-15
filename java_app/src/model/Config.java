package model;

import java.io.File;

public class Config {
    private File inputFile = null;
    private File outputFile = null;
    private String algorithm = "";

    public void setInputFile(File file){
        inputFile = file;
    }

    public void setOutputFile(File file) {
        outputFile = file;
    }
    public void setAlgorithm(String algorithm){
        this.algorithm = algorithm;
    }
    public File getInputFile(){
        return inputFile;
    }

    public File getOutputFile(){
        return outputFile;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
