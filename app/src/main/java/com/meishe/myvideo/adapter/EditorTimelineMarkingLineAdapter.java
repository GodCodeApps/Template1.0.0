package com.meishe.myvideo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meishe.common.utils.Logger;
import com.meishe.myvideo.util.PixelPerMicrosecondUtil;
import com.meishe.myvideo.util.TimeUtil;
import com.meishe.myvideoapp.R;
import java.util.ArrayList;
import java.util.List;

public class EditorTimelineMarkingLineAdapter extends RecyclerView.Adapter {
    private static final String TAG = "EditorTimelineMarkingLineAdapter";
    private static final int TYPE_FOOT = 2;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_MID = 1;
    private int firstWidth;
    private float mContentWidth;
    private Context mContext;
    private List<String> mData = new ArrayList();
    private int mLeftPadding;
    private int mRightPadding;
    private int mUnitDistance;

    public EditorTimelineMarkingLineAdapter(Context context, int i, int i2, long j) {
        this.mContext = context;
        this.mLeftPadding = i;
        this.mRightPadding = i2;
        this.mUnitDistance = PixelPerMicrosecondUtil.durationToLength(1000000);
        init(j);
        Paint paint = new Paint();
        paint.setTextSize(context.getResources().getDimension(R.dimen.sp9));
        this.mContentWidth = paint.measureText("00:00");
        this.firstWidth = (int) (((float) this.mLeftPadding) - (this.mContentWidth / 2.0f));
    }

