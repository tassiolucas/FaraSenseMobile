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
import farasense.mobile.databinding.YearlyChartConsumptionFragmentDataBinding;
import farasense.mobile.view_model.YearlyChartConsumptionFragmentViewModel;

public class YearlyChartConsumptionFragment extends Fragment {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private YearlyChartConsumptionFragmentDataBinding binding;
    private YearlyChartConsumptionFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yearly_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(YearlyChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        binding.setYearlyCompumptionFragment(viewModel);

        return view;
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}
