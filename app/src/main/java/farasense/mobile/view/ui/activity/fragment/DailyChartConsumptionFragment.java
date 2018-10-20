package farasense.mobile.view.ui.activity.fragment;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.annotation.Nullable;

import farasense.mobile.R;
import farasense.mobile.databinding.DailyChartConsumptionFragmentDataBinding;
import farasense.mobile.view_model.DailyChartConsumptionFragmentViewModel;

public class DailyChartConsumptionFragment extends Fragment {

    private DailyChartConsumptionFragmentDataBinding binding;
    private DailyChartConsumptionFragmentViewModel viewModel;
    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(DailyChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        binding.setDailyCompumptionFragment(viewModel);

        viewModel.getDailyConsumption();

        return view;
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}
