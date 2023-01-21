package com.deal.bytee.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import kotlin.jvm.internal.Intrinsics;

public class BaseActivity extends AppCompatActivity {
    

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = getWindow();
            Intrinsics.checkExpressionValueIsNotNull(window, "window");
            View decorView = window.getDecorView();
            Intrinsics.checkExpressionValueIsNotNull(decorView, "window.decorView");
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    public final void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(color);
        }
    }


    public final void showSnackbar(String message, int actionStrId, View.OnClickListener listener) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Snackbar snackbar = Snackbar.make(findViewById(actionStrId), (CharSequence) message, BaseTransientBottomBar.LENGTH_SHORT);
        Intrinsics.checkExpressionValueIsNotNull(snackbar, "Snackbar.make(\n         …   LENGTH_SHORT\n        )");
        if (!(actionStrId == 0 || listener == null)) {
            snackbar.setAction((CharSequence) getString(actionStrId), listener);
        }
        snackbar.show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Intrinsics.checkParameterIsNotNull(permissions, "permissions");
        Intrinsics.checkParameterIsNotNull(grantResults, "grantResults");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onBackPressed() {
        handleBackStack();
    }

    public final void handleBackStack() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            Intrinsics.checkExpressionValueIsNotNull(fm, "supportFragmentManager");
            for (Fragment frag : fm.getFragments()) {
                Intrinsics.checkExpressionValueIsNotNull(frag, "frag");
                FragmentManager childFragmentManager = frag.getChildFragmentManager();
                Intrinsics.checkExpressionValueIsNotNull(childFragmentManager, "frag.childFragmentManager");
                if (frag.isVisible() && childFragmentManager.getBackStackEntryCount() > 0) {
                    int backStackEntryCount = childFragmentManager.getBackStackEntryCount();
                    for (int i = 0; i < backStackEntryCount; i++) {
                        Object obj = childFragmentManager.getFragments().get(i);
                        Intrinsics.checkExpressionValueIsNotNull(obj, "childFragmentManager.fragments[i]");
                        if (((Fragment) obj).isVisible()) {
                            childFragmentManager.popBackStack();
                            return;
                        }
                    }
                    continue;
                }
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
