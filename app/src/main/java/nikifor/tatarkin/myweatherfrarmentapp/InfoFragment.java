package nikifor.tatarkin.myweatherfrarmentapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedPressureEvent;
import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedSpeedEvent;

public class InfoFragment extends Fragment implements Constants {
    private static final String INDEX_CITY = "index";
    private static final String KEY_TEXT_SPEED = "key speed";
    private static final String KEY_TEXT_PRESSURE = "key pressure";

    private Button buttonAboutCityBrowser;
    private TextView textSpeedWind;
    private TextView textPressure;
    private TextView textNameCity;

    boolean visibleSpeedBoolean;
    boolean visiblePressureBoolean;

    // Фабричный метод создания фрагмента
    public static InfoFragment create(int index, boolean visibleSpeed, boolean visiblePressure) {
        InfoFragment infoFragment = new InfoFragment();    // создание

        // Передача параметров
        Bundle args = new Bundle();
        args.putInt(INDEX_CITY, index);
        args.putBoolean(SPEED_VISIBLE, visibleSpeed);
        args.putBoolean(PRESSURE_VISIBLE, visiblePressure);
        infoFragment.setArguments(args);
        return infoFragment;
    }

    // Получить индекс из списка (фактически из параметра)
    public int getIndex() {
        int index = getArguments().getInt(INDEX_CITY, 0);
        return index;
    }

    public boolean getVisibSpeed(){
        visibleSpeedBoolean = getArguments().getBoolean(SPEED_VISIBLE);
        return visibleSpeedBoolean;
    }

    public boolean getVisibPressure(){
        visiblePressureBoolean = getArguments().getBoolean(PRESSURE_VISIBLE);
        return visiblePressureBoolean;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Определить какой город надо показать, и показать его
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setTextNameCity();

        //Показать-скрыть скоростьи давление при открытии фрагмента на новой активити.
        setVisibilitySpeed();
        setVisibilityPressure();


        openBrowser();
    }

    //Инициализация полей
    private void initViews(@NonNull View view) {
        buttonAboutCityBrowser = view.findViewById(R.id.button_browser);
        textSpeedWind = view.findViewById(R.id.textSpeedWind);
        textPressure = view.findViewById(R.id.textPressure);
        textNameCity = view.findViewById(R.id.idCity);
    }

    //Открытие браузера
    private void openBrowser() {
        buttonAboutCityBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.wiki_url).toString() + textNameCity.getText().toString();
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
            }
        });
    }

    //Запись названия города
    private void setTextNameCity(){
        String[] cities = getResources().getStringArray(R.array.cities);
        textNameCity.setText(cities[getIndex()]);
    }

    //Определие скрыть/показать давление.
    private void setVisibilityPressure() {
        textPressure.setVisibility(getVisibPressure()? View.VISIBLE : View.INVISIBLE);
    }
    //Определение скрыть/показать скорость.
    private void setVisibilitySpeed() {
        textSpeedWind.setVisibility(getVisibSpeed()? View.VISIBLE : View.GONE);
    }


    //Показать/Скрыть скорость ветка и давление при открытии фрагмента на той же активити- начало.
    @Subscribe
    @SuppressWarnings("unused")
    public void onFragmentBtnClickEvent(FragmentBtnClickedSpeedEvent event) {
        textSpeedWind.setVisibility(event.visibilitySpeed? View.VISIBLE : View.GONE);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onFragmentBtnClickEvent(FragmentBtnClickedPressureEvent event) {
        textPressure.setVisibility(event.visibilityPressure? View.VISIBLE : View.GONE);
    }
    //Показать/Скрыть скорость ветка и давление - конец.


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);

    }
    @Override
    public void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();

    }
}
