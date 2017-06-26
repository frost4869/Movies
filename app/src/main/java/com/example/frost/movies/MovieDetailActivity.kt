package com.example.frost.movies

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.frost.movies.Utils.Constant
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.github.ksoichiro.android.observablescrollview.ScrollUtils
import com.nineoldandroids.view.ViewHelper
import com.nineoldandroids.view.ViewPropertyAnimator
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : BaseActivity(), ObservableScrollViewCallbacks {


    private var movie_id: String? = null
    private var poster_path: String? = null
    private var fab_margin: Int = 0
    private var fabIsShown: Boolean = false

    private var mFlexibleSpaceShowFabOffset: Int = 0
    private var mFlexibleSpaceImageHeight: Int = 0
    private var mActionBarSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val extra = intent
        if (extra != null) {
            movie_id = extra.getStringExtra("movie_id")
            poster_path = extra.getStringExtra("poster_path")

            Glide.with(this)
                    .load(Constant.getImage_base_url() + "original/" + poster_path)
                    .into(image)
        }

        mFlexibleSpaceImageHeight = resources.getDimensionPixelSize(R.dimen.flexible_space_image_height)
        mFlexibleSpaceShowFabOffset = resources.getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset)
        mActionBarSize = actionBarSize

        scroll.setScrollViewCallbacks(this)

        fab_margin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(fab, 0F)
        ViewHelper.setScaleY(fab, 0F)

        ScrollUtils.addOnGlobalLayoutListener(scroll) {
            scroll.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize)

            // If you'd like to start from scrollY == 0, don't write like this:
            //mScrollView.scrollTo(0, 0);
            // The initial scrollY is 0, so it won't invoke onScrollChanged().
            // To do this, use the following:
            //onScrollChanged(0, false, false);

            // You can also achieve it with the following codes.
            // This causes scroll change from 1 to 0.
            //mScrollView.scrollTo(0, 1);
            //mScrollView.scrollTo(0, 0);
        }
    }

    override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
    }

    override fun onDownMotionEvent() {
    }

    override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {
        // Translate overlay and image
        val flexibleRange = (mFlexibleSpaceImageHeight - mActionBarSize).toFloat()
        val minOverlayTransitionY = mActionBarSize - overlay.getHeight()
        ViewHelper.setTranslationY(overlay, ScrollUtils.getFloat((-scrollY).toFloat(), minOverlayTransitionY.toFloat(), 0f))
        ViewHelper.setTranslationY(image, ScrollUtils.getFloat((-scrollY / 2).toFloat(), minOverlayTransitionY.toFloat(), 0f))

        // Change alpha of overlay
        ViewHelper.setAlpha(overlay, ScrollUtils.getFloat(scrollY.toFloat() / flexibleRange, 0f, 1f))

        // Scale title text
        val scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0f, 0.3F)
        ViewHelper.setPivotX(view_title, 0f)
        ViewHelper.setPivotY(view_title, 0f)
        ViewHelper.setScaleX(view_title, scale)
        ViewHelper.setScaleY(view_title, scale)

        // Translate title text
        val maxTitleTranslationY = (mFlexibleSpaceImageHeight - view_title.getHeight() * scale).toInt()
        val titleTranslationY = maxTitleTranslationY - scrollY
        ViewHelper.setTranslationY(view_title, titleTranslationY.toFloat())

        // Translate FAB
        val maxFabTranslationY = mFlexibleSpaceImageHeight - fab.getHeight() / 2
        val fabTranslationY = ScrollUtils.getFloat(
                (-scrollY + mFlexibleSpaceImageHeight - fab.getHeight() / 2).toFloat(),
                (mActionBarSize - fab.getHeight() / 2).toFloat(),
                maxFabTranslationY.toFloat())

        ViewHelper.setTranslationX(fab, (overlay.getWidth() - fab_margin - fab.getWidth()).toFloat())
        ViewHelper.setTranslationY(fab, fabTranslationY)

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab()
        } else {
            showFab()
        }
    }

    private fun showFab() {
        if (!fabIsShown) {
            ViewPropertyAnimator.animate(fab).cancel()
            ViewPropertyAnimator.animate(fab).scaleX(1f).scaleY(1f).setDuration(200).start()
            fabIsShown = true
        }
    }

    private fun hideFab() {
        if (fabIsShown) {
            ViewPropertyAnimator.animate(fab).cancel()
            ViewPropertyAnimator.animate(fab).scaleX(0f).scaleY(0f).setDuration(200).start()
            fabIsShown = false
        }
    }
}
