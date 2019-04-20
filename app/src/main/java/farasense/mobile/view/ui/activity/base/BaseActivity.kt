package farasense.mobile.view.ui.activity.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.miguelcatalan.materialsearchview.MaterialSearchView
import butterknife.BindView
import farasense.mobile.R
import farasense.mobile.view.components.FaraSenseTextViewBold
import farasense.mobile.view.components.FaraSenseTextViewRegular

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    @BindView(R.id.toolbar)
    protected var toolbar: Toolbar? = null
    @BindView(R.id.search_view)
    protected var searchView: MaterialSearchView? = null
    @BindView(R.id.toolbar_title)
    protected var toolbarTitle: FaraSenseTextViewBold? = null
    @BindView(R.id.toolbar_subtitle)
    protected var toolbarSubtitle: FaraSenseTextViewRegular? = null

    protected fun setToolbar(title: String, subtitle: String?, homeAsUpEnable: Boolean) {
        if (toolbar != null) {
            toolbar!!.title = ""
            toolbarTitle!!.text = title
            if (subtitle != null) {
                toolbarSubtitle!!.visibility = View.VISIBLE
                toolbarSubtitle!!.text = subtitle
            }
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(homeAsUpEnable)
        }

        //        final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        //        if (backArrow != null) {
        //            backArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        //            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        //        }
    }

    companion object {
        fun startActivity(activity: Activity, classGoTo: Class<*>, finishActivity: Boolean) {
            val intent = Intent(activity, classGoTo)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            activity.startActivity(intent)
            if (finishActivity) {
                activity.finish()
            }
        }
    }


}
