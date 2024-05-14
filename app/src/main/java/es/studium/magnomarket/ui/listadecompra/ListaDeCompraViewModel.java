package es.studium.magnomarket.ui.listadecompra;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListaDeCompraViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ListaDeCompraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is lista de compra fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}