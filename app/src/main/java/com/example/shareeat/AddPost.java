package com.example.shareeat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class AddPost extends Fragment {


    TextView pageTitle;
    EditText recipeNameEditText;
    Spinner spinner;//category
    String category;
    EditText recipeEditText;
    ImageView avatarImageView;
    Button editImage;
    Button addBtn;
    Button cancelBtn;
    ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_post, container, false);

        avatarImageView = view.findViewById(R.id.addPost_avatar);
        recipeNameEditText= view.findViewById(R.id.addPost_recipeName);
        recipeEditText= view.findViewById(R.id.addPost_Recipe);
        String [] categories ={"","Italian","Spicy","French","Meat","Dairy","Fish","Kosher","Dessert",};
        spinner = (Spinner) view.findViewById(R.id.addPost_Category);
        editImage= view.findViewById(R.id.addPost_uploadPic_btn);
        addBtn = view.findViewById(R.id.addPost_add_btn);
        cancelBtn = view.findViewById(R.id.addPost_cancel_btn);
        pb = view.findViewById(R.id.addPost_progressBar);
        pb.setVisibility(View.INVISIBLE);

        //category spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                category = spinner.getSelectedItem().toString();
                Log.d("TAG","selected itrem is: "+category);
            }

            public void onNothingSelected(AdapterView<?> arg) {

            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            //    Navigation.findNavController(view).navigate(R.id.);
                addRecipe(v);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        return view;
    }



    private void addRecipe(View view){
        if (recipeNameEditText.getText().length() == 0 || recipeEditText.getText().length() == 0) {
            Snackbar.make(view, "You must fill all the fields", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("TAG", "Some of thef ields are empty.");
        }
        else{
            Recipe recipe= new Recipe();
            recipe.setId(UUID.randomUUID().toString());
            recipe.setTitleRecipe(recipeNameEditText.getText().toString());
            recipe.setRecipe(recipeEditText.getText().toString());
            recipe.setCategory(category);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                recipe.setUserId(user.getUid());
                recipe.setUserName(user.getDisplayName());
            }

            BitmapDrawable drawable = (BitmapDrawable) avatarImageView.getDrawable();
            Bitmap bitmap=drawable.getBitmap();

            addBtn.setEnabled(false);
            cancelBtn.setEnabled((false));
            editImage.setEnabled(false);
            avatarImageView.setEnabled(false);
            Model.instance.uploadImage(bitmap, recipe.getId(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null){

                    }
                    else{
                        recipe.setImageUrl(url);
                        Model.instance.addRecipe(recipe, new Model.AddRecipeListener() {
                            @Override
                            public void onComplete() {
                                addBtn.setEnabled(true);
                                cancelBtn.setEnabled((false));
                                editImage.setEnabled(false);
                                Navigation.findNavController(addBtn).popBackStack();
                            }
                        });
                    }
                }
            });

//            addBtn.setEnabled(false);
//            Log.d("TAG","recipe "+recipe.getUserName()+" id "+recipe.getUserId());
//            pb.setVisibility(View.VISIBLE);
//            Model.instance.addRecipe(recipe, new Model.AddRecipeListener() {
//                @Override
//                public void onComplete() {
//                    pb.setVisibility(View.INVISIBLE);
//                    addBtn.setEnabled(true);
//                   // AllPosts.reloadData();
//                    Navigation.findNavController(addBtn).popBackStack();
//                }
//            });


        }


    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void editImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatarImageView.setImageBitmap(imageBitmap);
        }
    }

}