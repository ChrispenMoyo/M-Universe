package com.example.m_universe;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.Activity;
import java.util.UUID;
import android.util.Log;




import com.example.m_universe.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    public AddFragment() {
        // Required empty public constructor
    }

    FirebaseAuth firebaseAuth;
    EditText title, des;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String[] cameraPermission = new String[]{Manifest.permission.CAMERA};
    String[] storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    ProgressBar pd;
    ImageView image;
    String edititle, editdes, editimage;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;

    Uri imageuri = null;
    String name, email, uid, dp;
    DatabaseReference databaseReference;
    Button upload;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // title = view.findViewById(R.id.ptitle);
        des = view.findViewById(R.id.pdes);
        image = view.findViewById(R.id.imagep);
        upload = view.findViewById(R.id.pupload);
        pd = new ProgressBar(getContext());
        //  pd.setCancelable(false);
        Intent intent = requireActivity().getIntent();

        // image.setImageURI(imageuri);

        // Retrieving the user data like name ,email and profile pic using query
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    name = Objects.requireNonNull(dataSnapshot1.child("name").getValue()).toString();
                    email = "" + dataSnapshot1.child("email").getValue();
                    dp = "" + Objects.requireNonNull(dataSnapshot1.child("image").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Initialising camera and storage permission
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // After click on image we will be selecting an image
        image.setOnClickListener(v -> showImagePicDialog());

        // Now we will upload out blog
        upload.setOnClickListener(v -> {
            //String titl = "" + title.getText().toString().trim();
            String description = "" + des.getText().toString().trim();

            // If empty set error
            if (TextUtils.isEmpty(description)) {
                des.setError("Description Cant be empty");
                Toast.makeText(getContext(), "Description can't be left empty", Toast.LENGTH_LONG).show();
                return;
            }

            // If empty show error
            if (imageuri == null) {
                Toast.makeText(getContext(), "Select an Image", Toast.LENGTH_LONG).show();
            } else {
                uploadData(description);

            }
        });

        return view;
    }

    private void showImagePicDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Image From");
        builder.setItems(options, (dialog, which) -> {
            // check for the camera and storage permission if
            // not given the request for permission
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromCamera();
                }
            } else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickFromGallery();
                }
            }
        });
        builder.create().show();
    }

    // check for storage permission
    private Boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
    }

    // if not given then request for permission after that check if request is given or not

    // Define the ActivityResultLauncher for requesting camera permission
    private final ActivityResultLauncher<String> requestCameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Camera permission is granted
                    pickFromCamera();
                } else {
                    Toast.makeText(getContext(), "Please Enable Camera Permission", Toast.LENGTH_LONG).show();
                }
            }
    );

    // Define the ActivityResultLauncher for requesting storage permission
    private final ActivityResultLauncher<String> requestStoragePermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Storage permission is granted
                    pickFromGallery();
                } else {
                    Toast.makeText(getContext(), "Please Enable Storage Permission", Toast.LENGTH_LONG).show();
                }
            }
    );

    private final ActivityResultLauncher<Intent> pickFromGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // The user picked an image from the gallery
                    Intent data = result.getData();
                    if (data != null) {
                        imageuri = data.getData();
                        image.setImageURI(imageuri);
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> pickFromCameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // The user has captured an image
                    image.setImageURI(imageuri);
                }
            }
    );


    // request for permission to write data into storage
    private void requestStoragePermission() {
        //  requestStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestStoragePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }

    }

    // check camera permission to click picture using camera
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // request for permission to click photo using camera in app
    private void requestCameraPermission() {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);

    }

    // if access is given then pick image from camera and then put
    // the imageuri in intent extra and pass to startactivityforresult
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageuri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);

        pickFromCameraLauncher.launch(camerIntent);
    }

    // if access is given then pick image from gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");

        pickFromGalleryLauncher.launch(galleryIntent);
    }


// Upload the value of blog data into firebase
private void uploadData(final String description) {
    pd.setVisibility(View.VISIBLE);
    final String timestamp = String.valueOf(System.currentTimeMillis());
    String filepathname = "Posts/" + "post" + timestamp;
    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
    byte[] data = byteArrayOutputStream.toByteArray();

    // Initialize the storage reference for uploading the data
    StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child(filepathname);
    UploadTask uploadTask = storageReference1.putBytes(data);

    uploadTask.addOnSuccessListener(taskSnapshot -> {
        storageReference1.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUri = uri.toString();

            // If the upload is successful, update the data into Firebase
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid", uid);
            hashMap.put("uname", name);
            hashMap.put("uemail", email);
            hashMap.put("udp", dp);
            hashMap.put("description", description);
            hashMap.put("uimage", downloadUri);
            hashMap.put("ptime", timestamp);
            hashMap.put("plike", "0");
            hashMap.put("pcomments", "0");

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
            String postId = databaseReference.push().getKey();

            // Save the post data to the database
            databaseReference.child(postId).setValue(hashMap)
                    .addOnSuccessListener(aVoid -> {
                        pd.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Published", Toast.LENGTH_LONG).show();
                        des.setText("");
                        image.setImageURI(null);
                        imageuri = null;
                    })
                    .addOnFailureListener(e -> {
                        pd.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Failed to upload post", Toast.LENGTH_LONG).show();
                    });
        });
    }).addOnFailureListener(e -> {
        pd.setVisibility(View.GONE);
        Log.e("FirebaseError", "Failed to upload image: " + e.getMessage());
        Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_LONG).show();
    });
}

}
    // Here we are getting data from image
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
                imageuri = data.getData();
                image.setImageURI(imageuri);
            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                image.setImageURI(imageuri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
} */
