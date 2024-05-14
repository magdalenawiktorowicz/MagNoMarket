package es.studium.magnomarket.ui.midespensa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MiDespensaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MiDespensaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mi despensa fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}