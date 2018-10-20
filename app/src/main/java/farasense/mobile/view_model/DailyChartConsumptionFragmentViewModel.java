package farasense.mobile.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import farasense.mobile.model.DAO.FaraSenseSensorDAO;
import farasense.mobile.model.realm.FaraSenseSensor;

public class DailyChartConsumptionFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<List<FaraSenseSensor>> faraSenseListObservable;

    public DailyChartConsumptionFragmentViewModel(Application application) { super(application); }

    public void getDailyConsumption() {
        List<FaraSenseSensor> faraSenseSensorDailyList = FaraSenseSensorDAO.getDailyComsumption();


    }


}
