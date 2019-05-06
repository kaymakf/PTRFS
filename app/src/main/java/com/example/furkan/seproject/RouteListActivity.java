package com.example.furkan.seproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RouteListActivity extends AppCompatActivity {

    private List<Button> btnList = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        LinearLayout scrollView = (LinearLayout) findViewById(R.id.route_buttons);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        try {
            RouteFinder.getInstance().findRoute();
            JSONArray routes = RouteFinder.getInstance().getJson().getJSONArray("Routes");

            for (int i=0; i<routes.length(); i++){
                JSONObject route = routes.getJSONObject(i);
                int buttonStyle = R.style.Widget_AppCompat_Button_Colored;
                Button btn = new Button(new ContextThemeWrapper(this, buttonStyle), null, buttonStyle);
                btn.setText(route.getString("DurationMinutes") + " minutes");
                btn.setId(i);
                btn.setBackgroundColor(Color.rgb(0, 136, 204));


                LinearLayout horizontalLayout = new LinearLayout(RouteListActivity.this);
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayout.setFadingEdgeLength(1);
                horizontalLayout.setPadding(10,10,5,30);

                //horizontalLayout.setBackgroundColor(Color.CYAN);

                JSONArray segments = route.getJSONArray("RouteSegments");

                for (int j=0; j<segments.length(); j++){
                    JSONObject segment = segments.getJSONObject(j);
                    ImageView imageView = new ImageView(this);
                    ImageLoader.getInstance().displayImage(segment.getString("IconUrl"), imageView);
                    horizontalLayout.addView(imageView);
                    TextView txt = new TextView(this);
                    txt.setText(segment.getString("DurationMinutes") + " min  ");
                    horizontalLayout.addView(txt);
                }

                scrollView.addView(btn);
                scrollView.addView(horizontalLayout);

                btnList.add(btn);
            }


            for (Button b: btnList) {
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RouteFinder.getInstance().pickedRoute = b.getId();

                        Intent intent = new Intent(RouteListActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
