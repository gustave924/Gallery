package com.bignerdranch.android.simplegalleryreplica.view.dialoges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bignerdranch.android.simplegalleryreplica.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FilterDialog extends AppCompatDialogFragment {

    private OnFilterTypes mOnFilterTypes;

    public FilterDialog(OnFilterTypes filterTypes){
        mOnFilterTypes = filterTypes;
    }


    @BindView(R.id.images_check_box) CheckBox mImagesCheckBox;
    @BindView(R.id.videos_check_box) CheckBox mVideosCheckBox;
    @BindView(R.id.gifs_check_box) CheckBox mGIFsCheckBox;
    @BindView(R.id.raw_images_check_box) CheckBox mRAWImagesCheckBox;
    @BindView(R.id.svgs_images_check_box) CheckBox mSVGsImagesCheckBox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_select_type_dialogue,
                container,
                false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.getDialog().getWindow().setLayout(width, height);
    }

    @OnClick(R.id.cancel_button)
    public void onCancel(View v){
        this.getDialog().dismiss();
    }

    @OnClick(R.id.ok_button)
    public void onOk(View v){
        List<String> mimeTypes = new ArrayList<>();
        if(mImagesCheckBox.isChecked()){
            mimeTypes.add(getMIMEType("jpg"));
            mimeTypes.add(getMIMEType("png"));
        }

        if(mVideosCheckBox.isChecked()){
            mimeTypes.add(getMIMEType("3gp"));
            mimeTypes.add(getMIMEType("mp4"));
            mimeTypes.add(getMIMEType("webm"));
            mimeTypes.add(getMIMEType("mkv"));
        }

        if(mGIFsCheckBox.isChecked()){
            mimeTypes.add(getMIMEType("gif"));
        }

        if(mRAWImagesCheckBox.isChecked()){
            mimeTypes.add(getMIMEType("DNG"));
            mimeTypes.add(getMIMEType("CR2"));
            mimeTypes.add(getMIMEType("NEF"));
            mimeTypes.add(getMIMEType("ARW"));
        }

        if (mSVGsImagesCheckBox.isChecked()){
            mimeTypes.add(getMIMEType("svg"));
        }
        String[] arr = new String[mimeTypes.size()];
        mOnFilterTypes.onFilterTypesSelections(mimeTypes.toArray(arr));

    }

    private String getMIMEType(String extension){
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return  mimeType;
    }
}
