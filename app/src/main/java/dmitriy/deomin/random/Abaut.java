package dmitriy.deomin.random;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.util.Timer;
import java.util.TimerTask;

public class Abaut extends Activity {

    HTextView hTextView;
    TextView textView;
    int live;
    Timer mTimer;
    MyTimerTask mMyTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abaut);

        //во весь экран
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ((RelativeLayout)findViewById(R.id.fon_abaut)).setBackgroundColor(Main.FON);

        hTextView = (HTextView) findViewById(R.id.text_abaut);
        hTextView.setBackgroundColor(Main.FON);
        hTextView.setAnimateType(HTextViewType.TYPER);
        hTextView.animateText(getVersion());

        textView = (TextView)findViewById(R.id.textView_abaut);
        textView.setText(getString(R.string.abaut_text).replace("+++++++","Random "+getVersion()));
        textView.setVisibility(View.GONE);

        live= 0; // 2
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 1000);

    }

    public  void pizdez(){
        if (mTimer != null) {
            mTimer.cancel();
        }

        hTextView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        ((ImageButton)findViewById(R.id.imageButton_vk)).setVisibility(View.VISIBLE);
    }

    public void finish(View view) {
        this.finish();
    }


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    live++;
                    if(live>=2){
                      pizdez();
                    }
                }
            });
        }
    }

    public  void opengruppa(View view) {
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/random_deomin"));
        startActivity(browseIntent);
    }
    private String getVersion(){
        try {
            PackageManager packageManager=getPackageManager();
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        }
        catch (  PackageManager.NameNotFoundException e) {
            return "?";
        }
    }

}
