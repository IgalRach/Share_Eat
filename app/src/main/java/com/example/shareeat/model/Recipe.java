package com.example.shareeat.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firestore.v1.DocumentTransform;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

@Entity
public class Recipe {
    //user.nickname;
    @PrimaryKey
    @NonNull
    private String id;
    private String titleRecipe;
    private String category;
    private String recipe;
    private Long CreatedDate;
    private Long UpdatedDate;
    private String imageUrl;
    private String userId;
    private String userName;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("titleRecipe", titleRecipe);
        result.put("category", category);
        result.put("recipe", recipe);
        result.put("CreatedDate", FieldValue.serverTimestamp());
        result.put("lastUpdated", FieldValue.serverTimestamp());
        result.put("imageUrl", imageUrl);
        result.put("userId", userId);
        result.put("userName", userName);
        return result;
    }

    public void fromMap(Map<String, Object> map) {
        id = (String)map.get("id");
        titleRecipe = (String)map.get("titleRecipe");
        category = (String)map.get("category");
        recipe = (String)map.get("recipe");
        imageUrl = (String)map.get("imageUrl");
        userId = (String)map.get("userId");
        userName = (String)map.get("userName");
        Timestamp ts = (Timestamp) map.get("CreatedDate");
        Timestamp ts1 = (Timestamp) map.get("lastUpdated");
        CreatedDate = ts.getSeconds();
        UpdatedDate = ts1.getSeconds();
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }


    public String getTitleRecipe() {
        return titleRecipe;
    }

    public void setTitleRecipe(String titleRecipe) {
        this.titleRecipe = titleRecipe;
    }

    public Long getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Long createdDate) {
        CreatedDate = createdDate;
    }

    public Long getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        UpdatedDate = updatedDate;
    }
}
