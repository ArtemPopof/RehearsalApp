package ru.abbysoft.rehearsapp.util

import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener

fun longIdToString(long: Long): String {
    return "img$long"
}

fun SlidingUpPanelLayout.restrictExpanding() {
    val panelItself = this
    this.addPanelSlideListener(object: PanelSlideListener {
        override fun onPanelSlide(panel: View?, slideOffset: Float) {
            if (panelItself.height > panelItself.anchorPoint) {
            }
        }

        override fun onPanelStateChanged(
            panel: View?,
            previousState: SlidingUpPanelLayout.PanelState?,
            newState: SlidingUpPanelLayout.PanelState?
        ) {

        }

    })
}