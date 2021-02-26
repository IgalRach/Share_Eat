package com.example.shareeat.model;


import android.app.Activity;
import java.util.LinkedList;
import java.util.List;

public class Model {
      public final static Model instance = new Model();
    List<Recipe> data = new LinkedList<Recipe>();

    private Model(){
        for(int i=0; i<100; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitleRecipe("1234"+i);
            recipe.setRecipe("five lemons , 20 apples"+i);
            data.add(recipe);
        }
    }

    public List<Recipe> getAllRecipes(){
        return data;
    }

    public void addRecipe(Recipe recipe){

    }
    public interface SuccessListener{
        void onComplete(boolean result);
    }

    public interface Listener<T>{
        void onComplete(T t);
    }

    ModelFirebase modelFirebase = new ModelFirebase();
    ModelSql modelSql = new ModelSql();

    private Activity mActivity;
    public final static Model instance = new Model();
    ModelFirebase fireBase = new ModelFirebase();

    public void signUpFB(User user, String password){
        fireBase.signUpToFirebase(user, password, mActivity);
    }

    public void signInFB(String email, String password){
        fireBase.signInToFirebase(email ,password, mActivity);
    }
  

}

