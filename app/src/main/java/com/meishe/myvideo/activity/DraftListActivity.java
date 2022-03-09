package com.meishe.myvideo.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meicam.sdk.NvsStreamingContext;
import com.meishe.common.utils.CollectionUtils;
import com.meishe.common.utils.CustomDialogUtil;
import com.meishe.common.utils.Logger;
import com.meishe.draft.DraftManager;
import com.meishe.draft.data.DraftData;
import com.meishe.draft.util.DraftFileUtil;
import com.meishe.engine.bean.TimelineData;
import com.meishe.myvideo.adapter.ManageListAdapter;
import com.meishe.myvideo.decoration.SpaceItemDecoration;
import com.meishe.myvideo.util.AppManager;
import com.meishe.myvideo.util.ParameterSettingValues;
import com.meishe.myvideo.util.SpUtil;
import com.meishe.myvideo.util.SystemUtils;
import com.meishe.myvideo.util.Util;
import com.meishe.myvideo.util.asset.NvAssetConstant;
import com.meishe.myvideo.util.asset.NvAssetManager;
import com.meishe.myvideo.view.PrivacyPolicyDialog;
import com.meishe.myvideoapp.R;
import com.meishe.player.common.Constants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DraftListActivity extends BasePermissionActivity {
    public static final int AD_SPANNER_CHANGE_CODE = 203;
    public static final int INIT_ARSCENE_COMPLETE_CODE = 201;
    public static final int INIT_ARSCENE_FAILURE_CODE = 202;
    public static final int REQUEST_CAMERA_PERMISSION_CODE = 200;
    private String TAG = DraftListActivity.class.getSimpleName();
    private boolean arSceneFinished = false;
    private boolean initARSceneing = true;
    private int mCanUseARFaceType = 0;
    private Dialog mConfirmDeleteDialog;
    private int mCurrentPosition = -1;
    private List<DraftData> mDraftData = new ArrayList();
    private MainActivityHandler mHandler = new MainActivityHandler(this);
    private HandlerThread mHandlerThread;
    private View mLlDelete;
    private boolean mManageFlag = false;
    private ManageListAdapter mManageListAdapter;
    private RecyclerView mManageListview;
    private TextView mManageTextView;
    private Dialog mManagerMoreDialog;
    private Dialog mRenameDialog;
    private ImageButton mSettingButton;

    private void recordLog() {
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public int initRootView() {
        return R.layout.activity_draft_list;
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initTitle() {
    }

    /* access modifiers changed from: package-private */
    public class MainActivityHandler extends Handler {
        WeakReference<DraftListActivity> mWeakReference;

        public MainActivityHandler(DraftListActivity mainActivity) {
            this.mWeakReference = new WeakReference<>(mainActivity);
        }

        public void handleMessage(Message message) {
            if (this.mWeakReference.get() != null) {
                switch (message.what) {
                    case 201:
                        DraftListActivity.this.arSceneFinished = true;
                        DraftListActivity.this.initARSceneing = false;
                        return;
                    case DraftListActivity.INIT_ARSCENE_FAILURE_CODE /*{ENCODED_INT: 202}*/:
                        DraftListActivity.this.arSceneFinished = false;
                        DraftListActivity.this.initARSceneing = false;
                        return;
                    case DraftListActivity.AD_SPANNER_CHANGE_CODE /*{ENCODED_INT: 203}*/:
                    default:
                        return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initViews() {
        mManageListview = findViewById(R.id.manage_listview);
        mSettingButton = findViewById(R.id.bt_setting);
        mManageListview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mManageListAdapter = new ManageListAdapter(this, this.mDraftData);
        mManageListAdapter.setOnItemClickListener(new ManageListAdapter.OnItemClickListener() {
            /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass1 */

            @Override // com.meishe.myvideo.adapter.ManageListAdapter.OnItemClickListener
            public void onItemClick(int i, DraftData draftData) {
                TimelineData.getInstance().fromJson(draftData.getJsonData());
                Bundle bundle = new Bundle();
                bundle.putInt("from_type", 1);
                bundle.putString(DraftEditActivity.INTENT_KEY_DRAFT_DIR, ((DraftData) DraftListActivity.this.mDraftData.get(i)).getDirPath());
                AppManager.getInstance().jumpActivity(DraftListActivity.this, DraftEditActivity.class, bundle);
            }
        });
        this.mManageListAdapter.setOnMoreClickListener(new ManageListAdapter.OnMoreClickListener() {
            /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass2 */

            @Override // com.meishe.myvideo.adapter.ManageListAdapter.OnMoreClickListener
            public void onClick(int i, DraftData draftData) {
                DraftListActivity.this.showMoreDialog(i, draftData);
            }
        });
        this.mManageListview.setAdapter(this.mManageListAdapter);
        this.mManageListview.addItemDecoration(new SpaceItemDecoration(0, 0));
        this.mLlDelete = findViewById(R.id.ll_delete);
        findViewById(R.id.bt_delete).setOnClickListener(this);
        this.mLlDelete.setVisibility(View.GONE);
        this.mLlDelete.setOnClickListener(this);
        this.mManageTextView = (TextView) findViewById(R.id.manage_textView);
        this.mManageTextView.setOnClickListener(this);
    }

    private void prepareEffectAssets() {
        long currentTimeMillis = System.currentTimeMillis();
        NvAssetManager init = NvAssetManager.init(this.mContext.getApplicationContext());
        NvAssetConstant.NvAssetPath[] values = NvAssetConstant.NvAssetPath.values();
        for (NvAssetConstant.NvAssetPath nvAssetPath : values) {
            init.searchLocalAssets(nvAssetPath.type);
            init.searchReservedAssets(nvAssetPath.type, nvAssetPath.path);
        }
        Logger.d(this.TAG, "prepareEffectAssets: cast = " + (System.currentTimeMillis() - currentTimeMillis));
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity, androidx.fragment.app.FragmentActivity
    public void onResume() {
        super.onResume();
        cancelDraftsClickAble();
        if (hasAllPermission()) {
            recordLog();
            getDraftData();
        }
    }

    private void getDraftData() {
        if (this.mManageListAdapter != null) {
            this.mDraftData = DraftManager.getInstance().getDraftData(getApplicationContext());
            this.mManageListAdapter.updateDrafts(this.mDraftData);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initData() {
        if (hasAllPermission()) {
//            showPrivacyDialog();
            initARSceneEffect();
            prepareEffectAssets();
        } else {
            checkPermissions();
        }
        SpUtil.getInstance(getApplicationContext());
        ParameterSettingValues parameterSettingValues = (ParameterSettingValues) SpUtil.getObjectFromShare(this, "paramter");
        if (parameterSettingValues != null) {
            ParameterSettingValues.setParameterValues(parameterSettingValues);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BaseActivity
    public void initListener() {
        this.mSettingButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();/*{ENCODED_INT: 2131296352}*/
        if (id == R.id.bt_delete || id == R.id.ll_delete) { /*{ENCODED_INT: 2131296561}*/
            if (!CollectionUtils.isEmpty(this.mManageListAdapter.getDeleteDraft())) {
                showConfirmDelete();
                return;
            }
            return;
        } else if (id == R.id.bt_setting) { /*{ENCODED_INT: 2131296353}*/
            AppManager.getInstance().jumpActivity(this, SettingActivity.class);
            return;
        } else if (id == R.id.manage_textView) { /*{ENCODED_INT: 2131296577}*/
            checkDraftsClickAble();
            return;
        }
        return;
    }

    private void initConfirmDeleteDialog() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_draft_delete, (ViewGroup) null);
        inflate.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass3 */

            public void onClick(View view) {
                DraftListActivity.this.mManageListAdapter.deleteCheckedDrafts(new ManageListAdapter.OnDeleteListener() {
                    /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass3.AnonymousClass1 */

                    @Override // com.meishe.myvideo.adapter.ManageListAdapter.OnDeleteListener
                    public void onComplete() {
                        DraftListActivity.this.checkDraftsClickAble();
                    }

                    @Override // com.meishe.myvideo.adapter.ManageListAdapter.OnDeleteListener
                    public void onStart(List<DraftData> list) {
                        DraftListActivity.this.deleteAllDraft(list);
                    }
                });
                DraftListActivity.this.closeConfirmDelete();
            }
        });
        inflate.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass4 */

            public void onClick(View view) {
                DraftListActivity.this.closeConfirmDelete();
            }
        });
        this.mConfirmDeleteDialog = CustomDialogUtil.getCustomWrapDialog(this.mContext, inflate, CustomDialogUtil.SHOW_POSITION.CENTER);
    }

    private void initRenameDialog() {
        int i = this.mCurrentPosition;
        if (i >= 0 && i < this.mDraftData.size()) {
            View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_draft_rename, (ViewGroup) null);
            final EditText editText = (EditText) inflate.findViewById(R.id.et_draft_name);
            editText.setText(this.mDraftData.get(this.mCurrentPosition).getFileName());
            inflate.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
                /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass5 */

                public void onClick(View view) {
                    DraftListActivity.this.renameDraft(editText.getText().toString());
                    DraftListActivity.this.closeRenameDialog();
                }
            });
            inflate.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass6 */

                public void onClick(View view) {
                    DraftListActivity.this.closeRenameDialog();
                }
            });
            this.mRenameDialog = CustomDialogUtil.getCustomWrapDialog(this.mContext, inflate, CustomDialogUtil.SHOW_POSITION.CENTER);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void closeConfirmDelete() {
        Dialog dialog = this.mConfirmDeleteDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mConfirmDeleteDialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void closeRenameDialog() {
        Dialog dialog = this.mRenameDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mRenameDialog.dismiss();
        }
    }

    private void showConfirmDelete() {
        if (this.mConfirmDeleteDialog == null) {
            initConfirmDeleteDialog();
        }
        Dialog dialog = this.mConfirmDeleteDialog;
        if (dialog != null && !dialog.isShowing()) {
            this.mConfirmDeleteDialog.show();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showRenameDialog() {
        if (this.mRenameDialog == null) {
            initRenameDialog();
        }
        Dialog dialog = this.mRenameDialog;
        if (dialog != null && !dialog.isShowing()) {
            this.mRenameDialog.show();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void checkDraftsClickAble() {
        this.mManageFlag = !this.mManageFlag;
        if (this.mManageFlag) {
            showDeleteDialog();
            this.mManageTextView.setText(this.mContext.getResources().getString(R.string.draft_manage_cancel));
        } else {
            closeDeleteDialog();
            this.mManageTextView.setText(this.mContext.getResources().getString(R.string.draft_manage));
        }
        this.mManageListAdapter.setCheckAble(this.mManageFlag);
    }

    private void cancelDraftsClickAble() {
        boolean z = this.mManageFlag;
        if (z) {
            this.mManageFlag = !z;
            closeDeleteDialog();
            this.mManageTextView.setText(this.mContext.getResources().getString(R.string.draft_manage));
            this.mManageListAdapter.setCheckAble(this.mManageFlag);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BasePermissionActivity
    public List<String> initPermissions() {
        return Util.getAllPermissionsList();
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BasePermissionActivity
    public void hasPermission() {
        showPrivacyDialog();
        initARSceneEffect();
        prepareEffectAssets();
        getDraftData();
        recordLog();
    }

    private void initARSceneEffect() {
        this.mCanUseARFaceType = NvsStreamingContext.hasARModule();
        if (this.mCanUseARFaceType != 1 || this.arSceneFinished) {
            this.initARSceneing = false;
            return;
        }
        if (this.mHandlerThread == null) {
            this.mHandlerThread = new HandlerThread("handlerThread");
            this.mHandlerThread.start();
        }
//        new Handler(this.mHandlerThread.getLooper(), new Handler.Callback() {
//            /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass7 */
//
//            public boolean handleMessage(Message message) {
//                boolean copyFileIfNeed = FileUtil.copyFileIfNeed(MainActivity.this, "SenseAR_ST.model", "facemode");
//                String str = MainActivity.this.TAG;
//                Logger.e(str, "copySuccess-->" + copyFileIfNeed);
//                File externalFilesDir = MainActivity.this.getApplicationContext().getExternalFilesDir(null);
//                boolean initHumanDetection = NvsStreamingContext.initHumanDetection(MeiSheApplication.getContext(), externalFilesDir + "/facemode/SenseAR_ST.model", "assets:/facemode/SenseAR_ST.lic", 19);
//                String str2 = MainActivity.this.TAG;
//                Logger.e(str2, "initSuccess-->" + initHumanDetection);
//                boolean z = NvsStreamingContext.setupHumanDetectionData(0, "assets:/facemode/fakeface.dat");
//                String str3 = MainActivity.this.TAG;
//                Logger.e(str3, "fakefaceSuccess-->" + z);
//                boolean z2 = NvsStreamingContext.setupHumanDetectionData(1, "assets:/facemode/makeup.dat");
//                String str4 = MainActivity.this.TAG;
//                Logger.e(str4, "makeupSuccess-->" + z2);
//                if (initHumanDetection) {
//                    MainActivity.this.mHandler.sendEmptyMessage(201);
//                } else {
//                    MainActivity.this.mHandler.sendEmptyMessage(MainActivity.INIT_ARSCENE_FAILURE_CODE);
//                }
//                return false;
//            }
//        }).sendEmptyMessage(1);
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BasePermissionActivity
    public void nonePermission() {
        Log.d(this.TAG, "hasPermission: 没有允许权限");
    }

    /* access modifiers changed from: protected */
    @Override // com.meishe.myvideo.activity.BasePermissionActivity
    public void noPromptPermission() {
        Log.d(this.TAG, "hasPermission: 用户选择了不再提示");
        startAppSettings();
    }

    public void startAppSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    public void materialSelect(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("from_type", 1);
        AppManager.getInstance().jumpActivity(this, MaterialSelectActivity.class, bundle);
    }

    private void initMoreDialog() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_draft_manager, (ViewGroup) null);
        View findViewById = inflate.findViewById(R.id.tv_rename);
        View findViewById2 = inflate.findViewById(R.id.tv_copy);
        View findViewById3 = inflate.findViewById(R.id.tv_delete);
        findViewById.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass8 */

            public void onClick(View view) {
                DraftListActivity.this.closeMoreDialog();
                DraftListActivity.this.showRenameDialog();
            }
        });
        findViewById2.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass9 */

            public void onClick(View view) {
                DraftListActivity.this.copyDraft();
                DraftListActivity.this.closeMoreDialog();
            }
        });
        findViewById3.setOnClickListener(new View.OnClickListener() {
            /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass10 */

            public void onClick(View view) {
                DraftListActivity.this.deleteDraft();
                DraftListActivity.this.closeMoreDialog();
            }
        });
        this.mManagerMoreDialog = CustomDialogUtil.getCustomDialog(this.mContext, inflate, CustomDialogUtil.SHOW_POSITION.BOTTOM);
        this.mManagerMoreDialog.setCanceledOnTouchOutside(true);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void deleteDraft() {
        int i = this.mCurrentPosition;
        if (i >= 0 && i < this.mDraftData.size()) {
            DraftFileUtil.deleteFolder(this.mDraftData.get(this.mCurrentPosition).getDirPath());
            if (this.mManageListAdapter != null) {
                this.mDraftData = DraftManager.getInstance().getDraftData(getApplicationContext());
                this.mManageListAdapter.updateDrafts(this.mDraftData);
                this.mCurrentPosition = -1;
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void deleteAllDraft(List<DraftData> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (DraftData draftData : list) {
                DraftFileUtil.deleteFolder(draftData.getDirPath());
            }
            this.mDraftData = DraftManager.getInstance().getDraftData(getApplicationContext());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void renameDraft(String str) {
        int i = this.mCurrentPosition;
        if (i >= 0 && i < this.mDraftData.size()) {
            DraftData draftData = this.mDraftData.get(this.mCurrentPosition);
            String dirPath = draftData.getDirPath();
            DraftFileUtil.renameFile(dirPath, dirPath.replace(draftData.getFileName(), str));
            draftData.setFileName(str);
            ManageListAdapter manageListAdapter = this.mManageListAdapter;
            if (manageListAdapter != null) {
                manageListAdapter.notifyItemChanged(this.mCurrentPosition);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void copyDraft() {
        int i = this.mCurrentPosition;
        if (i >= 0 && i < this.mDraftData.size()) {
            DraftData copyDraft = DraftManager.getInstance().copyDraft(this, this.mDraftData.get(this.mCurrentPosition));
            if (this.mManageListAdapter != null && copyDraft != null) {
                this.mDraftData.add(copyDraft);
                this.mManageListAdapter.addNewDraft(copyDraft);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void closeMoreDialog() {
        Dialog dialog = this.mManagerMoreDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mManagerMoreDialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showMoreDialog(int i, DraftData draftData) {
        if (this.mManagerMoreDialog == null) {
            initMoreDialog();
        }
        this.mCurrentPosition = i;
        Dialog dialog = this.mManagerMoreDialog;
        if (dialog != null && !dialog.isShowing()) {
            this.mManagerMoreDialog.show();
        }
    }

    private void showDeleteDialog() {
        this.mLlDelete.setVisibility(View.VISIBLE);
    }

    private void closeDeleteDialog() {
        this.mLlDelete.setVisibility(View.GONE);
    }

    private void showPrivacyDialog() {
        final SpUtil instance = SpUtil.getInstance(getApplicationContext());
        if (!instance.getBoolean(Constants.KEY_AGREE_PRIVACY, false)) {
            PrivacyPolicyDialog privacyPolicyDialog = new PrivacyPolicyDialog(this, R.style.dialog);
            privacyPolicyDialog.setOnButtonClickListener(new PrivacyPolicyDialog.OnPrivacyClickListener() {
                /* class com.meishe.myvideo.activity.MainActivity.AnonymousClass11 */

                @Override // com.meishe.myvideo.view.PrivacyPolicyDialog.OnPrivacyClickListener
                public void onButtonClick(boolean z) {
                    instance.putBoolean(Constants.KEY_AGREE_PRIVACY, z);
                    if (!z) {
                        AppManager.getInstance().finishActivity();
                    }
                }

                @Override // com.meishe.myvideo.view.PrivacyPolicyDialog.OnPrivacyClickListener
                public void pageJumpToWeb(String str) {
                    String str2;
                    String string = DraftListActivity.this.getString(R.string.service_agreement);
                    String string2 = DraftListActivity.this.getString(R.string.privacy_policy);
                    if (str.contains(string)) {
                        str2 = SystemUtils.isZh(DraftListActivity.this) ? Constants.USER_AGREEMENTS : Constants.USER_AGREEMENTS_EN;
                    } else {
                        str2 = str.contains(string2) ? SystemUtils.isZh(DraftListActivity.this) ? Constants.PRIVACY_POLICY_URL : Constants.PRIVACY_POLICY_URL_EN : "";
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("URL", str2);
                        AppManager.getInstance().jumpActivity(DraftListActivity.this, MainWebViewActivity.class, bundle);
                    }
                }
            });
            privacyPolicyDialog.show();
        }
    }
}
