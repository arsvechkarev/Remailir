package com.arsvechkarev.friends.presentation.pagerscreens

import android.view.Gravity.CENTER
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.friends.R
import com.arsvechkarev.views.ComplexProgressBar
import com.arsvechkarev.views.R.string
import com.arsvechkarev.views.Snackbar
import core.resources.Colors
import core.resources.Dimens
import core.resources.Dimens.MarginBig
import core.resources.Dimens.ProgressBarSizeBig
import core.resources.Styles
import core.resources.Styles.BoldTextView
import core.resources.Styles.ClickableButton
import core.resources.TextSizes
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.classNameTag
import viewdsl.gravity
import viewdsl.id
import viewdsl.image
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.paddings
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

fun CoordinatorLayout.LayoutSuccess(
  recyclerViewId: Int,
  adapter: RecyclerView.Adapter<*>
) = withViewBuilder {
  child<RecyclerView>(MatchParent, MatchParent) {
    id(recyclerViewId)
    invisible()
    paddings(top = 16.dp)
    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
  }
}

fun CoordinatorLayout.LayoutLoading(
  layoutLoadingId: Int,
  layoutLoadingStringResId: Int
) = withViewBuilder {
  VerticalLayout(MatchParent, WrapContent) {
    id(layoutLoadingId)
    invisible()
    gravity(CENTER)
    layoutGravity(CENTER)
    TextView(MatchParent, WrapContent, style = BoldTextView) {
      gravity(CENTER)
      textSize(TextSizes.H3)
      text(layoutLoadingStringResId)
    }
    child<ComplexProgressBar>(ProgressBarSizeBig, ProgressBarSizeBig) {
      margins(top = MarginBig)
    }
  }
}

fun CoordinatorLayout.LayoutNoData(
  layoutNoDataId: Int,
  layoutNoDataStringRes: Int
) = withViewBuilder {
  VerticalLayout(MatchParent, WrapContent) {
    id(layoutNoDataId)
    invisible()
    paddingHorizontal(40.dp)
    gravity(CENTER)
    layoutGravity(CENTER)
    TextView(MatchParent, WrapContent, style = BoldTextView) {
      gravity(CENTER)
      textSize(TextSizes.H1)
      text(layoutNoDataStringRes)
    }
    TextView(MatchParent, WrapContent, style = Styles.BaseTextView) {
      gravity(CENTER)
      textSize(TextSizes.H4)
      margins(top = 32.dp)
      text(R.string.text_add_people_to_friends)
    }
    TextView(WrapContent, WrapContent, style = ClickableButton()) {
      margins(top = 32.dp)
      text(R.string.text_find_people)
    }
  }
}

fun CoordinatorLayout.LayoutFailure(
  layoutFailureId: Int,
  textFailureId: Int,
  textRetryId: Int,
) = withViewBuilder {
  VerticalLayout(MatchParent, MatchParent) {
    id(layoutFailureId)
    invisible()
    gravity(CENTER)
    ImageView(Dimens.FailureLayoutImageSize, Dimens.FailureLayoutImageSize) {
      image(R.drawable.image_unknown_error)
    }
    TextView(WrapContent, WrapContent, style = BoldTextView) {
      id(textFailureId)
      textSize(TextSizes.H3)
      gravity(CENTER)
      paddingVertical(24.dp)
      marginHorizontal(32.dp)
    }
    TextView(WrapContent, WrapContent, style = ClickableButton(
      Colors.ErrorGradientStart, Colors.ErrorGradientEnd
    )) {
      id(textRetryId)
      textSize(TextSizes.H3)
      text(string.text_retry)
    }
  }
}

fun CoordinatorLayout.SnackBar() = withViewBuilder {
  child<Snackbar>(MatchParent, WrapContent, style = Styles.Snackbar) {
    classNameTag()
  }
}