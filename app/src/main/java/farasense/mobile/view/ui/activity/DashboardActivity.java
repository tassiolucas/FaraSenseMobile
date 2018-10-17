package farasense.mobile.view.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import farasense.mobile.R;
import farasense.mobile.databinding.DashboardDataBinding;
import farasense.mobile.view.ui.activity.base.BaseActivity;

public class DashboardActivity extends BaseActivity {

    private DashboardDataBinding binding;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        ButterKnife.bind(this);

        setToolbar(getString(R.string.welcome_label), null, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
