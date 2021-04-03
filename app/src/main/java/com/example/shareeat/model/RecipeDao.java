package com.example.shareeat.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("select * from Recipe")
   LiveData<List<Recipe>> getAllRecipes();

    @Query("select * from Recipe where userId = :userId")
    LiveData<List<Recipe>> getUserRecipes(String userId);

   @Query("select * from Recipe where category= :category")
   LiveData<List<Recipe>> getRecipesByCategory(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipe);

    @Delete
    void deleteRecipe(Recipe recipe);
}
