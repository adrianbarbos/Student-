package com.aka.studentplus.news;

import java.util.List;
import java.util.Map;

import com.aka.studentplus.imgload.UrlImageViewHelper;

import android.content.Context;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class Adapter extends SimpleAdapter {

public Adapter(Context context, List<? extends Map<String, ?>> data,
        int resource, String[] from, int[] to) {
    super(context, data, resource, from, to);
    // TODO Auto-generated constructor stub
}
@Override
public void setViewImage(ImageView v, String value) {
    UrlImageViewHelper.setUrlDrawable(v, value);   
}

}