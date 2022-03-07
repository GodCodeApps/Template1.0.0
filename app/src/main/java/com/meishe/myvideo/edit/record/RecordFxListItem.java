package com.meishe.myvideo.edit.record;

import android.graphics.drawable.Drawable;
import androidx.annotation.Keep;
import java.io.Serializable;

@Keep
public class RecordFxListItem implements Serializable {
    public String fxID;
    public String fxName;
    public String imagePath;
    public Drawable image_drawable;
    public int index;
    public boolean selected = false;
}
