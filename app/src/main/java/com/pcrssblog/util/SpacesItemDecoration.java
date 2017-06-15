package com.pcrssblog.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Manage spaces between grids
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public SpacesItemDecoration(int pItemOffset) {
        this.mItemOffset = pItemOffset;
    }

    public SpacesItemDecoration(@NonNull final Context pContext, @DimenRes final int pItemOffsetId) {
        this(pContext.getResources().getDimensionPixelSize(pItemOffsetId));
    }

    @Override
    public void getItemOffsets(final Rect pOutRect, final View pView, final RecyclerView pParent, final RecyclerView.State pState) {
        super.getItemOffsets(pOutRect, pView, pParent, pState);
        pOutRect.set(this.mItemOffset, this.mItemOffset, this.mItemOffset, this.mItemOffset);
    }
}
