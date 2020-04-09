package nikifor.tatarkin.myweatherfrarmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Dialog dialogAboutDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_developer) {
            createDialogDeveloper();
        }
        return super.onOptionsItemSelected(item);
    }


    //Метод открывает диалоговое окно с описанием автора приложения.
    private void createDialogDeveloper(){
        dialogAboutDeveloper = new Dialog(this);
        dialogAboutDeveloper.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAboutDeveloper.setContentView(R.layout.about_developer_dialog); //путь к макету диалога
        dialogAboutDeveloper.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//прозрачный фон
        dialogAboutDeveloper.setCancelable(false); //диалог нельзя закрыть кнопкой назад

        TextView btnclose = (TextView) dialogAboutDeveloper.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAboutDeveloper.dismiss(); //закрываем диалоговое окно
            }
        });
        dialogAboutDeveloper.show();
    }

}
