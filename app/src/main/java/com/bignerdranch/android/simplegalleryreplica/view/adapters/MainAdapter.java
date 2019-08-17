package com.bignerdranch.android.simplegalleryreplica.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ImageFolderViewHolder> {
    private Context mContext;
    private List<ImageFolder> mImageFolders;

    public MainAdapter(Context context, List<ImageFolder> imageFolders) {
        mContext = context;
        mImageFolders = imageFolders;
    }

    @NonNull
    @Override
    public ImageFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_main_recycler_view_cell,
                parent,
                false);
        return new ImageFolderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFolderViewHolder holder, int position) {
        holder.bind(mImageFolders.get(position));
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

}
