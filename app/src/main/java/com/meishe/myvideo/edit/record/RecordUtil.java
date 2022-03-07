package com.meishe.myvideo.edit.record;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.load.Key;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecordUtil {
    private static final String TAG = "RecordUtil";

    public static void clearRecordAudioData() {
    }

    public static List<RecordFxListItem> listRecordFxFromJson(Context context) {
        ArrayList arrayList = new ArrayList();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("record/record.json"), Key.STRING_CHARSET_NAME);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            }
            bufferedReader.close();
            inputStreamReader.close();
            JSONArray jSONArray = new JSONObject(sb.toString()).getJSONArray("record_fx");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                RecordFxListItem recordFxListItem = new RecordFxListItem();
                recordFxListItem.fxName = jSONObject.getString("name");
                recordFxListItem.fxID = jSONObject.getString("builtin_fx_name");
                recordFxListItem.index = jSONObject.getInt("rank");
                recordFxListItem.imagePath = jSONObject.getString("image_path");
                recordFxListItem.image_drawable = ContextCompat.getDrawable(context, context.getResources().getIdentifier(recordFxListItem.imagePath, "mipmap", context.getPackageName()));
                arrayList.add(recordFxListItem);
            }
            Collections.sort(arrayList, new RecordFxIndexComparator());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public static class RecordFxIndexComparator implements Comparator<RecordFxListItem> {
        public int compare(RecordFxListItem recordFxListItem, RecordFxListItem recordFxListItem2) {
            return recordFxListItem.index - recordFxListItem2.index;
        }
    }
}
