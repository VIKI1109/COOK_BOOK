package mdp20126376.mdpcw02.cookbook.model;

public class Video {

    public Video(String title, String dataPath) {
        this.title = title;
        this.dataPath = dataPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    String title;
    String dataPath;


 }
