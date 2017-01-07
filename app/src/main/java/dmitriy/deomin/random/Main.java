package dmitriy.deomin.random;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import dmitriy.deomin.random.tilteffect.TiltEffectAttacher;

public class Main extends Activity implements View.OnClickListener {

    Lab_text ot_n;
    Lab_text do_n;
    Lab_text size_numbers;



    String  rnd;
    String vibor;

    Button number_b,color_b,drob_b,bukva_b,sovet_b;

    HTextView hTextView;

    LinearLayout doto;
    Context context;


    //tema

    public static int FON;


    //dialog
    LayoutInflater inflater;
    View layout;
    HTextView text_random;
    Button dialog_copy;
    Button dialog_sare;
    Button dioalog_rand;
    AlertDialog alert;

    TextView time;

     int width_d;
     int heigh_d;

    //  static public Typeface face;

    boolean visi;//true при активном приложении
    boolean time_show_reklamma; //
    static public int TIME_SHOW_REKLAMA; // сколько показывать рекламу

    //сохранялка
    public static SharedPreferences mSettings; // сохранялка
    public static final String APP_PREFERENCES = "mysettings"; // файл сохранялки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        context = getApplicationContext();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width_d = display.getWidth();
        heigh_d = display.getHeight();


        //  face = Typeface.createFromAsset(getAssets(),"fonts/Tweed.ttf");

        //при первом запуске проги поставим дефолтные значения
        if(save_read("celka").length()==0){
            //разделитель списка
            save_value("razdelitel_spiska",",");
            //символ в конце списка
            save_value("conec_spiska",".");
            //нумерация списка (если текст есть то нумерация работает)
            save_value("num_spisok","est");
            //дополнительный текст к нумерации
            save_value("text_k_numeracii","");

            //показ кнопок меню
            save_value("checkBox_Chislo","da");
            save_value("checkBoxDrob","da");
            save_value("checkBoxColor","da");
            save_value("checkBoxBukva","da");
            save_value("checkBoxSlovo","da");


            //вскрыто
            save_value("celka","slomana");
        }


        ot_n = (Lab_text) findViewById(R.id.editText_name_ot);
        do_n  =(Lab_text) findViewById(R.id.editText_name_do);
        size_numbers = (Lab_text) findViewById(R.id.editText_size_rnd);

        ot_n.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        do_n.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        size_numbers.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

        do_n.getLabel().setGravity(Gravity.RIGHT);
        do_n.getEditText().setGravity(Gravity.RIGHT);
        size_numbers.getLabel().setGravity(Gravity.CENTER_HORIZONTAL);
        size_numbers.getEditText().setGravity(Gravity.CENTER_HORIZONTAL);

        ot_n.getEditText().setText((save_read("ot_n").equals("")?"0":(save_read("ot_n"))));
        do_n.getEditText().setText((save_read("do_n").equals("")?"100":(save_read("do_n"))));
        size_numbers.getEditText().setText((save_read("size_numbers").equals("")?"1":(save_read("size_numbers"))));

        ot_n.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                save_value("ot_n",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        do_n.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                save_value("do_n",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        size_numbers.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(Integer.valueOf(s.toString())==0){
                        Toast.makeText(getApplicationContext(),"Число должно быть больше 0",Toast.LENGTH_SHORT).show();
                        size_numbers.getEditText().setText("1");
                        save_value("size_numbers",s.toString());
                    }else {
                        save_value("size_numbers",s.toString());
                    }

                }else {
                    //если не ввели в течении 3х сек нечего установим 1
                   startLoop();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //слушаем сигналы
 //***************************************************************************
        //фильтр для нашего сигнала
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Key_signala_stav_1_v_pizdu");

        //приёмник  сигналов
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //если чел нечего так и не ввел то поставим 1
                if(size_numbers.getEditText().getText().toString().length()==0){
                    save_value("size_numbers","1");
                    size_numbers.getEditText().setText("1");
                }else{
                    save_value("size_numbers",size_numbers.getEditText().getText().toString());
                }

            }
        };

        //регистрируем приёмник
        registerReceiver(broadcastReceiver,intentFilter);
//************************************************************************************



