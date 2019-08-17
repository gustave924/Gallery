package com.bignerdranch.android.simplegalleryreplica.model;

import android.media.Image;
import android.net.Uri;

import androidx.annotation.Nullable;

public class ImageFolder {
    private String name;
    private String firstImageContainedPath;
    private int imagesCount;

    public ImageFolder(String name, String firstImageContainedPath, int imagesCount) {
        this.name = name;
        this.firstImageContainedPath = firstImageContainedPath;
        this.imagesCount = imagesCount;
    }

    public String getName() {
        return name;
    }

    public String getFirstImageContainedPath() {
        return firstImageContainedPath;
    }

    public int getImagesCount() {
        return imagesCount;
    }

    public void IncrementImageCount(){
        imagesCount++;
    }

    private String getFolderPath(String firstImagePath ){
        Uri uri = Uri.parse(firstImagePath);
        String filePath = firstImagePath.replace(uri.getLastPathSegment(), "");
        return filePath;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        ImageFolder foreignImageFolder = (ImageFolder) obj;
        String foreignFolderPath = getFolderPath(foreignImageFolder.getFirstImageContainedPath());
        String citizienFolderPath = getFolderPath(this.getFirstImageContainedPath());
        return foreignFolderPath.equals(citizienFolderPath);
    }
}
