package com.bignerdranch.android.simplegalleryreplica.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.bignerdranch.android.simplegalleryreplica.R;
import com.bignerdranch.android.simplegalleryreplica.model.ImageFolder;
import com.bignerdranch.android.simplegalleryreplica.view.adapters.MainAdapter;
import com.bignerdranch.android.simplegalleryreplica.view.dialoges.FilterDialog;
import com.bignerdranch.android.simplegalleryreplica.view.dialoges.OnFilterTypes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final int OPEN_CAMERA_CODE = 1001;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.photo_recycler_view) RecyclerView mPhotosRecyclerView;

    private List<ImageFolder> mImageFolders = new ArrayList<>();
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mToolbar.getOverflowIcon()
                .setColorFilter(Color.parseColor("#FFFFFF"),
                        android.graphics.PorterDuff.Mode.SRC_IN);

        mPhotosRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter = new MainAdapter(this, mImageFolders);
        mPhotosRecyclerView.setAdapter(mAdapter);

        setSupportActionBar(mToolbar);
        MainActivityPermissionsDispatcher.getImageFoldersWithPermissionCheck(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivityPermissionsDispatcher.getImageFoldersWithPermissionCheck(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar_items, menu);
        return true;
    }



    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public  void getImageFolders(){
        mImageFolders.clear();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy = MediaStore.Images.Media.DATE_MODIFIED + " DESC";

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("image/*");

        String[] selectionArgs = new String[]{};


        Cursor cursor = this.getContentResolver().query(uri,
                projection,
                selectionMimeType,
                selectionArgs,
                orderBy);

        int dataIndex = cursor.getColumnIndex(projection[0]);
        int nameIndex = cursor.getColumnIndex(projection[1]);



        if(cursor != null){
            while (cursor.moveToNext()){
                String firstImage = cursor.getString(dataIndex);
                String folderName =  cursor.getString(nameIndex);
                ImageFolder imageFolder = new ImageFolder(folderName, firstImage, 1);
                if(mImageFolders.contains(imageFolder)){
                    int index = mImageFolders.indexOf(imageFolder);
                    mImageFolders.get(index).IncrementImageCount();
                }else{
                    mImageFolders.add(imageFolder);
                }
            }
        }
        System.out.print("Yo Yo");
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.open_camera_item:
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    PackageManager pm = this.getPackageManager();

                    final ResolveInfo mInfo = pm.resolveActivity(i, 0);

                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(intent);
                } catch (Exception e){

                }
                return true;
            case R.id.filter_media_item:
                FragmentManager fm = getSupportFragmentManager();
                FilterDialog dialog = new FilterDialog(new OnFilterTypes() {
                    @Override
                    public void onFilterTypesSelections(String[] selectionArgs) {

                    }
                });
                dialog.show(fm, "Filter");
        }
        return true;
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void startCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,OPEN_CAMERA_CODE);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void saveImage(Bitmap bitmap){
        UUID uuid = UUID.randomUUID();
        try {
            FileOutputStream out = new FileOutputStream("image-"+uuid.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case OPEN_CAMERA_CODE:

        }
    }
}
