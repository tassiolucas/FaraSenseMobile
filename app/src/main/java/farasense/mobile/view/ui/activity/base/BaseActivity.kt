package farasense.mobile.view.ui.activity.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    // TODO: Implementar separadamente nas Activitys
//    @BindView(R.id.toolbar)
//    protected lateinit var toolbar: Toolbar
//    @BindView(R.id.search_view)
//    protected lateinit var searchView: MaterialSearchView
//    @BindView(R.id.toolbar_title)
//    protected lateinit var toolbarTitle: FaraSenseTextViewBold
//    @BindView(R.id.toolbar_subtitle)
//    protected lateinit var toolbarSubtitle: FaraSenseTextViewRegular

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
