package farasense.mobile.view.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import javax.annotation.Nullable;

import butterknife.BindView;
import farasense.mobile.R;
import farasense.mobile.view.components.FaraSenseTextViewBold;
import farasense.mobile.view.components.FaraSenseTextViewRegular;

public class BaseActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @Nullable @BindView(R.id.search_view)
    protected MaterialSearchView searchView;
    @Nullable @BindView(R.id.toolbar_title)
    protected FaraSenseTextViewBold toolbarTitle;
    @Nullable @BindView(R.id.toolbar_subtitle)
    protected FaraSenseTextViewRegular toolbarSubtitle;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    protected void setToolbar(final String title, final String subtitle, boolean homeAsUpEnable) {
        toolbar.setTitle("");
        toolbarTitle.setText(title);
        if (subtitle != null) {
            toolbarSubtitle.setVisibility(View.VISIBLE);
            toolbarSubtitle.setText(subtitle);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnable);

//        final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
//        if (backArrow != null) {
//            backArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(backArrow);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void startActivity(final Activity activity, final Class<?> classGoTo, final Boolean finishActivity) {
        Intent intent = new Intent(activity, classGoTo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        if (finishActivity) {
            activity.finish();
        }
    }


}