        number_b = (Button)findViewById(R.id.button_number);
        color_b = (Button)findViewById(R.id.button_color);
        drob_b = (Button)findViewById(R.id.button_drob);
        bukva_b = (Button)findViewById(R.id.button_bukva);
        sovet_b = (Button)findViewById(R.id.button_sovet);

        number_b.setBackgroundColor(FON);
        color_b.setBackgroundColor(FON);
        drob_b.setBackgroundColor(FON);
        bukva_b.setBackgroundColor(FON);
        sovet_b.setBackgroundColor(FON);

        //скроем или покажем кнопки
        number_b.setVisibility((save_read("checkBox_Chislo").length()>0)?View.VISIBLE:View.GONE);
        color_b.setVisibility((save_read("checkBoxColor").length()>0)?View.VISIBLE:View.GONE);
        drob_b.setVisibility((save_read("checkBoxDrob").length()>0)?View.VISIBLE:View.GONE);
        bukva_b.setVisibility((save_read("checkBoxBukva").length()>0)?View.VISIBLE:View.GONE);
        sovet_b.setVisibility((save_read("checkBoxSlovo").length()>0)?View.VISIBLE:View.GONE);


        doto  = (LinearLayout) findViewById(R.id.ot_do);

        vibor  = "Число";

        hTextView = (HTextView) findViewById(R.id.text);
        hTextView.setAnimateType(HTextViewType.SCALE);
        hTextView.animateText("Cлучайное " + vibor); // animate


        doto.setVisibility(View.VISIBLE);
        number_b.setTextColor(Color.RED);
        color_b.setTextColor(Color.BLACK);
        drob_b.setTextColor(Color.BLACK);
        bukva_b.setTextColor(Color.BLACK);
        sovet_b.setTextColor(Color.BLACK);

        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setText(rnd);

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        //**********************************************************************

        TiltEffectAttacher.attach(findViewById(R.id.button_generete));

        visi = true;  // приложение активно
        TIME_SHOW_REKLAMA = 10; //секнды показа рекламы

        //реклама
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        time_show_reklamma = false;  //если бы черти isVisible mAdView сделали это херня бы не пригодилась

