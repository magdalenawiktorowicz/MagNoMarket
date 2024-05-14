package es.studium.magnomarket.ui.micuenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MiCuentaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MiCuentaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mi cuenta fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}