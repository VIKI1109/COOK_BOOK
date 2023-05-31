package mdp20126376.mdpcw02.cookbook.model;

public class Recipe {


    public Recipe(String title, int recipe_id,float rating, String instruction, String ingredient_list,String video_path) {
        this.title = title;
        this.rating = rating;
        this.instruction = instruction;
        this.ingredient_list = ingredient_list;
        this.video_path=video_path;
        this.recipe_id = recipe_id;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getIngredient_list() {
        return ingredient_list;
    }

    public void setIngredient_list(String ingredient_list) {
        this.ingredient_list = ingredient_list;
    }


    private float rating;
    private String instruction;
    private String ingredient_list;


    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    private int recipe_id;

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    private String video_path;





}
