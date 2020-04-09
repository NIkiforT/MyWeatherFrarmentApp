package nikifor.tatarkin.myweatherfrarmentapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedPressureEvent;
import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedSpeedEvent;
import nikifor.tatarkin.myweatherfrarmentapp.CoatContainer;
import nikifor.tatarkin.myweatherfrarmentapp.Constants;
import nikifor.tatarkin.myweatherfrarmentapp.R;
import nikifor.tatarkin.myweatherfrarmentapp.model.WeatherRequest;
import nikifor.tatarkin.myweatherfrarmentapp.recyclerView.TemperatureAdapter;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class InfoFragment extends Fragment implements Constants {
    private static final String INDEX_CITY = "index";
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=";
    private static final String WEATHER_API_KEY = "240af58b6f095eb759a3ecd2d282d448";

    private Button buttonAboutCityBrowser;
    private TextView textSpeedWind;
    private TextView textPressure;
    private TextView textNameCity;
    private ImageView imageSpeedWind;
    private ImageView imagePressure;
    private TextView textTemp;

    private RecyclerView recyclerView;

    //Фейковые данные для температуры на 3 дня.
    private String[] listTemperature = new String[]{"-", "-", "-"};

    // Фабричный метод создания фрагмента
    public static InfoFragment create(CoatContainer container) {
        InfoFragment infoFragment = new InfoFragment();    // создание

        // Передача параметров
        Bundle args = new Bundle();
        args.putSerializable(INDEX_CITY, container);
        infoFragment.setArguments(args);
        return infoFragment;
    }

    // Получить индекс из списка (фактически из параметра)
    public int getIndex() {
        CoatContainer coatContainer = (CoatContainer) (Objects.requireNonNull(getArguments())
                .getSerializable(INDEX_CITY));
        try {
            return coatContainer.position;
        } catch (Exception e) {
            return 0;
        }
    }

    public String getNameCity(){
        CoatContainer coatContainer = (CoatContainer) getArguments().getSerializable(INDEX_CITY);
            try {
                return coatContainer.cityName;
            }catch (Exception e){
                return "-";
            }
    }

    //Получить информацию о том, нужно ли показывать скорость ветра
    public boolean getVisibSpeed(){
        CoatContainer coatContainer = (CoatContainer) (Objects.requireNonNull(getArguments())
                .getSerializable(INDEX_CITY));
        try {
            return coatContainer.visibilitySpeed;
        } catch (Exception e) {
            return true;
        }
    }

    //Получить информацию о том, нужно ли показывать давление
    public boolean getVisibPressure(){
        CoatContainer coatContainer = (CoatContainer) (Objects.requireNonNull(getArguments())
                .getSerializable(INDEX_CITY));
        try {
            return coatContainer.visibilityPressure;
        } catch (Exception e) {
            return true;
        }
    }

    public String getTemp(){
        CoatContainer coatContainer = (CoatContainer) getArguments().getSerializable(INDEX_CITY);
        try {
            return coatContainer.temp;
        }catch (Exception e){
            return "-";
        }
    }
    public String getSpeed(){
        CoatContainer coatContainer = (CoatContainer) getArguments().getSerializable(INDEX_CITY);
        try {
            return coatContainer.speed;
        }catch (Exception e){
            return "-";
        }
    }
    public String getPressure(){
        CoatContainer coatContainer = (CoatContainer) getArguments().getSerializable(INDEX_CITY);
        try {
            return coatContainer.pressure;
        }catch (Exception e){
            return "-";
        }
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
        initRecyclerView(view);
        setTextNameCity();
        setTextSpeed();
        setTextPressure();
        setTextTemp();

        //Закрузка данных о погоде с сервера.
        loadWeather(view);

        //Показать-скрыть скоростьи давление при открытии фрагмента на новой активити.
        setVisibilitySpeed();
        setVisibilityPressure();

        openBrowser();
    }

    // Инциализация RecyclerView для показа температуры на3 дня.  + Декоратор
    private void initRecyclerView(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recycler_view_temp);

        //Декоратор - начало.
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), HORIZONTAL);
        itemDecoration.setDrawable(getContext().getDrawable(R.drawable.separator_temperature));
        recyclerView.addItemDecoration(itemDecoration);
        //Декоратор - конец.

        TemperatureAdapter adapter = new TemperatureAdapter(listTemperature);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }

    //Инициализация полей
    private void initViews(@NonNull View view) {
        buttonAboutCityBrowser = view.findViewById(R.id.button_browser);
        textSpeedWind = view.findViewById(R.id.textSpeedWind);
        textPressure = view.findViewById(R.id.textPressure);
        textNameCity = view.findViewById(R.id.idCity);
        imageSpeedWind = view.findViewById(R.id.speedWindImag);
        imagePressure = view.findViewById(R.id.pressureImag);
        textTemp = view.findViewById(R.id.temperature);

    }

    //Открытие браузера
    private void openBrowser() {
        buttonAboutCityBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Open the browser?", Snackbar.LENGTH_LONG)
                        .setAction("Open", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = getString(R.string.wiki_url).toString() + textNameCity.getText().toString();
                                Uri uri = Uri.parse(url);
                                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(browser);
                            }
                        }).show();
            }
        });
    }

    //Запись названия города
    private void setTextNameCity(){
        textNameCity.setText(getNameCity());
    }

    private void setTextTemp(){
        textTemp.setText(getTemp());
    }
    private void setTextSpeed(){
        textSpeedWind.setText(getSpeed());
    }
    private void setTextPressure(){
        textPressure.setText(getPressure());
    }


    //Определие скрыть/показать давление при открытии новой Activity.
    private void setVisibilityPressure() {
        textPressure.setVisibility(getVisibPressure()? View.VISIBLE : View.INVISIBLE);
        imagePressure.setVisibility(getVisibPressure()? View.VISIBLE : View.INVISIBLE);
    }
    //Определение скрыть/показать скорость при открытии новой Activity
    private void setVisibilitySpeed() {
        textSpeedWind.setVisibility(getVisibSpeed()? View.VISIBLE : View.GONE);
        imageSpeedWind.setVisibility(getVisibSpeed()? View.VISIBLE : View.GONE);
    }


    //Показать/Скрыть скорость ветка и давление при открытии фрагмента на той же активити- начало.
    @Subscribe
    @SuppressWarnings("unused")
    public void onFragmentBtnClickEvent(FragmentBtnClickedSpeedEvent event) {
        textSpeedWind.setVisibility(event.visibilitySpeed? View.VISIBLE : View.GONE);
        imageSpeedWind.setVisibility(event.visibilitySpeed? View.VISIBLE : View.GONE);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onFragmentBtnClickEvent(FragmentBtnClickedPressureEvent event) {
        textPressure.setVisibility(event.visibilityPressure? View.VISIBLE : View.GONE);
        imagePressure.setVisibility(event.visibilityPressure? View.VISIBLE : View.GONE);
    }
    //Показать/Скрыть скорость ветка и давление - конец.

    //Переопределенные методы для использования EventBus. - начало.
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
    //Переопределенные методы для использования EventBus. - конец.

    //Запрос и получение данных о погоде.
    private void loadWeather(View view){
        try{
            final String cityUpdate = String.format(WEATHER_URL, textNameCity.getText().toString());
            final URL uri = new URL(cityUpdate + WEATHER_API_KEY);
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = getLines(in);
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayWeather(weatherRequest);

                            }
                        });

                    }catch (Exception e){
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    }finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getLines(BufferedReader in){
        return in.lines().collect(Collectors.joining("\n"));
    }

    //Запись данных в TextView (температура, скорость, давление)
    private void displayWeather(WeatherRequest weatherRequest) {
        textTemp.setText(String.format("%.2f", weatherRequest.getMain().getTemp()) + "\u2109");
        textSpeedWind.setText(String.format("%d", weatherRequest.getWind().getSpeed()) + " m/s");
        textPressure.setText(String.format("%d", weatherRequest.getMain().getPressure()) + " hPa");
    }
}
