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
import farasense.mobile.databinding.ThirtyChartConsumptionFragmentDataBinding;
import farasense.mobile.view_model.ThirtyChartConsumptionFragmentViewModel;

public class ThirtyChartConsumptionFragment extends Fragment {

    private ThirtyChartConsumptionFragmentDataBinding binding;
    private ThirtyChartConsumptionFragmentViewModel viewModel;
    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_thirty_chart_consumption, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(ThirtyChartConsumptionFragmentViewModel.class);

        View view = binding.getRoot();

        binding.setThirtyCompumptionFragment(viewModel);

        return view;
    }

    @Override
    public LifecycleRegistry getLifecycle() { return registry; }

}
