package com.kmitl58070042.dnyopr.comparizon.fragment;


import android.Manifest;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.kmitl58070042.dnyopr.comparizon.MainActivity;
import com.kmitl58070042.dnyopr.comparizon.R;
import com.kmitl58070042.dnyopr.comparizon.database.ItemInfoDB;
import com.kmitl58070042.dnyopr.comparizon.model.ItemInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class AddItemFragment extends Fragment implements View.OnClickListener {

    private ItemInfo itemInfo;
    private ItemInfoDB itemInfoDB;
    private EditText brand, detail, cost, size;
    private AddItemFragmentListener listener;
    private int SELECT_FILE = 0;
    private String strPath;
    private ImageView image;

    private int REQUEST_CODE = 1;


    public interface AddItemFragmentListener {
        void onItemAdded();
    }


    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance(ItemInfo itemInfo) {

        Bundle args = new Bundle();
        args.putParcelable("itemInfo", itemInfo);
        AddItemFragment fragment = new AddItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemInfoDB = Room.databaseBuilder(getActivity(),
                ItemInfoDB.class, "ItemInfo")
                .fallbackToDestructiveMigration()
                .build();
        itemInfo = getArguments().getParcelable("itemInfo");

        listener = (AddItemFragmentListener) getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);


        brand = rootView.findViewById(R.id.edit_brand);
        detail = rootView.findViewById(R.id.edit_detail);
        cost = rootView.findViewById(R.id.edit_cost);
        size = rootView.findViewById(R.id.edit_size);
        image = rootView.findViewById(R.id.btn_image);

        Log.e("where", "onCreateView");

        //save item data
        Button btn_save = rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        image.setOnClickListener(this);

        return rootView;
    }

    private void onSave() {
        if (validateData()) {

            Log.e("where", "pass");
            new AsyncTask<Void, Void, ItemInfo>() {

                @Override
                protected ItemInfo doInBackground(Void... voids) {


                    itemInfo.setBrand(brand.getText().toString());
                    itemInfo.setDetail(detail.getText().toString());
                    itemInfo.setCost(Float.valueOf(cost.getText().toString()));
                    itemInfo.setSize(Float.valueOf(size.getText().toString()));
                    itemInfo.setImage(strPath);

                    itemInfoDB.itemInfoDAO().insert(itemInfo);
                    return null;
                }

                @Override
                protected void onPostExecute(ItemInfo itemInfo) {
                    super.onPostExecute(itemInfo);

                    listener.onItemAdded();
                }
            }.execute();
        } else {
            Toast.makeText(getActivity(), "Please enter description.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateData() {
        if (brand.getText().toString().equals("") ||
                detail.getText().toString().equals("") ||
                cost.getText().toString().equals("") ||
                size.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {

        if (R.id.btn_save == view.getId()) {
            Log.e("where", "onClick");
            onSave();
        }
        if (R.id.btn_image == view.getId()) {
            Log.wtf("where", "add image");
            addImage();
        }


    }

    private void addImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select"), REQUEST_CODE);

//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            itemInfo.setImage(String.valueOf(uri));

            strPath = getRealPathFromURI(getContext(), uri);

            Log.wtf("path", String.valueOf(uri));
            try {
                Log.wtf("path", "try");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.wtf("path", "else");
        }

//        if (resultCode == Activity.RESULT_OK) {
//
//            if (requestCode == SELECT_FILE) {
//
//                Uri uriSelectedImage = data.getData();
//                strPath = getRealPathFromURI(getContext(), uriSelectedImage);
//
//                File fImage = new File(strPath);
//                Bitmap imageBitmap = BitmapFactory.decodeFile(fImage.getPath());
//
//                image.setImageBitmap(imageBitmap);
//
//            }
//        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}