package cz.kerslager.android.kompas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageViewArrow;

    private float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewArrow = (ImageView) findViewById(R.id.imageViewArrow);
    }

    public void onButtonClick(View v) {
        // nemá smysl pracovat s hodnotou přes 360 stupňů (i když to funguje)
        if (currentDegree >= 360) currentDegree -= 360;

        // degree stanovuje nový směr, zde jen pootočíme o +10° od aktuální pozice
        int degree = Math.round(currentDegree) + 10;

        // vlastní rotace (s animací)
        RotateAnimation ra = new RotateAnimation(
                currentDegree, // z pozice
                degree,        // do pozice
                Animation.RELATIVE_TO_SELF, // pivotXType
                0.5f,                       // pivotXValue -> rotace kolem středu obrázku (50%)
                Animation.RELATIVE_TO_SELF, // pivotYType
                0.5f                        // pivotYValue -> rotace kolem středu obrázku (50%)
        );
        ra.setDuration(1000);  // délka animace otáčení
        ra.setFillAfter(true); // otočit a zůstat v nové pozici

        imageViewArrow.startAnimation(ra);
        currentDegree = degree;
    }
}
