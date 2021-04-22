package com.example.shareeat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shareeat.model.Model;
import com.example.shareeat.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment {

    CircleImageView profilePic;
    EditText newFullName;
    View view;
    FirebaseUser user;

    Uri postImgUri;
    Bitmap postImgBitmap;

    boolean isExist=false;
    User currentUser;
    UserProfileChangeRequest profileUpdates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        profilePic=view.findViewById(R.id.detailsprofile_profile_im);
        if (User.getInstance().profilePic != null) {
            Picasso.get().load(User.getInstance().profilePic).noPlaceholder().into(profilePic);
        }

        newFullName = view.findViewById(R.id.editTextTextPersonName);
        newFullName.setText(user.getDisplayName());

        Button updateProfilePic=view.findViewById(R.id.editProfile_upload_Btn);
        Button save = view.findViewById(R.id.editProfile_Save_Btn);
        Button cancel = view.findViewById(R.id.editProfile_cencel_Btn);

        updateProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
        return view;
    }


    private void updateUserProfile() {

        if (isExist){
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newFullName.getText().toString()).setPhotoUri(postImgUri)
                    .build();
            Model.instance.uploadImage(postImgBitmap, user.getEmail(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null){
                        displayFailedError();
                    }
                    else{
                        currentUser= new User( user.getUid(),newFullName.getText().toString(),user.getEmail(),url);
                        Model.instance.updateUserProfile(currentUser);

                    }

                }
            });

        }
        else{
            currentUser= new User( user.getUid(),newFullName.getText().toString(),user.getEmail());
            Model.instance.updateUserProfile(currentUser);
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newFullName.getText().toString()).setPhotoUri(postImgUri)
                    .build();

        }
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Navigation.findNavController(view).navigate( R.id.profile);
                    NavController navCtrl = Navigation.findNavController(view);
                    navCtrl.popBackStack();
                    navCtrl.popBackStack();
                    //navCtrl.navigateUp();
                }
            }
        });

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
            profilePic.setImageURI(postImgUri);
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
}