        //если нет интеренета скроем еЁ
        if (!isOnline(context)) {
            mAdView.setVisibility(View.GONE);
        } else {
            //через 10 секунд скроем её(пока так потом можно регулировать от количества постов)
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.v("TTT","ebasit");
                    if (visi) {
                        if (time_show_reklamma) {
                            mAdView.setVisibility(View.GONE); // скроем рекламу и поток больше не запустится
                        } else {
                            //иначе покажем
                            mAdView.setVisibility(View.VISIBLE);
                            time_show_reklamma = true; // это нужно чтоб знать что реклама показна
                            handler.postDelayed(this, 1000 * TIME_SHOW_REKLAMA); // через 10 секунд вырубим рекламу
                        }
                    }else {
                        handler.postDelayed(this, 1000 * 2); // если приложение свернуто пока в пустую погоняем поток
                    }
                }
            });
        }


        //посмотрим если есть сохранёный свет фона поставим его иначе рандомно
        if(save_read("fon_color").length()>0){
            FON = Integer.valueOf(save_read("fon_color"));
        }else{
            FON = random_color();
        }
        //поставим цвет фону
        ((LinearLayout)findViewById(R.id.fon_main)).setBackgroundColor(FON);
        number_b.setBackgroundColor(FON);
        color_b.setBackgroundColor(FON);
        drob_b.setBackgroundColor(FON);
        bukva_b.setBackgroundColor(FON);
        sovet_b.setBackgroundColor(FON);
        //


        time = (TextView)findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //поставим рандомный цвет фону
                FON = random_color();
                ((LinearLayout)findViewById(R.id.fon_main)).setBackgroundColor(FON);
                number_b.setBackgroundColor(FON);
                color_b.setBackgroundColor(FON);
                drob_b.setBackgroundColor(FON);
                bukva_b.setBackgroundColor(FON);
                sovet_b.setBackgroundColor(FON);
                //

            }
        });
        time.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //если есть сохранёный проверим его с текущим если совпадает то уберём его вообще
                if(save_read("fon_color").equals(String.valueOf(FON))){
                    save_value("fon_color","");
                    Toast.makeText(getApplicationContext(),"Фон будет рандомный",Toast.LENGTH_SHORT).show();
                }else{
                    save_value("fon_color",String.valueOf(FON));
                    Toast.makeText(getApplicationContext(),"Фон сохранён",Toast.LENGTH_SHORT).show();
                    ((LinearLayout)findViewById(R.id.fon_main)).setBackgroundColor(FON);
                    number_b.setBackgroundColor(FON);
                    color_b.setBackgroundColor(FON);
                    drob_b.setBackgroundColor(FON);
                    bukva_b.setBackgroundColor(FON);
                    sovet_b.setBackgroundColor(FON);
                    //
                }
                return true;
            }
        });

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.v("TTT", "время херачит");
                if (visi) {
                    //попробем делать скриншоты
                    captureScreen();


                    time.setText(DateFormat.format("dd-MM-yyyy     kk:mm:ss", new java.util.Date()).toString());
                }
                handler.postDelayed(this, 1000 * 1); // если приложение свернуто пока в пустую погоняем поток

            }
        });
    }

    public void startLoop() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(3);//3 секунды подождём
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //пошлём сигнал что надо дело делать
                Intent i  = new Intent("Key_signala_stav_1_v_pizdu");
                sendBroadcast(i);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == dialog_copy.getId()){
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.myscale);
            dialog_copy.startAnimation(anim);
            putText(rnd);
        }
        if(v.getId() == dialog_sare.getId()){
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.myscale);
            dialog_sare.startAnimation(anim);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            intent.putExtra(Intent.EXTRA_TEXT, rnd);
            try
            {
                startActivity(Intent.createChooser(intent, "Поделиться через"));
            }
            catch (android.content.ActivityNotFoundException ex)
            {
                Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId()==dioalog_rand.getId()){
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.myscale);
            dioalog_rand.startAnimation(anim);
            alert.cancel();
            generate((Button)findViewById(R.id.button_generete));

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            open_menu(findViewById(R.id.liner_logo));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public int random_color(){
        int r = random_nomer(0,255);
        int g = random_nomer(0,255);
        int b = random_nomer(0,255);
        return Color.rgb(r, g, b);
    }

    public int random_nomer(int min,int max){
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    //генерируем случайную дробь
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public double random_drob(double o,double d){
        double random = ThreadLocalRandom.current().nextDouble(o, d);
        return round(random,3);
    }
    //уменьшает количество символов после заятой
    private double round(double number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
        return (double) (int) ((tmp - (int) tmp) >= 0.5 ? tmp + 1 : tmp) / pow;
    }

    public void generate(View view) {
        switch (vibor){
            case ("Число"):
                if(ot_n.getEditText().getText().length()<1){
                    Toast.makeText(getApplicationContext(),"Введите начальное значение",Toast.LENGTH_LONG).show();
                    return;
                }
                if(do_n.getEditText().getText().length()<1){
                    Toast.makeText(getApplicationContext(),"Введите конечное значение",Toast.LENGTH_LONG).show();
                    return;
                }
                number_function();
                break;
            case ("Дробь"):
                if(ot_n.getEditText().getText().length()<1){
                    Toast.makeText(getApplicationContext(),"Введите начальное значение",Toast.LENGTH_LONG).show();
                    return;
                }
                if(do_n.getEditText().getText().length()<1){
                    Toast.makeText(getApplicationContext(),"Введите конечное значение",Toast.LENGTH_LONG).show();
                    return;
                }
                drob_function();
                break;
            case ("Цвет"):
                color_function();
                break;
            case ("Буква"):
                bukva_function();
                break;
            case ("Слово"):
                fraza_function();
                break;
        }
    }

    public void number_function(){

        rnd = "";
        String razdelitel = save_read("razdelitel_spiska");
        String conec_spisca = save_read("conec_spiska");
        String text_k_numeracii = save_read("text_k_numeracii");
        boolean numer = save_read("num_spisok").length()>0;



        double o = Double.valueOf(ot_n.getEditText().getText().toString());
        int min = (int)o;
        double d = Double.valueOf(do_n.getEditText().getText().toString());
        int max = (int)d;



        for (int i = 0;i<Integer.valueOf(size_numbers.getEditText().getText().toString());i++){
            if((i+1)==Integer.valueOf(size_numbers.getEditText().getText().toString())){
                rnd = rnd+String.valueOf(random_nomer(min,max))+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+conec_spisca:conec_spisca);
            }else {
                rnd = rnd+String.valueOf(random_nomer(min,max))+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+razdelitel:razdelitel);
            }

        }


        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setMovementMethod(new ScrollingMovementMethod());
        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setText(rnd);

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        ((LinearLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);
        //**********************************************************************

        layout.setMinimumWidth(width_d);
        layout.setMinimumHeight(width_d/2);
        AlertDialog.Builder bulder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        bulder.setView(layout);
        alert = bulder.create();
        alert.show();

    }

    public void color_function(){


        rnd = "";
        String razdelitel = save_read("razdelitel_spiska");
        String conec_spisca = save_read("conec_spiska");
        String text_k_numeracii = save_read("text_k_numeracii");
        boolean numer = save_read("num_spisok").length()>0;



        for (int i = 0;i<Integer.valueOf(size_numbers.getEditText().getText().toString());i++){

            String col = String.valueOf(random_color());

            if((i+1)==Integer.valueOf(size_numbers.getEditText().getText().toString())){
                rnd = rnd+"<font color=\'"+col+"\'>"+col+"</font>"+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+conec_spisca:conec_spisca);
            }else {
                rnd = rnd+"<font color=\'"+col+"\'>"+col+"</font>"+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+razdelitel:razdelitel);
            }
        }



        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);
        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setMovementMethod(new ScrollingMovementMethod());
        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setText(Html.fromHtml(rnd));

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);

        ((LinearLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);
        //**********************************************************************


        AlertDialog.Builder bulder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        bulder.setView(layout);
        alert =  bulder.create();
        alert.show();
    }

    public void drob_function(){

        rnd = "";
        String razdelitel = save_read("razdelitel_spiska");
        String conec_spisca = save_read("conec_spiska");
        String text_k_numeracii = save_read("text_k_numeracii");
        boolean numer = save_read("num_spisok").length()>0;


        double o = Double.valueOf(ot_n.getEditText().getText().toString());

        double d = Double.valueOf(do_n.getEditText().getText().toString());


        for (int i = 0;i<Integer.valueOf(size_numbers.getEditText().getText().toString());i++){
            if((i+1)==Integer.valueOf(size_numbers.getEditText().getText().toString())){
                rnd = rnd+String.valueOf(random_drob(o, d))+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+conec_spisca:conec_spisca);
            }else {
                rnd = rnd+String.valueOf(random_drob(o, d))+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+razdelitel:razdelitel);
            }

        }


        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setMovementMethod(new ScrollingMovementMethod());
        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setText(rnd);

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        ((LinearLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);
        //**********************************************************************

        layout.setMinimumWidth(width_d);
        layout.setMinimumHeight(width_d/2);
        AlertDialog.Builder bulder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        bulder.setView(layout);
        alert =  bulder.create();
        alert.show();
    }

    public void bukva_function(){


        rnd = "";
        String razdelitel = save_read("razdelitel_spiska");
        String conec_spisca = save_read("conec_spiska");
        String text_k_numeracii = save_read("text_k_numeracii");
        boolean numer = save_read("num_spisok").length()>0;


        //Русский алфавит
        String bukvi = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        int min = 0;
        int max = bukvi.length()-1;



        for (int i = 0;i<Integer.valueOf(size_numbers.getEditText().getText().toString());i++){
            if((i+1)==Integer.valueOf(size_numbers.getEditText().getText().toString())){
                rnd = rnd+String.valueOf(bukvi.charAt(random_nomer(min,max)))+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+conec_spisca:conec_spisca);
            }else {
                rnd = rnd+String.valueOf(bukvi.charAt(random_nomer(min,max)))+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+razdelitel:razdelitel);
            }

        }


        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setMovementMethod(new ScrollingMovementMethod());
        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setText(rnd);

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        ((LinearLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);
        //**********************************************************************

        layout.setMinimumWidth(width_d);
        layout.setMinimumHeight(width_d/2);
        AlertDialog.Builder bulder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        bulder.setView(layout);
        alert =  bulder.create();
        alert.show();

    }

    public void fraza_function(){

        rnd = "";
        String razdelitel = save_read("razdelitel_spiska");
        String conec_spisca = save_read("conec_spiska");
        String text_k_numeracii = save_read("text_k_numeracii");
        boolean numer = save_read("num_spisok").length()>0;

        String slova[] = getResources().getStringArray(R.array.slova);


        int min = 0;
        int max = slova.length-1;


        for (int i = 0;i<Integer.valueOf(size_numbers.getEditText().getText().toString());i++){
            if((i+1)==Integer.valueOf(size_numbers.getEditText().getText().toString())){
                rnd = rnd+slova[random_nomer(min,max)].toString()+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+conec_spisca:conec_spisca);
            }else {
                rnd = rnd+slova[random_nomer(min,max)].toString()+((numer)?"("+String.valueOf(i+1)+text_k_numeracii+")"+razdelitel:razdelitel);
            }

        }

        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setMovementMethod(new ScrollingMovementMethod());
        ((AutoScaleTextView)layout.findViewById(R.id.text_dialog)).setText(rnd);

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        ((LinearLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);

        //**********************************************************************
        layout.setMinimumWidth(width_d);
        layout.setMinimumHeight(width_d/2);
        AlertDialog.Builder bulder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        bulder.setView(layout);

        alert =  bulder.create();
        alert.show();

    }

    //запись
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    public void putText(String text){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES. HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context,"Скопировали:"+text+" в буфер",Toast.LENGTH_LONG).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = ClipData.newPlainText(text, text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context,"Скопировали:"+text+" в буфер",Toast.LENGTH_LONG).show();
        }
    }
    //чтение
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    public String getText(){
        String text = null;
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES. HONEYCOMB ) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboard.getText()!=null) {
                text = clipboard.getText().toString();
            }
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboard.getText()!=null) {
                text = clipboard.getText().toString();
            }
        }
        return text;
    }

    public void number_clik(View view) {
        vibor  = "Число";
        hTextView.setAnimateType(HTextViewType.SCALE);
        hTextView.animateText("Cлучайное "+vibor);
        doto.setVisibility(View.VISIBLE);

        number_b.setTextColor(Color.RED);
        color_b.setTextColor(Color.BLACK);
        drob_b.setTextColor(Color.BLACK);
        bukva_b.setTextColor(Color.BLACK);
        sovet_b.setTextColor(Color.BLACK);
    }

    public void drob_clik(View view) {
        vibor  = "Дробь";
        hTextView.setAnimateType(HTextViewType.ANVIL);
        hTextView.animateText("Cлучайная " + vibor);
        doto.setVisibility(View.VISIBLE);


        drob_b.setTextColor(Color.RED);
        number_b.setTextColor(Color.BLACK);
        color_b.setTextColor(Color.BLACK);
        bukva_b.setTextColor(Color.BLACK);
        sovet_b.setTextColor(Color.BLACK);
    }

    public void color_click(View view) {
        vibor  = "Цвет";
        hTextView.setAnimateType(HTextViewType.RAINBOW);
        hTextView.animateText("Cлучайный " + vibor);
        doto.setVisibility(View.GONE);

        color_b.setTextColor(Color.RED);
        number_b.setTextColor(Color.BLACK);
        drob_b.setTextColor(Color.BLACK);
        bukva_b.setTextColor(Color.BLACK);
        sovet_b.setTextColor(Color.BLACK);
    }

    public void bukva_clik(View view) {
        vibor  = "Буква";
        hTextView.setAnimateType(HTextViewType.TYPER);
        hTextView.animateText("Cлучайная " + vibor);
        doto.setVisibility(View.GONE);

        bukva_b.setTextColor(Color.RED);
        number_b.setTextColor(Color.BLACK);
        color_b.setTextColor(Color.BLACK);
        drob_b.setTextColor(Color.BLACK);
        sovet_b.setTextColor(Color.BLACK);
    }

    public void sovet_clik(View view) {
        vibor  = "Слово";
        hTextView.setAnimateType(HTextViewType.SCALE);
        hTextView.animateText("Cлучайное " + vibor);
        doto.setVisibility(View.GONE);

        sovet_b.setTextColor(Color.RED);
        number_b.setTextColor(Color.BLACK);
        color_b.setTextColor(Color.BLACK);
        drob_b.setTextColor(Color.BLACK);
        bukva_b.setTextColor(Color.BLACK);
    }
