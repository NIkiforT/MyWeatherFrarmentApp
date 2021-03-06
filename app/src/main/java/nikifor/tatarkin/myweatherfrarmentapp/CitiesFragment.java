package nikifor.tatarkin.myweatherfrarmentapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedPressureEvent;
import nikifor.tatarkin.myweatherfrarmentapp.ClickedEvent.FragmentBtnClickedSpeedEvent;

public class CitiesFragment extends Fragment implements Constants {

    private static final String KEY_CURRENT_CITY = "CurrentCity";
    private static final String INDEX_CITY = "index";
    private static final String KEY_TEXT_SPEED = "key speed";
    private static final String KEY_TEXT_PRESSURE = "key pressure";



    boolean isExistInfo; //Можно ли расположить рядом информацию о погоде.
    int currentPosition = 0; //позиция в списке городов
    boolean checkedSpeedBoolean; //показать скрыть скорость ветра
    boolean checkedPressureBoolean; //показать скрыть давление

    LinearLayout linearCities;
    CheckBox checkSpeed;
    CheckBox checkPressure;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListCities(view);
        initChecks(view);

        isCheckedSpeed();
        isCheckedPressure();

        clickSpeedBox();
        clickPressureBox();
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

    //Инициализация списка городов
    private void initListCities(View view) {
        linearCities = view.findViewById(R.id.container_cities);
        linearCities.setPadding(10,0,40,0);
        String[] cities = getResources().getStringArray(R.array.cities);

        //Внешние отступы
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        layoutParams.setMargins(0,15,0,0);

        // В этом цикле создаем элемент TextView,
        // заполняем его значениями,
        // и добавляем на экран.
        //создаем обработку касания на элемент
        for (int i = 0; i < cities.length; i++) {
            String city = cities[i];
            TextView tv = new TextView(getContext());
            tv.setText(city);
            tv.setTextSize(28);
            tv.setBackground(getContext().getDrawable(R.drawable.button_stroke_black95_press_white));
            tv.setPadding(15, 0,0,0);
            tv.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            tv.setLayoutParams(layoutParams);
            linearCities.addView(tv);
            final int fiPosition = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = fiPosition;
                    showCoatOfInfo();
                }
            });
        }
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
                detail = InfoFragment.create(currentPosition, checkedSpeedBoolean, checkedPressureBoolean);
                // Выполняем транзакцию по замене фрагмента
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.coat_of_info, detail);  // замена фрагмента
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            // Если нельзя вывести информацию рядом, откроем вторую activity
            Intent intent = new Intent();
            intent.setClass(getActivity(), CitiesWeatherInfo.class);
            // и передадим туда параметры
            intent.putExtra(INDEX_CITY, currentPosition);
            intent.putExtra(SPEED_VISIBLE, checkSpeed.isChecked());
            intent.putExtra(PRESSURE_VISIBLE, checkPressure.isChecked());
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