    private void init(long j) {
        if (j < 0) {
            Logger.e(TAG, "clipPointList.size() != clipList.size()");
            return;
        }
        this.mData.clear();
        this.mData.add(".");
        int floor = (int) Math.floor((double) (j + 1));
        for (int i = 0; i < floor; i++) {
            if (i % 2 == 0) {
                List<String> list = this.mData;
                list.add("" + TimeUtil.secToTime(i));
            } else {
                this.mData.add("  .");
            }
        }
        this.mData.add(".");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = new View(this.mContext);
        if (i == 0) {
            view.setLayoutParams(new ViewGroup.LayoutParams((int) (((float) this.mLeftPadding) - (this.mContentWidth / 2.0f)), -1));
            return new HeaderHolder(view);
        } else if (i == 1) {
            return new MidHolder(LayoutInflater.from(this.mContext).inflate(R.layout.timeline_editor_marking_line_layout, viewGroup, false));
        } else {
            if (i != 2) {
                return null;
            }
            view.setLayoutParams(new ViewGroup.LayoutParams(this.mRightPadding, -1));
            return new FootHolder(view);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MidHolder) {
            MidHolder midHolder = (MidHolder) viewHolder;
            midHolder.mRootView.setLayoutParams(new ViewGroup.LayoutParams(this.mUnitDistance, -1));
            midHolder.mTvMarkingLine.setText(this.mData.get(i));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<String> list = this.mData;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (i == 0) {
            return 0;
        }
        return i == getItemCount() - 1 ? 2 : 1;
    }

    public void setUnitDistance(int i, float f, long j) {
        String str = TAG;
        Logger.d(str, "setUnitDistance scale :" + f + "  unitDistance:" + i);
        initData(i, f, j);
        notifyDataSetChanged();
    }

    private void initData(int i, float f, long j) {
        double d = (double) f;
        int i2 = 0;
        if (d < 0.3d) {
            this.mUnitDistance = i * 8;
            this.mData.clear();
            this.mData.add(".");
            int floor = (int) Math.floor((double) (j + 1));
            while (i2 < floor) {
                if (i2 % 2 == 0 && i2 % 4 == 0 && i2 % 8 == 0) {
                    if (i2 % 16 == 0) {
                        List<String> list = this.mData;
                        list.add("" + TimeUtil.secToTime(i2));
                    } else {
                        this.mData.add("  .");
                    }
                }
                i2++;
            }
            this.mData.add(".");
        } else if (d >= 0.3d && d < 0.5d) {
            this.mUnitDistance = i * 4;
            this.mData.clear();
            this.mData.add(".");
            int floor2 = (int) Math.floor((double) (j + 1));
            while (i2 < floor2) {
                if (i2 % 2 == 0 && i2 % 4 == 0) {
                    if (i2 % 8 == 0) {
                        List<String> list2 = this.mData;
                        list2.add("" + TimeUtil.secToTime(i2));
                    } else {
                        this.mData.add("  .");
                    }
                }
                i2++;
            }
            this.mData.add(".");
        } else if (d >= 0.5d && d < 0.8d) {
            this.mUnitDistance = i * 2;
            this.mData.clear();
            this.mData.add(".");
            int floor3 = (int) Math.floor((double) (j + 1));
            while (i2 < floor3) {
                if (i2 % 2 == 0) {
                    if (i2 % 4 == 0) {
                        List<String> list3 = this.mData;
                        list3.add("" + TimeUtil.secToTime(i2));
                    } else {
                        this.mData.add("  .");
                    }
                }
                i2++;
            }
            this.mData.add(".");
        } else if (d >= 0.8d && f < 2.0f) {
            this.mUnitDistance = i;
            init(j);
        } else if (f >= 2.0f && f < 4.0f) {
            this.mUnitDistance = i / 2;
            this.mData.clear();
            this.mData.add(".");
            int floor4 = (int) Math.floor((double) (j + 1));
            while (i2 < floor4) {
                List<String> list4 = this.mData;
                list4.add("" + TimeUtil.secToTime(i2));
                if (i2 != floor4 - 1) {
                    this.mData.add("  .");
                }
                i2++;
            }
            this.mData.add(".");
        } else if (f >= 4.0f && f < 6.0f) {
            this.mUnitDistance = i / 4;
            this.mData.clear();
            this.mData.add(".");
            int floor5 = (int) Math.floor((double) (j + 1));
            while (i2 < floor5) {
                if (i2 > 0) {
                    this.mData.add("  .");
                }
                List<String> list5 = this.mData;
                list5.add("" + TimeUtil.secToTime(i2));
                if (i2 != floor5 - 1) {
                    this.mData.add("  .");
                    this.mData.add("15f");
                }
                i2++;
            }
            this.mData.add(".");
        } else if (f >= 6.0f && f < 8.0f) {
            this.mUnitDistance = i / 6;
            this.mData.clear();
            this.mData.add(".");
            int floor6 = (int) Math.floor((double) (j + 1));
            while (i2 < floor6) {
                if (i2 > 0) {
                    this.mData.add("  .");
                }
                List<String> list6 = this.mData;
                list6.add("" + TimeUtil.secToTime(i2));
                if (i2 != floor6 - 1) {
                    this.mData.add("  .");
                    this.mData.add("10f");
                    this.mData.add("  .");
                    this.mData.add("20f");
                }
                i2++;
            }
            this.mData.add(".");
        } else if (f >= 8.0f && f < 13.0f) {
            this.mUnitDistance = i / 12;
            this.mData.clear();
            this.mData.add(".");
            int floor7 = (int) Math.floor((double) (j + 1));
            while (i2 < floor7) {
                if (i2 > 0) {
                    this.mData.add("  .");
                }
                List<String> list7 = this.mData;
                list7.add("" + TimeUtil.secToTime(i2));
                if (i2 != floor7 - 1) {
                    this.mData.add("  .");
                    this.mData.add("5f");
                    this.mData.add("  .");
                    this.mData.add("10f");
                    this.mData.add("  .");
                    this.mData.add("15f");
                    this.mData.add("  .");
                    this.mData.add("20f");
                    this.mData.add("  .");
                    this.mData.add("25f");
                }
                i2++;
            }
            this.mData.add(".");
        } else if (f >= 13.0f) {
            this.mUnitDistance = i / 20;
            this.mData.clear();
            this.mData.add(".");
            int floor8 = (int) Math.floor((double) (j + 1));
            while (i2 < floor8) {
                if (i2 > 0) {
                    this.mData.add("  .");
                }
                List<String> list8 = this.mData;
                list8.add("" + TimeUtil.secToTime(i2));
                if (i2 != floor8 - 1) {
                    this.mData.add("  .");
                    this.mData.add("3f");
                    this.mData.add("  .");
                    this.mData.add("6f");
                    this.mData.add("  .");
                    this.mData.add("9f");
                    this.mData.add("  .");
                    this.mData.add("12f");
                    this.mData.add("  .");
                    this.mData.add("15f");
                    this.mData.add("  .");
                    this.mData.add("18f");
                    this.mData.add("  .");
                    this.mData.add("21f");
                    this.mData.add("  .");
                    this.mData.add("24f");
                    this.mData.add("  .");
                    this.mData.add("27f");
                }
                i2++;
            }
            this.mData.add(".");
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View view) {
            super(view);
        }
    }

    private class FootHolder extends RecyclerView.ViewHolder {
        public FootHolder(View view) {
            super(view);
        }
    }

    private class MidHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRootView;
        private TextView mTvMarkingLine;

        public MidHolder(View view) {
            super(view);
            this.mRootView = (RelativeLayout) view.findViewById(R.id.editor_thumbnail_marking_layout);
            this.mTvMarkingLine = (TextView) view.findViewById(R.id.tv_time_marking_line);
        }
    }

    public int getFirstWidth() {
        return this.firstWidth;
    }
}