//*****************************************************************************************************

    private void captureScreen() {

        String url_img = "/Pictures/Screenshot/" + "Screenshots" + System.currentTimeMillis() + ".png";

        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        //создадим папки если нет
        File sddir = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/Screenshot/");
        if (!sddir.exists()) {
            sddir.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString(), url_img));
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            //Toast.makeText(context, "Скриншот сохранён", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void open_info(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        final View content = LayoutInflater.from(Main.this).inflate(R.layout.info_text, null);
        content.setMinimumHeight(heigh_d-5);
        content.setMinimumWidth(width_d-5);
        builder.setView(content);
        final AlertDialog al= builder.create();
        al.show();
        ((TextView) content.findViewById(R.id.text_info_la)).setText(getString(R.string.info_text));
        ((TextView) content.findViewById(R.id.text_info_la)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al.dismiss();
            }
        });
        ((LinearLayout)content.findViewById(R.id.fon_text_info)).setBackgroundColor(FON);

    }

    public void open_setting_vivod(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        final View content = LayoutInflater.from(Main.this).inflate(R.layout.setting_vivod, null);
        content.setMinimumHeight(heigh_d-5);
        content.setMinimumWidth(width_d-5);
        builder.setView(content);
        final AlertDialog al= builder.create();
        al.show();


        if(save_read("num_spisok").length()>0){
            ((CheckBox)content.findViewById(R.id.checkBox_numeracia_spiska)).setChecked(true);
            ((Lab_text)content.findViewById(R.id.text_k_numeracii)).setVisibility(View.VISIBLE);
        }else{
            ((CheckBox)content.findViewById(R.id.checkBox_numeracia_spiska)).setChecked(false);
            ((Lab_text)content.findViewById(R.id.text_k_numeracii)).setVisibility(View.GONE);
        }

        //слушаем чек
        ((CheckBox)content.findViewById(R.id.checkBox_numeracia_spiska)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //нумерация списка
                if(((CheckBox)content.findViewById(R.id.checkBox_numeracia_spiska)).isChecked()){
                    ((Lab_text)content.findViewById(R.id.text_k_numeracii)).setVisibility(View.VISIBLE);
                    save_value("num_spisok","est");

                }else {
                    ((Lab_text)content.findViewById(R.id.text_k_numeracii)).setVisibility(View.GONE);
                    save_value("num_spisok","");
                }
            }
        });


        //показ кнопок меню
        ((CheckBox)content.findViewById(R.id.checkBox_Chislo)).setChecked(save_read("checkBox_Chislo").length()>0);
        ((CheckBox)content.findViewById(R.id.checkBoxDrob)).setChecked(save_read("checkBoxDrob").length()>0);
        ((CheckBox)content.findViewById(R.id.checkBoxColor)).setChecked(save_read("checkBoxColor").length()>0);
        ((CheckBox)content.findViewById(R.id.checkBoxBukva)).setChecked(save_read("checkBoxBukva").length()>0);
        ((CheckBox)content.findViewById(R.id.checkBoxSlovo)).setChecked(save_read("checkBoxSlovo").length()>0);



       ((Lab_text)content.findViewById(R.id.text_k_numeracii)).getEditText().setText(save_read("text_k_numeracii"));
       ((Lab_text)content.findViewById(R.id.razdelitel_spiska)).getEditText().setText(save_read("razdelitel_spiska"));
       ((Lab_text)content.findViewById(R.id.simfol_conca_spiska)).getEditText().setText(save_read("conec_spiska"));



        ((Button) content.findViewById(R.id.button_gotovo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //разделитель списка
                save_value("razdelitel_spiska",((Lab_text)content.findViewById(R.id.razdelitel_spiska)).getEditText().getText().toString());

                //символ в конце списка
                save_value("conec_spiska",((Lab_text)content.findViewById(R.id.simfol_conca_spiska)).getEditText().getText().toString());


                //нумерация списка
                if(((CheckBox)content.findViewById(R.id.checkBox_numeracia_spiska)).isChecked()){
                    save_value("num_spisok","est");

                }else {
                    save_value("num_spisok","");
                }

                //дополнительный текст к нумерации
                save_value("text_k_numeracii",((Lab_text)content.findViewById(R.id.text_k_numeracii)).getEditText().getText().toString());


                //показ кнопок меню
                if(((CheckBox)content.findViewById(R.id.checkBox_Chislo)).isChecked()){
                    save_value("checkBox_Chislo","da");
                }else{
                    save_value("checkBox_Chislo","");
                }
                if(((CheckBox)content.findViewById(R.id.checkBoxDrob)).isChecked()){
                    save_value("checkBoxDrob","da");
                }else{
                    save_value("checkBoxDrob","");
                }
                if(((CheckBox)content.findViewById(R.id.checkBoxColor)).isChecked()){
                    save_value("checkBoxColor","da");
                }else{
                    save_value("checkBoxColor","");
                }
                if(((CheckBox)content.findViewById(R.id.checkBoxBukva)).isChecked()){
                    save_value("checkBoxBukva","da");
                }else{
                    save_value("checkBoxBukva","");
                }
                if(((CheckBox)content.findViewById(R.id.checkBoxSlovo)).isChecked()){
                    save_value("checkBoxSlovo","da");
                }else{
                    save_value("checkBoxSlovo","");
                }


                //скроем или покажем кнопки
                number_b.setVisibility((save_read("checkBox_Chislo").length()>0)?View.VISIBLE:View.GONE);
                color_b.setVisibility((save_read("checkBoxColor").length()>0)?View.VISIBLE:View.GONE);
                drob_b.setVisibility((save_read("checkBoxDrob").length()>0)?View.VISIBLE:View.GONE);
                bukva_b.setVisibility((save_read("checkBoxBukva").length()>0)?View.VISIBLE:View.GONE);
                sovet_b.setVisibility((save_read("checkBoxSlovo").length()>0)?View.VISIBLE:View.GONE);


                al.dismiss();
            }
        });
        ((LinearLayout)content.findViewById(R.id.fon_setting_vivod)).setBackgroundColor(FON);

    }



    public void open_menu(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        final View content = LayoutInflater.from(Main.this).inflate(R.layout.custom_dialog_menu, null);
        content.setMinimumHeight(heigh_d/3);
        content.setMinimumWidth(heigh_d/3);
        builder.setView(content);
        final AlertDialog al= builder.create();
        al.show();
        ((LinearLayout)content.findViewById(R.id.fon_dialog_menu)).setBackgroundColor(FON);

        ((Button)content.findViewById(R.id.MENU_ID_ABOUT)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al.dismiss();
                Intent i = new Intent(context,Abaut.class);
                startActivity(i);
            }
        });
        ((Button)content.findViewById(R.id.MENU_ID_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al.dismiss();
                open_setting_vivod(v);
            }
        });
        ((Button)content.findViewById(R.id.button_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al.dismiss();
                open_info(v);
            }
        });



    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }


    //ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss
    public static void save_value(String Key, String Value) { //сохранение строки
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(Key, Value);
        editor.apply();
    }
    public static String save_read(String key_save) {  // чтение настройки
        if (mSettings.contains(key_save)) {
            return (mSettings.getString(key_save, ""));
        }else{
            return "";
        }

    }
    public static void save_value_int(String Key, int Value) { //сохранение строки
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(Key, Value);
        editor.apply();
    }
    public static int save_read_int(String key_save) {  // чтение настройки
        if (mSettings.contains(key_save)) {
            return (mSettings.getInt(key_save, 0));
        }else{
            return 0;
        }

    }
//sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss


    @Override
    protected void onPause() {
        super.onPause();
        visi = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        visi = true;
    }


}
