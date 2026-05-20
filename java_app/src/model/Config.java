package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private File inputFile = null;
    private File outputFile = null;
    private String algorithm = "";
    private boolean showIds = false;
    private boolean showEdgeNames = false;
    private boolean showEdgeWeights = false;

    public void setInputFile(File file) {
        inputFile = file;
    }

    public void setOutputFile(File file) {
        outputFile = file;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public boolean getShowEdgeWeights() {
        return showEdgeWeights;
    }

    public void setShowEdgeWeights(boolean showEdgeWeights) {
        this.showEdgeWeights = showEdgeWeights;
    }

    public boolean getShowEdgeNames() {
        return showEdgeNames;
    }

    public void setShowEdgeNames(boolean showEdgeNames) {
        this.showEdgeNames = showEdgeNames;
    }

    public boolean getShowIds() {
        return showIds;
    }

    public void setShowIds(boolean showIds) {
        this.showIds = showIds;
    }
}
