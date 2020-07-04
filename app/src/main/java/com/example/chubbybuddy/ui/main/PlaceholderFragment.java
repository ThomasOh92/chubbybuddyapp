package com.example.chubbybuddy.ui.main;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.chubbybuddy.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements SensorEventListener, StepListener {
    //Step variables
    private TextView TvSteps;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps = 0;
    private int fitnessLevel;
    private int dietLevel;
    private TextView levelView;
    private Button minusButton;
    private Button plusButton;
    private ImageView chubbyBuddyDisplay;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

        //Pedometer stuff
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        minusButton = root.findViewById(R.id.minus);
        plusButton = root.findViewById(R.id.plus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleMinus();
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                togglePlus();
            }
        });
        chubbyBuddyDisplay = root.findViewById(R.id.chubby_buddy_display);

        //Pedometer stuff
        TvSteps = (TextView) root.findViewById(R.id.tv_steps);

        //Diet or Fitness stuff
        levelView = root.findViewById(R.id.level);
        pageViewModel.getLevelText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s == "Your Fitness Level is ") {
                    fitnessLevel = getActivity().getPreferences(Context.MODE_PRIVATE).getInt("fitnessLevel", 5);
                    s += fitnessLevel;
                    setFitnessImage();
                    TvSteps.setText(TEXT_NUM_STEPS);
                } else {
                    dietLevel = getActivity().getPreferences(Context.MODE_PRIVATE).getInt("dietLevel", 5);
                    s+= dietLevel;
                    setDietImage();
                }
                levelView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        String viewText = levelView.getText().toString();
        String s;
        if ( viewText.contains("Fitness")) {
            TvSteps.setText(TEXT_NUM_STEPS + numSteps);
        }
    }

    public void toggleMinus(){
        String viewText = levelView.getText().toString();
        String s = "";
        if ( viewText.contains("Fitness") && fitnessLevel > 0) {
            fitnessLevel--;
            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt("fitnessLevel", fitnessLevel).apply();
            s = "Your Fitness level is " + fitnessLevel;
            setFitnessImage();
            levelView.setText(s);
        } else if (viewText.contains("Diet") && dietLevel > 0) {
            dietLevel--;
            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt("dietLevel", dietLevel).apply();
            s = "Your Diet level is " + dietLevel;
            setDietImage();
            levelView.setText(s);
        }
    }

    public void togglePlus(){
        String viewText = levelView.getText().toString();
        String s = "";
        if ( viewText.contains("Fitness") && fitnessLevel < 10) {
            fitnessLevel++;
            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt("fitnessLevel", fitnessLevel).apply();
            s = "Your Fitness level is " + fitnessLevel;
            setFitnessImage();
            levelView.setText(s);
        } else if (viewText.contains("Diet") && dietLevel < 10)  {
            dietLevel++;
            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt("dietLevel", dietLevel).apply();
            s = "Your Diet level is " + dietLevel;
            setDietImage();
            levelView.setText(s);
        }
    }

    public void setFitnessImage(){
        switch(fitnessLevel) {
            case 0:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_0);
                break;
            case 1:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_1);
                break;
            case 2:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_2);
                break;
            case 3:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_3);
                break;
            case 4:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_4);
                break;
            case 5:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_5);
                break;
            case 6:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_6);
                break;
            case 7:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_7);
                break;
            case 8:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_8);
                break;
            case 9:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_9);
                break;
            case 10:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_10);
                break;
            default:
                chubbyBuddyDisplay.setImageResource(R.drawable.fitnesslevel_5);
        }
    }

    public void setDietImage(){
        switch(dietLevel) {
            case 0:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_0);
                break;
            case 1:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_1);
                break;
            case 2:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_2);
                break;
            case 3:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_3);
                break;
            case 4:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_4);
                break;
            case 5:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_5);
                break;
            case 6:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_6);
                break;
            case 7:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_7);
                break;
            case 8:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_8);
                break;
            case 9:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_9);
                break;
            case 10:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_10);
                break;
            default:
                chubbyBuddyDisplay.setImageResource(R.drawable.dietlevel_5);
        }
    }
}