package com.bignerdranch.android.simplegalleryreplica.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.simplegalleryreplica.R;
import com.bignerdranch.android.simplegalleryreplica.model.ImageFolder;
import com.bignerdranch.android.simplegalleryreplica.utils.SharedPreferencesConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int GRID = 0;
    public static final int LIST = 1;

    private Context mContext;
    private List<ImageFolder> mImageFolders;
    private int mDisplayType;
    private SharedPreferences mSharedPreferences;


    public MainAdapter(Context context, List<ImageFolder> imageFolders) {
        mContext = context;
        mImageFolders = imageFolders;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mDisplayType = mSharedPreferences.getInt(SharedPreferencesConstants.DISPLAY_TYPE, GRID);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(mDisplayType == GRID){
            v = LayoutInflater.from(mContext).inflate(R.layout.activity_main_recycler_view_cell,
                    parent,
                    false);
            return new ImageFolderViewHolder(v);
        }else{
            v =  LayoutInflater.from(mContext).inflate(R.layout.activity_main_recycler_second_view_cell,
                    parent,
                    false);
            return  new ListImageFolderViewHolder(v);
        }


    }

    public int getDisplayType() {
        return mDisplayType;
    }

    public void setDisplayType(int displayType) {
        mDisplayType = displayType;
        mSharedPreferences.edit()
                .putInt(SharedPreferencesConstants.DISPLAY_TYPE, displayType)
                .apply();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ImageFolderViewHolder){
            ((ImageFolderViewHolder)holder).bind(mImageFolders.get(position));
        }else{
            ((ListImageFolderViewHolder)holder).bind(mImageFolders.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mImageFolders.size();
    }


    class ImageFolderViewHolder extends RecyclerView.ViewHolder{

        private ImageView mFirstPhotoImageView;
        private TextView mFolderNameTextView;
        private TextView mNumberOfPhotosTextView;

        public ImageFolderViewHolder(@NonNull View itemView) {
            super(itemView);

            mFirstPhotoImageView = itemView.findViewById(R.id.first_image_view);
            mFolderNameTextView = itemView.findViewById(R.id.folder_name_text_view);
            mNumberOfPhotosTextView = itemView.findViewById(R.id.photos_count_text_view);
        }

        public void bind(ImageFolder imageFolder){
            Display display = ( (Activity) mContext).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Glide.with(mContext)
                    .load(new File(imageFolder.getFirstImageContainedPath()))
                    .apply(new RequestOptions().override(size.x/2, size.x/2))
                    .optionalCenterCrop()
                    .into(mFirstPhotoImageView);
            mFolderNameTextView.setText(imageFolder.getName());
            mNumberOfPhotosTextView.setText(String.valueOf(imageFolder.getImagesCount()));
        }
    }

    class ListImageFolderViewHolder extends RecyclerView.ViewHolder{

        private ImageView mFirstPhotoImageView;
        private TextView mFolderNameTextView;
        private TextView mNumberOfPhotosTextView;
        private TextView mFolderPathTextView;

        public ListImageFolderViewHolder(@NonNull View itemView) {
            super(itemView);

            mFirstPhotoImageView = itemView.findViewById(R.id.first_image_view);
            mFolderNameTextView = itemView.findViewById(R.id.folder_name_text_view);
            mNumberOfPhotosTextView = itemView.findViewById(R.id.photos_count_text_view);
            mFolderPathTextView = itemView.findViewById(R.id.folder_path_text_view);
        }

        public void bind(ImageFolder imageFolder){
            Display display = ( (Activity) mContext).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Glide.with(mContext)
                    .load(new File(imageFolder.getFirstImageContainedPath()))
                    .apply(new RequestOptions().override(size.x/2, size.x/2))
                    .optionalCenterCrop()
                    .into(mFirstPhotoImageView);
            mFolderNameTextView.setText(imageFolder.getName());
            mNumberOfPhotosTextView.setText(String.valueOf(imageFolder.getImagesCount()));
            mFolderPathTextView.setText(imageFolder.getFolderPath());
        }
    }

}
