package com.meishe.myvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.bean.MusicInfo;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private OnItemClickListener mClickListener;
    private Context mContext;
    private List<MusicInfo> mMusicDataList = new ArrayList();
    private MusicInfo selectedMusicInfo;

    public interface OnItemClickListener {
        void onItemClick(int i, MusicInfo musicInfo);
    }

    public MusicListAdapter(Context context, List<MusicInfo> list) {
        this.mContext = context;
        this.mMusicDataList = list;
    }

    public void updateData(List<MusicInfo> list) {
        this.mMusicDataList = list;
        notifyDataSetChanged();
    }

    public void clearPlayState() {
        List<MusicInfo> list = this.mMusicDataList;
        if (list != null) {
            for (MusicInfo musicInfo : list) {
                if (musicInfo != null) {
                    musicInfo.setPlay(false);
                    musicInfo.setPrepare(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mClickListener = onItemClickListener;
    }

    /* access modifiers changed from: package-private */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_music_author;
        private TextView item_music_name;
        private ImageButton item_music_play_btn;

        public ViewHolder(View view) {
            super(view);
            this.item_music_name = (TextView) view.findViewById(R.id.music_name);
            this.item_music_author = (TextView) view.findViewById(R.id.music_author);
            this.item_music_play_btn = (ImageButton) view.findViewById(R.id.music_play_btn);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music, viewGroup, false));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0009, code lost:
        r0 = r4.mMusicDataList.get(r6);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBindViewHolder(com.meishe.myvideo.adapter.MusicListAdapter.ViewHolder r5, final int r6) {
        /*
        // Method dump skipped, instructions count: 126
        */
        
//本方法所在的代码反编译失败，请在反编译界面按照提示打开Ejb编译器，找到当前对应的类的相应方法，替换到这里，然后进行适当的代码修复工作

//throw new UnsupportedOperationException("Method not decompiled: com.meishe.myvideo.adapter.MusicListAdapter.onBindViewHolder(com.meishe.myvideo.adapter.MusicListAdapter$ViewHolder, int):void");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mMusicDataList.size();
    }

    public void setSelectedMusicInfo(MusicInfo musicInfo) {
        this.selectedMusicInfo = musicInfo;
        notifyDataSetChanged();
    }
}
