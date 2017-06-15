package com.pcrssblog.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;

/**
 * A utility class that has common methods used for views
 */
public class ViewUtils {
    /**
     * Handle view nullability
     *
     * @param pReference
     * @param pErrorMessage
     * @param <T>
     * @return
     */
    public static <T> T checkNotNull(final T pReference, @Nullable final Object pErrorMessage) {
        if (pReference == null) {
            throw new NullPointerException(String.valueOf(pErrorMessage));
        } else {
            return pReference;
        }
    }

    /**
     * Handle view nullability
     *
     * @param pReference
     * @param <T>
     * @return
     */
    public static <T> T checkNotNull(final T pReference) {
        if (pReference == null) {
            throw new NullPointerException();
        } else {
            return pReference;
        }
    }

    /**
     * Add fragment (view) to activity
     *
     * @param pFragmentManager
     * @param pFragment
     */
    public static void addViewToActivity(@NonNull final FragmentManager pFragmentManager, @NonNull final Fragment pFragment) {
        checkNotNull(pFragmentManager);
        checkNotNull(pFragment);
        final FragmentTransaction transaction = pFragmentManager.beginTransaction();
        transaction.add(android.R.id.content, pFragment);
        transaction.commit();
    }

    /**
     * @param pContext
     * @param pDp
     * @return Pixels from DP
     */
    public static int getPixelsFromDP(final Context pContext, final int pDp) {
        final Resources resources = pContext.getResources();
        return (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            pDp,
            resources.getDisplayMetrics()
        );
    }
}