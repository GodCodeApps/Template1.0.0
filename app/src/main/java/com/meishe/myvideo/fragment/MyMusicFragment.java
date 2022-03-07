package com.meishe.myvideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.myvideo.activity.SelectMusicActivity;
import com.meishe.myvideo.adapter.MusicListAdapter;
import com.meishe.myvideo.bean.MusicInfo;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class MyMusicFragment extends Fragment {
    private MusicListAdapter mAdapter;
    private List<MusicInfo> mMusicData = new ArrayList();
    private RecyclerView mMusicRv;
    private LinearLayout mNoMusicBtn;
    private View mView;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.mView = layoutInflater.inflate(R.layout.fragment_my_music, viewGroup, false);
        this.mMusicRv = (RecyclerView) this.mView.findViewById(R.id.music_rv);
        this.mNoMusicBtn = (LinearLayout) this.mView.findViewById(R.id.no_music_btn);
        return this.mView;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        initMusicList();
    }

    private void initMusicList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), 1, false);
        List<MusicInfo> list = this.mMusicData;
        if (list == null || list.size() == 0) {
            this.mNoMusicBtn.setVisibility(0);
        } else {
            this.mNoMusicBtn.setVisibility(8);
        }
        this.mMusicRv.setLayoutManager(linearLayoutManager);
        this.mAdapter = new MusicListAdapter(getActivity(), this.mMusicData);
        this.mMusicRv.setAdapter(this.mAdapter);
        this.mAdapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.fragment.MyMusicFragment.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.MusicListAdapter.OnItemClickListener
            public void onItemClick(int i, MusicInfo musicInfo) {
                SelectMusicActivity selectMusicActivity = (SelectMusicActivity) MyMusicFragment.this.getActivity();
                if (selectMusicActivity != null) {
                    selectMusicActivity.playMusic(musicInfo, false);
                }
            }
        });
    }

    public void loadAudioData(List<MusicInfo> list) {
        if (list != null && !list.isEmpty()) {
            this.mMusicData.clear();
            this.mMusicData.addAll(list);
            MusicListAdapter musicListAdapter = this.mAdapter;
            if (musicListAdapter != null) {
                musicListAdapter.updateData(this.mMusicData);
            }
        }
    }

    public void clearPlayState() {
        MusicListAdapter musicListAdapter = this.mAdapter;
        if (musicListAdapter != null) {
            musicListAdapter.clearPlayState();
        }
    }

    public void setSelectedMusic(MusicInfo musicInfo) {
        MusicListAdapter musicListAdapter = this.mAdapter;
        if (musicListAdapter != null) {
            musicListAdapter.setSelectedMusicInfo(musicInfo);
        }
    }
}
