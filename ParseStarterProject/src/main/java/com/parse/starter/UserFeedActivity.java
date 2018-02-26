package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        final TextView textView = (TextView) findViewById(R.id.textView);


        Intent intent = getIntent();
        String activeUsername = intent.getStringExtra("username");

        if (activeUsername.equals(ParseUser.getCurrentUser().getUsername())){

            setTitle("My Feed");

        }else {

            setTitle(activeUsername + "'s Feed");
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");

        query.whereEqualTo("username", activeUsername);

        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null){
                    if (objects.size()>0){

                        textView.setVisibility(View.INVISIBLE);

                        for (ParseObject object : objects){

                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (e == null && data != null){

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                                                data.length);
                                        ImageView imageView = new ImageView(getApplicationContext());

                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT));

                                        imageView.setImageBitmap(bitmap);
                                        imageView.setAdjustViewBounds(true);
                                        //imageView.setScaleType(ImageView.ScaleType.MATRIX);
                                        imageView.setPadding(20,0,20, 20);
                                        linearLayout.addView(imageView);

                                    }

                                }
                            });

                        }

                    }
                }
            }
        });







    }
}
