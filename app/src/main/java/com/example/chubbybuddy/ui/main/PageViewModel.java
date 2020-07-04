package com.example.chubbybuddy.ui.main;

import android.content.Context;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            if (input == 1){
                return "Chubby Buddy Fitness";
            } else {
                return "Chubby Buddy Diet";
            }
        }
    });



    private LiveData<String> levelText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            if (input == 1){
                return "Your Fitness Level is ";
            } else {
                return "Your Diet Level is ";
            }

        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getLevelText() {
        return levelText;
    }
}