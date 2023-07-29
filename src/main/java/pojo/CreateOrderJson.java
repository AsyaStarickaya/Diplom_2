package pojo;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderJson {
    private List<String> ingredients;

    public CreateOrderJson(String ingredient1, String ingredient2){
        this.ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
    }


    public CreateOrderJson(){
        this.ingredients = new ArrayList<>();
    }

}
