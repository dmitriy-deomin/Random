package dmitriy.deomin.random;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import dmitriy.deomin.random.tilteffect.TiltEffectAttacher;

public class Main extends Activity implements View.OnClickListener {

    Lab_text ot_n;
    Lab_text do_n;

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

    // int width_d;
     //int heigh_d;

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

//        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        width_d = display.getWidth();
//        heigh_d = display.getHeight();


        //  face = Typeface.createFromAsset(getAssets(),"fonts/Tweed.ttf");

        //FON = getResources().getColor(R.color.colorFON)

        FON = random_color();



        ((LinearLayout)findViewById(R.id.fon_main)).setBackgroundColor(FON);


        ot_n = (Lab_text) findViewById(R.id.editText_name_ot);
        do_n  =(Lab_text) findViewById(R.id.editText_name_do);

        ot_n.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        do_n.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

        ot_n.getEditText().setText((save_read("ot_n").equals("")?"0":(save_read("ot_n"))));
        do_n.getEditText().setText((save_read("do_n").equals("")?"100":(save_read("do_n"))));

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

        doto  = (LinearLayout) findViewById(R.id.ot_do);

        vibor  = "Число";

        hTextView = (HTextView) findViewById(R.id.text);
        hTextView.setAnimateType(HTextViewType.LINE);
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

        text_random = (HTextView)layout.findViewById(R.id.text_dialog);
        text_random.setAnimateType(HTextViewType.LINE);
        text_random.animateText(String.valueOf(rnd));

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



        time = (TextView)findViewById(R.id.time);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.v("TTT", "время херачит");
                if (visi) {
                    time.setText(DateFormat.format("dd-MM-yyyy     kk:mm:ss", new java.util.Date()).toString());
                }
                handler.postDelayed(this, 1000 * 1); // если приложение свернуто пока в пустую погоняем поток

            }
        });
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
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
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

    public String getCurrDate()
    {
        String dt;
        Date cal = Calendar.getInstance().getTime();
        dt = cal.toLocaleString();
        return dt;
    }


    public void number_function(){

        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd  HH:mm");

        double o = Double.valueOf(ot_n.getEditText().getText().toString());
        int min = (int)o;
        double d = Double.valueOf(do_n.getEditText().getText().toString());
        int max = (int)d;

        final Integer integer = random_nomer(min,max);

        rnd = integer.toString();

        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        text_random = (HTextView)layout.findViewById(R.id.text_dialog);
        text_random.setAnimateType(HTextViewType.LINE);
        text_random.animateText(String.valueOf(rnd));

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        ((RelativeLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);
        //**********************************************************************

        AlertDialog.Builder bulder = new AlertDialog.Builder(this);
        bulder.setView(layout);
        alert = bulder.create();
        alert.show();

    }

    public void color_function(){
        final Integer integer = random_color();

        rnd = integer.toString();

        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        text_random = (HTextView)layout.findViewById(R.id.text_dialog);
        text_random.setAnimateType(HTextViewType.RAINBOW);
        text_random.setBackgroundColor(integer);
        text_random.animateText(rnd); // animate

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);

        ((RelativeLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(integer);
        //**********************************************************************


        AlertDialog.Builder bulder = new AlertDialog.Builder(this);
        bulder.setView(layout);
        alert =  bulder.create();
        alert.show();
    }

    Spannable color_vibor(String value,Integer color){
        Spannable text;
        text = new SpannableString(value);
        text.setSpan(new ForegroundColorSpan(color), 29, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //****************************************************
        return text;
    }

    public void drob_function(){
        final double dobl = random_drob((Double.valueOf(ot_n.getEditText().getText().toString())), (Double.valueOf(do_n.getEditText().getText().toString())));

        rnd = String.valueOf(dobl);

        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        text_random = (HTextView)layout.findViewById(R.id.text_dialog);
        text_random.setAnimateType(HTextViewType.ANVIL);
        text_random.animateText(rnd); // animate

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        ((RelativeLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);
        //**********************************************************************


        AlertDialog.Builder bulder = new AlertDialog.Builder(this);
        bulder.setView(layout);
        alert =  bulder.create();
        alert.show();
    }

    public void bukva_function(){

        final Integer integer = random_nomer(0, 32);

        String bukvi = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

        if(integer<=32) {
            rnd = String.valueOf(bukvi.charAt(integer));
        }else {
            rnd = String.valueOf(bukvi.charAt(integer-1));
        }

        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        text_random = (HTextView)layout.findViewById(R.id.text_dialog);
        text_random.setAnimateType(HTextViewType.TYPER);
        text_random.animateText(rnd); // animate

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        ((RelativeLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);
        //**********************************************************************


        AlertDialog.Builder bulder = new AlertDialog.Builder(this);
        bulder.setView(layout);
        alert =  bulder.create();
        alert.show();

    }

    public void fraza_function(){

        String slova[] = getResources().getStringArray(R.array.slova);

        final Integer integer = random_nomer(0, slova.length);



        if(integer<=slova.length) {
            rnd = slova[integer].toString();
        }else {
            rnd = slova[integer-1].toString();
        }

        //dialog****************************************************************
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custon_dialog, null);

        text_random = (HTextView)layout.findViewById(R.id.text_dialog);
        text_random.setAnimateType(HTextViewType.SCALE);
        text_random.animateText(rnd); // animate

        dialog_copy  = (Button)layout.findViewById(R.id.button_dialog_copy);
        dialog_copy.setOnClickListener(this);
        dialog_sare = (Button)layout.findViewById(R.id.button_dialog_share);
        dialog_sare.setOnClickListener(this);
        dioalog_rand = (Button)layout.findViewById(R.id.button_dialog_rand);
        dioalog_rand.setOnClickListener(this);
        ((RelativeLayout)layout.findViewById(R.id.dialog_fon)).setBackgroundColor(FON);
        //**********************************************************************

        AlertDialog.Builder bulder = new AlertDialog.Builder(this);
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
        hTextView.setAnimateType(HTextViewType.LINE);
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
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "Скриншот сохранён", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public void open_menu(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        final View content = LayoutInflater.from(Main.this).inflate(R.layout.custom_dialog_menu, null);
        builder.setView(content);
        final AlertDialog al= builder.create();
        al.show();

        ((Button)content.findViewById(R.id.MENU_ID_ABOUT)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Abaut.class);
                startActivity(i);
            }
        });
        ((Button)content.findViewById(R.id.MENU_ID_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(isOnline(Main.this)){
//                    Intent it = new Intent(Intent.ACTION_SEND);
//                    it.setData(Uri.parse("mailto:58627@bk.ru"));
//                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Main.this.startActivity(it);
//                }else{
//                    Toast.makeText(Main.this,"Нету интернету",Toast.LENGTH_LONG).show();
//                }

            }
        });

        ((RelativeLayout)content.findViewById(R.id.fon_dialog_menu)).setBackgroundColor(FON);

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
