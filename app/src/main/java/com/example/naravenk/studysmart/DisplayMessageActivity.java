package com.example.naravenk.studysmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    TextView articleText;
    TextView videoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        articleText = findViewById(R.id.textView6);
        videoText = findViewById(R.id.textView8);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if (Integer.parseInt(message) >= 2){
            articleText.setText("To make an accurate prediction, we need to know whether the two genes are inherited independently or not. That is, we need to know whether they ignore one another when they're sorted into gametes, or whether they stick together and get inherited as a unit. When Gregor Mendel asked this question, he found that different genes were inherited independently of one another, following what's called the law of independent assortment. In this article, we'll take a closer look at the law of independent assortment and how it is used to make predictions. We'll also see when and why the law of independent assortment does (or doesn't!) hold true.");
            videoText.setText("https://www.khanacademy.org/science/biology/classical-genetics/modal/v/introduction-to-heredity, https://www.khanacademy.org/science/biology/classical-genetics/modal/v/punnett-square-fun, https://www.khanacademy.org/science/biology/classical-genetics/modal/v/punnett-square-fun");
        }
        else{
            articleText.setText("To make an accurate prediction, we need to know whether the two genes are inherited independently or not. That is, we need to know whether they ignore one another when they're sorted into gametes, or whether they stick together and get inherited as a unit.");
            videoText.setText("https://www.khanacademy.org/science/biology/classical-genetics/modal/v/introduction-to-heredity, https://www.khanacademy.org/science/biology/classical-genetics/modal/v/punnett-square-fun");
        }

    }
}
