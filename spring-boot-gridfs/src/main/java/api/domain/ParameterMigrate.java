package api.domain;

import java.util.HashMap;
import java.util.Map;

public class ParameterMigrate {
    private int nbFile;
    private String importCode;
    private boolean little;
    private boolean medium;
    private boolean big;

    public ParameterMigrate(String importCode, boolean little, boolean medium, boolean big) {
        this.importCode = importCode;
        this.little = little;
        this.medium = medium;
        this.big = big;
    }

    public ParameterMigrate(int nbFile, boolean little, boolean medium, boolean big) {
        this.nbFile = nbFile;
        this.little = little;
        this.medium = medium;
        this.big = big;
    }

    public int getNbFile() {
        return nbFile;
    }

    public void setNbFile(int nbFile) {
        this.nbFile = nbFile;
    }

    public String getImportCode() {
        return importCode;
    }

    public void setImportCode(String importCode) {
        this.importCode = importCode;
    }

    public boolean isLittle() {
        return little;
    }

    public void setLittle(boolean little) {
        this.little = little;
    }

    public boolean isMedium() {
        return medium;
    }

    public void setMedium(boolean medium) {
        this.medium = medium;
    }

    public boolean isBig() {
        return big;
    }

    public void setBig(boolean big) {
        this.big = big;
    }

    public Map<Integer, String> getMapTypeFile() {

        Map<Integer, String> mapTypeFile = new HashMap<>();
        int idMap = 0;
        if (this.little) {
            mapTypeFile.put(idMap, "little");
            idMap++;
        }
        if (this.medium) {
            mapTypeFile.put(idMap, "medium");
            idMap++;
        }
        if (this.big) {
            mapTypeFile.put(idMap, "big");
            idMap++;
        }

        return mapTypeFile;
    }


}
