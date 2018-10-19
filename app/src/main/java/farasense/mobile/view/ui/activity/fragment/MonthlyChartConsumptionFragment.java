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
import farasense.mobile.databinding.MonthlyChartConsumptionFragmentDataBinding;
import farasense.mobile.view_model.MonthlyChartConsumptionFragmentViewModel;

public class MonthlyChartConsumptionFragment extends Fragment {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private MonthlyChartConsumptionFragmentDataBinding binding;
    private MonthlyChartConsumptionFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monthly_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(MonthlyChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        binding.setMonthlyCompumptionFragment(viewModel);

        return view;
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}
