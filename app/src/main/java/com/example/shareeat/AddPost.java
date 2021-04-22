package com.example.shareeat;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
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
import android.widget.Toast;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.Recipe;
import com.example.shareeat.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.shareeat.model.ModelFirebase.getImageFromFireBase;


public class AddPost extends Fragment {


    EditText recipeNameEditText;
    Spinner spinner;//category
    String category;
    EditText recipeEditText;
    ImageView avatarImageView;
    Button editImage;
    Button addBtn;
    Button cancelBtn;
    ProgressBar pb;

    Uri postImgUri;
    Bitmap postImgBitmap;
    boolean isExist=false;
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
        avatarImageView.setVisibility(View.INVISIBLE);

        getImageFromFireBase(User.getInstance().id);
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
        if (recipeNameEditText.getText().length() == 0 || recipeEditText.getText().length() == 0 || category.length() == 0) {
            Snackbar.make(view, "You must fill all the fields", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("TAG", "Some of the fields are empty.");
        }
        else if(isExist==false){
            Snackbar.make(view, "You must Import a Photo", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("TAG", "The Photo didnt Upload");
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


            recipe.setUserPic(User.getInstance().FBpic);

            addBtn.setEnabled(false);
            cancelBtn.setEnabled((false));
            editImage.setEnabled(false);

            BitmapDrawable drawable = (BitmapDrawable) avatarImageView.getDrawable();
            Bitmap bitmap=drawable.getBitmap();

            Model.instance.uploadImage(drawable.getBitmap(), recipe.getId(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null){
                        displayFailedError();
                    }
                    else{
                        recipe.setImageUrl(url);
                        pb.setVisibility(View.VISIBLE);
                        Model.instance.addRecipe(recipe, new Model.AddRecipeListener() {
                            @Override
                            public void onComplete() {
                                pb.setVisibility(View.INVISIBLE);
                                addBtn.setEnabled(true);
                                cancelBtn.setEnabled((false));
                                editImage.setEnabled(false);
                                Navigation.findNavController(addBtn).popBackStack();
                            }
                        });
                    }
                }
            });
        }


    }

    public void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void editImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your recipe picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                    isExist=true;
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(openGalleryIntent, 1);
                    isExist=true;
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData() != null && data != null && resultCode == RESULT_OK) {
            postImgUri = data.getData();
            avatarImageView.setImageURI(postImgUri);
            postImgBitmap = uriToBitmap(postImgUri);
        }
        else {
            Toast.makeText(getActivity(), "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {

        try {
            ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}