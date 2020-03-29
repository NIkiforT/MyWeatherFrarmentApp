package nikifor.tatarkin.myweatherfrarmentapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.otto.Subscribe;

import java.util.Objects;

import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedPressureEvent;
import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedSpeedEvent;
import nikifor.tatarkin.myweatherfrarmentapp.CoatContainer;
import nikifor.tatarkin.myweatherfrarmentapp.Constants;
import nikifor.tatarkin.myweatherfrarmentapp.R;
import nikifor.tatarkin.myweatherfrarmentapp.TemperatureAdapter;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class InfoFragment extends Fragment implements Constants {
    private static final String INDEX_CITY = "index";

    private Button buttonAboutCityBrowser;
    private TextView textSpeedWind;
    private TextView textPressure;
    private TextView textNameCity;

    private RecyclerView recyclerView;

    //Фейковые данные для температуры на 3 дня.
    private String[] listTemperature = new String[]{"+ 24'", "+ 23'", "+ 19'"};

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

    //Переопределенные методы для использования EventBus.
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
