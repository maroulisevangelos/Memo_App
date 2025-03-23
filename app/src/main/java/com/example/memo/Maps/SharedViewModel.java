package com.example.memo.Maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> destinationData = new MutableLiveData<>();

    public LiveData<String> getDestinationData() {
        return destinationData;
    }

    public void setDestinationData(String data) {
        destinationData.setValue(data);
    }

}