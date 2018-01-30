package com.view.demo7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LetterSideBar.LetterTouchListener{

    private TextView letter_tv;
    private LetterSideBar letter_side_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        letter_tv = (TextView) findViewById(R.id.letter_tv);
        letter_side_bar = (LetterSideBar) findViewById(R.id.letter_side_bar);

        letter_side_bar.setonLetterTouchListener(this);
    }

    @Override
    public void touch(CharSequence letter , boolean isTouch) {
        if (isTouch){  //isTouch为true表示移动手指，让中间的文字显示，并且设置文字
            letter_tv.setVisibility(View.VISIBLE);
            letter_tv.setText(letter);
        }else{    //isTouch为false表示手指抬起，让中间的文字隐藏
            letter_tv.setVisibility(View.GONE);
        }

    }
}
