package io.mobiledriver;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Toshiba on 2015-05-25.
 */
public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private JSONArray array;
    private EditText licensePlate;
    private EditText startDate;
    private EditText endDate;
    private EditText area;

    public SpinnerListener(JSONArray array, EditText licensePlate, EditText startDate,
                           EditText endDate, EditText area)
    {
        this.array = array;
        this.licensePlate = licensePlate;
        this. startDate = startDate;
        this.endDate = endDate;
        this.area = area;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.i("Selected: ", String.valueOf(position));
        try {
            Log.i("Value of selected: ", this.array.getString(position));
            this.licensePlate.setText(this.array.getJSONObject(position).getString("registrationNumber"));
            this.startDate.setText(this.array.getJSONObject(position).getString("startDate"));
            this.endDate.setText(this.array.getJSONObject(position).getString("endDate"));
            this.area.setText(this.array.getJSONObject(position).getString("areaId"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
