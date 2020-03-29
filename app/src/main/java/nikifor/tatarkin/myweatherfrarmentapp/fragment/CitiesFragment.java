package nikifor.tatarkin.myweatherfrarmentapp.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import nikifor.tatarkin.myweatherfrarmentapp.CitiesWeatherInfo;
import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedPressureEvent;
import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedSpeedEvent;
import nikifor.tatarkin.myweatherfrarmentapp.CoatContainer;
import nikifor.tatarkin.myweatherfrarmentapp.Constants;
import nikifor.tatarkin.myweatherfrarmentapp.R;
import nikifor.tatarkin.myweatherfrarmentapp.recyclerView.RecyclerCitiesAdapter;

public class CitiesFragment extends Fragment implements Constants {

    private static final String KEY_CURRENT_CITY = "CurrentCity";
    private static final String INDEX_CITY = "index";
    private static final String KEY_TEXT_SPEED = "key speed";
    private static final String KEY_TEXT_PRESSURE = "key pressure";


    private boolean isExistInfo; //Можно ли расположить рядом информацию о погоде.
    private int currentPosition = 0; //позиция в списке городов
    private boolean checkedSpeedBoolean; //показать скрыть скорость ветра
    private boolean checkedPressureBoolean; //показать скрыть давление

    private String[] citiesContainer; //Список город.

    private CheckBox checkSpeed;
    private CheckBox checkPressure;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Заполнение массива из ресурсов.
        citiesContainer = getResources().getStringArray(R.array.cities);

        initChecks(view);
        initRecyclerViewCities(view);

        //Запись в boolean переменные информацию о показе скорости ветра и температуры.
        isCheckedSpeed();
        isCheckedPressure();

        //Обработка чекбоксов при открытии второго фрагмента на первой активити.
        clickSpeedBox();
        clickPressureBox();
    }

    //Инициализация RecyclerView со списком городов.
    //Добавлениея слушателя.
    private void initRecyclerViewCities(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_cities);
        RecyclerCitiesAdapter adapter = new RecyclerCitiesAdapter(citiesContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        //Добавление слушателя.
        adapter.SetOnItemClickListener(new RecyclerCitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentPosition = position;
                showCoatOfInfo();
            }
        });
    }


    //Нажаты ли чек боксы - начало
    private void isCheckedPressure() {
        checkedPressureBoolean = checkPressure.isChecked();
    }
    private void isCheckedSpeed() {
        checkedSpeedBoolean = checkSpeed.isChecked();
    }
    //Нажаты ли чек боксы - конец

    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Определение, можно ли будет расположить рядом информацию о погоде в другом фрагменте
        isExistInfo = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции города.
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_CITY, 0);
            checkedSpeedBoolean = savedInstanceState.getBoolean(KEY_TEXT_SPEED);
            checkedPressureBoolean = savedInstanceState.getBoolean(KEY_TEXT_PRESSURE);

        }

        // Если можно показать рядом погоду, то сделаем это
        if (isExistInfo) {
            showCoatOfInfo();
        }
    }

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_CURRENT_CITY, currentPosition);
        outState.putBoolean(KEY_TEXT_SPEED, checkedSpeedBoolean);
        outState.putBoolean(KEY_TEXT_PRESSURE, checkedPressureBoolean);

        super.onSaveInstanceState(outState);
    }

    //Инициализация чекбоксов
    private void initChecks(View view) {
        checkSpeed = view.findViewById(R.id.checkBoxSpeed);
        checkSpeed.setChecked(true);
        checkPressure = view.findViewById(R.id.checkBoxPressure);
        checkPressure.setChecked(true);
    }


    //Открытие нового фрагмента с информацией.
    private void showCoatOfInfo() {
        if (isExistInfo) {
            // Проверим, что фрагмент с погодой существует в activity
            InfoFragment detail = (InfoFragment)
                    getFragmentManager().findFragmentById(R.id.coat_of_info);

            // Если есть необходимость, то выведем информацию о погоде
            if (detail == null || detail.getIndex() != currentPosition) {
                // Создаем новый фрагмент с текущей позицией для вывода погоды
                detail = InfoFragment.create(getCoatContainer());
                // Выполняем транзакцию по замене фрагмента
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.coat_of_info, detail);  // замена фрагмента
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            // Если нельзя вывести информацию рядом, откроем вторую activity
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), CitiesWeatherInfo.class);
            // и передадим туда параметры
            intent.putExtra(INDEX_CITY, getCoatContainer());
            startActivity(intent);
        }
    }

    //Скрыть/показать скорость ветра во втором фрагменте.
    private void clickSpeedBox(){
        checkSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getBus().post(new FragmentBtnClickedSpeedEvent(checkSpeed.isChecked()));
            }
        });
    }

    //Скрыть/показать давление во втором фрагменте.
    private void clickPressureBox(){
        checkPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getBus().post(new FragmentBtnClickedPressureEvent(checkPressure.isChecked()));
            }
        });
    }

    //Создание контейнера с информацией о позиции, названии города и передача состояния чек боксов (показать/скрыть)
    private CoatContainer getCoatContainer() {
        String[] cities = getResources().getStringArray(R.array.cities);
        CoatContainer container = new CoatContainer();
        container.position = currentPosition;
        container.cityName = cities[currentPosition];
        container.visibilitySpeed = checkSpeed.isChecked();
        container.visibilityPressure = checkPressure.isChecked();
        return container;
    }

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
