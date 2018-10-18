package farasense.mobile.view.ui.activity.fragment;

import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.annotation.Nullable;

public class DailyChartConsumptionFragment extends Fragment {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return null;
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }
}
