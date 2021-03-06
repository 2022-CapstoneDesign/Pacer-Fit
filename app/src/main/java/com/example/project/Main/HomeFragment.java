package com.example.project.Main;

import static com.example.project.Weather.TransLocalPoint.TO_GRID;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.Dist.DistDetailRecordFragment;
import com.example.project.Dist.KmLineRecordRequest;
import com.example.project.Formatter.OneMonthXAxisValueFormatter;
import com.example.project.Map.RecordMapActivity;
import com.example.project.Pedo.DetailRecordFragment;
import com.example.project.Pedo.PedoBarRecordRequest;
import com.example.project.Pedo.PedoRecordRequest;
import com.example.project.Pedo.StepCounterActivity;
import com.example.project.R;
import com.example.project.Weather.GpsTrackerService;
import com.example.project.Weather.RequestHttpConnection;
import com.example.project.Weather.TransLocalPoint;
import com.example.project.Weather.Weather;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {
    private TextView weatherInfo;
    private TextView temperature;
    private ImageView weatherInfo_Image;
    private TransLocalPoint transLocalPoint;
    private GpsTrackerService gpsTracker;
    private String address = ""; // x,y??? ??????x,y??????
    String weather = ""; // ?????? ??????
    String tmperature = ""; // ?????? ??????

    private BarChart barChart; // ?????? ?????????
    private LineChart lineChart; // ????????? ?????????

    private Button moreBarChartBtn;
    private Button moreLineChartBtn;

    String userID;
    String userWeight;
    String today_userPedoRecord;
    String today_userPedoTimeRecord;
    String today_userPedoCalorieRecord;
    String date_concat;
    String[] PedoRecord31 = new String[31];
    String[] KmRecord31 = new String[31];
    String[] beforeMonth31 = new String[31];
    String pedo_max, km_max;
    int green,gray;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_home_fragment, container, false);
        Context ct = container.getContext();
        TextView location = v.findViewById(R.id.location);
        location.setSelected(true);
        weatherInfo = v.findViewById(R.id.weather);
        weatherInfo.setSelected(true);
        weatherInfo_Image = v.findViewById(R.id.weather_image);
        temperature = v.findViewById(R.id.temperature);
        //Button Km_button = v.findViewById(R.id.Km_button);
        LinearLayout Km_button = v.findViewById(R.id.km_layout_btn);
        //null????????? ????????? ??????

        green = ContextCompat.getColor(getContext(), R.color.green_project);
        gray = ContextCompat.getColor(getContext(), R.color.gray_project);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate now = null;
            now = LocalDate.now();

            int monthValue = now.getMonthValue();
            int dayOfMonth = now.getDayOfMonth();
            date_concat = monthValue + "m" + dayOfMonth + "d";
        }
        //?????? ??? ????????? ??????
        for (int i = 30, j = 0; i >= 0; i--, j++) {
            Calendar month = Calendar.getInstance();
            month.add(Calendar.DATE, -i);
            beforeMonth31[j] = new java.text.SimpleDateFormat("M'm'dd'd'").format(month.getTime());
            if (beforeMonth31[j].substring(2, 3).equals("0")) {
                beforeMonth31[j] = beforeMonth31[j].replaceFirst("0", "");
            }
//            System.out.println(beforeMonth31[j]);
        }

        Intent receiveIntent = getActivity().getIntent();
        userID = receiveIntent.getStringExtra("userID");
        userWeight = receiveIntent.getStringExtra("userWeight");
        pedo_max = receiveIntent.getStringExtra("pedo_max");
        km_max = receiveIntent.getStringExtra("km_max");

        Km_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View map) {
                Intent intent = new Intent(getActivity(), RecordMapActivity.class); //Fragment -> Activity??? ?????? (Map_add.java)
                //Intent intent = new Intent(getActivity(), Map_add.class);
                intent.putExtra("userWeight",userWeight);
                Log.d("weight", String.valueOf(userWeight));
                startActivity(intent);
            }
        });

        //Button pedo_button = v.findViewById(R.id.pedo_button);
        LinearLayout pedo_button = v.findViewById(R.id.pedo_layout_btn);
        pedo_button.setOnClickListener(map -> {
            // DB??????
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        System.out.println("========================" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) { // ????????? ?????????
                            String today_PedoStepsRecord = jsonObject.getString(date_concat + ".step");
                            String today_PedoTimeRecord = jsonObject.getString(date_concat + ".time");
                            String today_PedoCalorieRecord = jsonObject.getString(date_concat + ".cal");
                            Intent intent = new Intent(getActivity(), StepCounterActivity.class); //Fragment -> Activity??? ?????? (StepCounterActivity.java)
                            intent.putExtra("userID", userID);
                            intent.putExtra("userWeight", userWeight);
                            intent.putExtra("today_stepsRecord", today_PedoStepsRecord);
                            intent.putExtra("today_stepsTimeRecord", today_PedoTimeRecord);
                            intent.putExtra("today_stepsCalorieRecord", today_PedoCalorieRecord);
                            startActivity(intent);
                        } else { // ???????????? ????????? ??????
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            PedoRecordRequest pedoRecordRequest = new PedoRecordRequest(userID, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(pedoRecordRequest);
            //DB?????? ?????? ????????? ?????? 3??? ?????????


//            intent.putExtra("userWeight",userWeight);
//            intent.putExtra("userStepsRecord",today_userPedoRecord);
//            intent.putExtra("userStepsTimeRecord",today_userPedoTimeRecord);
//            intent.putExtra("userStepsCalorieRecord",today_userPedoCalorieRecord);
//            System.out.println("userKg??????????????at HomeFrag"+userWeight);
//            System.out.println("userStepsRecord??????????????at HomeFrag"+today_userPedoRecord);
//            System.out.println("userStepsRecord??????????????at HomeFrag"+today_userPedoTimeRecord);
//            System.out.println("userStepsRecord??????????????at HomeFrag"+today_userPedoCalorieRecord);
//            //BottomNavigation?????????????????? StepCounterActivity??? userWeight?????? ??????
//            startActivity(intent);
//                Intent intent = new Intent(getActivity(), PopupPedo.class); //Fragment -> Activity??? ?????? (???????????????)
//                startActivity(intent);
        });
        Weather weatherMethod = new Weather();
        gpsTracker = new GpsTrackerService(ct);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        address = weatherMethod.getCurrentAddress(ct, latitude, longitude);
        String[] local = address.split(" ");  //????????? ????????????, ???????????????, xx??? ... ??? ??????
        // String localName = local[2]; //xx??? ??????

        location.setText(local[1] + " " + local[2]);

        transLocalPoint = new TransLocalPoint();
        TransLocalPoint.LatXLngY tmp = transLocalPoint.convertGRID_GPS(TO_GRID, latitude, longitude);
        Log.e(">>", "x = " + tmp.x + ", y = " + tmp.y);

        String url = weatherMethod.weather(tmp.x, tmp.y);
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();

        Calendar now = Calendar.getInstance();
        int isAMorPM = now.get(Calendar.AM_PM);
        switch (isAMorPM) {
            case Calendar.AM:
                ((TextView) v.findViewById(R.id.ampm)).setText("??????");
                break;
            case Calendar.PM:
                ((TextView) v.findViewById(R.id.ampm)).setText("??????");
                break;
        }


        // <----------- ?????? ????????? ----------->
        barChart = v.findViewById(R.id.barchart);
        ArrayList<Float> barChartValues2 = new ArrayList<>();//?????????
        for (int i = 0; i < 31; i++) {
            barChartValues2.add(0f); //?????????
        }
        barchartConfigureAppearance();//?????????
        BarData barChartData2 = createBarchartData(barChartValues2);//?????????
        barChart.setData(barChartData2); //?????????
        barChart.invalidate(); //?????????

        // <----------- ????????? ????????? ----------->
        lineChart = v.findViewById(R.id.linechart);
        //DB ?????? (Response)

        ArrayList<Float> lineChartValues2 = new ArrayList<>();
        // ?????? 1?????? ????????? ??? ???????????? -> DB ????????? ????????? ??????
        for (int i = 0; i < 31; i++) {
            //Log.d("RAND", String.valueOf(rand));
            lineChartValues2.add(0f); // 0 ~ 15,000 ????????? ?????????
        }
        linechartConfigureAppearance();
        LineData lineChartData2 = createLinechartData(lineChartValues2);
        lineChart.setData(lineChartData2); // LineData ??????
        lineChart.invalidate(); // LineChart ????????? ????????? ??????

        //barChart.setOnChartValueSelectedListener(this);
        Response.Listener<String> responseListener = response -> {
            try {
                System.out.println("========================" + response);
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    System.out.println("31?????? ????????? ???????????? ??????");
                    km_max = jsonObject.getString("km_max_recent");
                    pedo_max = jsonObject.getString("pedo_max_recent");
                    for (int i = 0; i < 31; i++) {
                        PedoRecord31[i] = jsonObject.getString(beforeMonth31[i]);
                    }
                    for (int i = 0; i < 31; i++) {
                        KmRecord31[i] = jsonObject.getString(beforeMonth31[i]+".km");
                    }
                    //??????
                    ArrayList<Float> barChartValues = new ArrayList<>();
                    // ?????? 1?????? ????????? ??? ???????????? -> DB ????????? ????????? ??????
                    for (int i = 0; i < 31; i++) {
                        float rand2 = Float.parseFloat(PedoRecord31[i]);
                        barChartValues.add(rand2); // DB???
                    }
                    BarData barChartData = createBarchartData(barChartValues);
                    barChart.setData(barChartData); // BarData ??????
                    barChart.invalidate(); // BarChart ????????? ????????? ??????

                    //?????????
                    ArrayList<Float> lineChartValues = new ArrayList<>();
                    // ?????? 1?????? ????????? ??? ???????????? -> DB ????????? ????????? ??????
                    for (int i = 0; i < 31; i++) {
                        float rand2 = Float.parseFloat(KmRecord31[i]);
                        //Log.d("RAND", String.valueOf(rand));
                        lineChartValues.add(rand2); // 0 ~ 15,000 ????????? ?????????
                    }
                    LineData lineChartData = createLinechartData(lineChartValues);
                    lineChart.setData(lineChartData); // LineData ??????
                    lineChart.invalidate(); // LineChart ????????? ????????? ??????
                } else { //????????? ??????
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        MainPedoRecentRecordRequest mainPedoRecentRecordRequest = new MainPedoRecentRecordRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(mainPedoRecentRecordRequest);
        moreBarChartBtn = v.findViewById(R.id.moreBarChartBtn);
        moreBarChartBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = response -> {
                    try {
                        System.out.println("========================Click->" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Bundle bundle = new Bundle(); // ????????? ?????? ??? ??????
                            bundle.putString("userID",userID);//????????? ?????? ??? ??????
                            for(int i=0; i<7; i++) {
                                bundle.putString(i+".day_step",jsonObject.getString(i+".day_step"));
                                bundle.putString(i+".day_time",jsonObject.getString(i+".day_time"));
                            }
                            bundle.putString("pedo_max_day",jsonObject.getString("pedo_max_day"));
                            for(int i=0; i<31; i++) {
                                bundle.putString(i+".month_step",jsonObject.getString(i+".month_step"));
                                bundle.putString(i+".month_time",jsonObject.getString(i+".month_time"));
                            }
                            bundle.putString("pedo_max_month",jsonObject.getString("pedo_max_month"));
                            for(int i=0; i<24; i++){
                                bundle.putString(i+".week_step",jsonObject.getString(i+".week_step"));
                                bundle.putString(i+".week_time",jsonObject.getString(i+".week_time"));
                            }
                            bundle.putString("pedo_max_180",jsonObject.getString("pedo_max_180"));
                            for(int i=0; i<12; i++){
                                bundle.putString(i+".year_step",jsonObject.getString(i+".year_step"));
                                bundle.putString(i+".year_time",jsonObject.getString(i+".year_time"));
                            }
                            bundle.putString("pedo_max_year",jsonObject.getString("pedo_max_year"));
                            // ????????? ????????? ??????. ?????? ?????????????????? ????????? ??? ????????? ??????
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            DetailRecordFragment detailRecordFragment = new DetailRecordFragment();//???????????????2 ??????
                            detailRecordFragment.setArguments(bundle);//????????? ???????????????2??? ?????? ??????
                            fm.beginTransaction()
                                    .replace(R.id.frame_container, detailRecordFragment)
                                    .addToBackStack(null).commit();
                        } else { //????????? ??????
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                PedoBarRecordRequest pedoBarRecordRequest = new PedoBarRecordRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(pedoBarRecordRequest);
                moreBarChartBtn.setEnabled(false);
            }
        });
        moreLineChartBtn = v.findViewById(R.id.moreLineChartBtn);
        moreLineChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = response -> {
                    try {
                        System.out.println("========================Click->" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Bundle bundle = new Bundle(); // ????????? ?????? ??? ??????
                            bundle.putString("userID",userID);//????????? ?????? ??? ??????
                            for(int i=0; i<7; i++) {
                                bundle.putString(i+".day_km",jsonObject.getString(i+".day_km"));
                                bundle.putString(i+".day_time",jsonObject.getString(i+".day_time"));
                            }
                            bundle.putString("km_max_day",jsonObject.getString("km_max_day"));
                            for(int i=0; i<31; i++) {
                                bundle.putString(i+".month_km",jsonObject.getString(i+".month_km"));
                                bundle.putString(i+".month_time",jsonObject.getString(i+".month_time"));
                            }
                            bundle.putString("km_max_month",jsonObject.getString("km_max_month"));
                            for(int i=0; i<24; i++){
                                bundle.putString(i+".week_km",jsonObject.getString(i+".week_km"));
                                bundle.putString(i+".week_time",jsonObject.getString(i+".week_time"));
                            }
                            bundle.putString("km_max_180",jsonObject.getString("km_max_180"));
                            for(int i=0; i<12; i++){
                                bundle.putString(i+".year_km",jsonObject.getString(i+".year_km"));
                                bundle.putString(i+".year_time",jsonObject.getString(i+".year_time"));
                            }
                            bundle.putString("km_max_year",jsonObject.getString("km_max_year"));
                            // ????????? ????????? ??????. ?????? ?????????????????? ????????? ??? ????????? ??????
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            DistDetailRecordFragment distDetailRecordFragment = new DistDetailRecordFragment();//???????????????2 ??????
                            distDetailRecordFragment.setArguments(bundle);//????????? ???????????????2??? ?????? ??????
                            fm.beginTransaction()
                                    .replace(R.id.frame_container, distDetailRecordFragment)
                                    .addToBackStack(null).commit();
                        } else { //????????? ??????
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                KmLineRecordRequest kmLineRecordRequest = new KmLineRecordRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(kmLineRecordRequest);
                moreLineChartBtn.setEnabled(false);
            }
        });

        return v;

    } // onCreateView

    // ??????????????? ?????? ??????
    private void barchartConfigureAppearance() {
        barChart.setTouchEnabled(false); // ?????? ??????
        barChart.setPinchZoom(false); // ??? ??????????????? ??????,????????? ??????
        barChart.setDrawBarShadow(false); // ???????????? ?????????
        barChart.setDrawGridBackground(false); // ???????????? ??????
        barChart.getLegend().setEnabled(false); // legend??? ????????? ??????
        barChart.getDescription().setEnabled(false); // ?????? ????????? DescriptionLabel ??????
        barChart.animateY(1500); // ??????????????? ???????????? ??????????????? ??????
        barChart.animateX(1500); // ??????-????????? ????????? ??????????????? ??????
        //barChart.setDescription();
        //barChart.setExtraOffsets(10f, 0f, 40f, 0f);
        // x??? ??????(??????????????? ?????? ?????????)
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(-0.5f); // ?????????????????? x??? ?????? ?????? ?????? ??????
        xAxis.setAxisMaximum(30.5f); // x : 0, 1, ... , 30 -> 31???
        xAxis.setDrawAxisLine(false); // ??? ????????? ??????
        xAxis.setLabelCount(31); // ?????? ?????? setGranularity??? ?????????
        xAxis.setGranularity(1f); // ?????? ??????(???????????? ???) -> OneMonthXAxisValueFormatter.java?????? ??? ???????????? ????????? ?????????
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // ??????
        //xAxis.setGridLineWidth(25f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X??? ????????? ?????? ??????
        xAxis.setValueFormatter(new OneMonthXAxisValueFormatter());
        // X??? ?????? ??????
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nanumsquareroundeb.ttf");
        xAxis.setTypeface(tf);

        // y??? ??????(??????????????? ?????? ??????)
        YAxis axisLeft = barChart.getAxisLeft();
        float pedo_max_float = 0f;
        pedo_max_float = Float.parseFloat(pedo_max);

        axisLeft.setAxisMaximum(pedo_max_float); // y??? ????????? ??????
        axisLeft.setAxisMinimum(0f); // y??? ????????? ??????
        axisLeft.setDrawLabels(false); // ??? ?????? ??????
        axisLeft.setDrawGridLines(false); // ??????
        axisLeft.setDrawAxisLine(false); // ??? ????????? ??????
        //axisLeft.setAxisLineColor(Color.parseColor("#FFFFFFFF")); // ??? ?????? ??????
        //axisLeft.setLabelCo

        // (??????????????? ?????? ?????????)
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setDrawLabels(false); // ??? ?????? ??????
        axisRight.setDrawGridLines(false); // ??????
        axisRight.setDrawAxisLine(false); // ??? ????????? ??????
        //axisRight.setEnabled(false); // ????????? y?????? ??? ????????? ??????
    }

    // ?????????????????? ?????? ??????
    private void linechartConfigureAppearance() {
        lineChart.setTouchEnabled(false);
        lineChart.setTouchEnabled(false); // ?????? ??????
        lineChart.setPinchZoom(false); // ??? ??????????????? ??????,????????? ??????
        lineChart.setDrawGridBackground(false); // ???????????? ??????
        lineChart.getLegend().setEnabled(false); // legend??? ????????? ??????
        lineChart.getDescription().setEnabled(false); // ?????? ????????? DescriptionLabel ??????
        //lineChart.animateY(1500); // ??????????????? ???????????? ??????????????? ??????
        lineChart.animateX(1500); // ??????-????????? ????????? ??????????????? ??????

        // x??? ??????(?????????????????? ?????? ?????????)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMaximum(30.5f); // x : 0, 1, ... , 30
        xAxis.setDrawAxisLine(false); // ??? ????????? ??????
        xAxis.setLabelCount(31); // ?????? ?????? setGranularity??? ?????????
        xAxis.setGranularity(1f); // ?????? ??????(???????????? ???) -> OneMonthXAxisValueFormatter.java?????? ??? ???????????? ????????? ?????????
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // ??????
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X??? ????????? ?????? ??????
        xAxis.setValueFormatter(new OneMonthXAxisValueFormatter());
        // X??? ?????? ??????
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nanumsquareroundeb.ttf");
        xAxis.setTypeface(tf);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        float km_max_float=0f;
        km_max_float = Float.parseFloat(km_max);

        km_max_float += km_max_float/10;
        yAxisLeft.setAxisMaximum(km_max_float); // y??? ????????? ??????
        yAxisLeft.setAxisMinimum(0f); // y??? ????????? ??????
        yAxisLeft.setDrawLabels(false); // ??? ?????? ??????
        yAxisLeft.setDrawGridLines(false); // ??????
        yAxisLeft.setDrawAxisLine(false); // ??? ????????? ??????

        // (?????????????????? ?????? ?????????)
        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setDrawLabels(false); // ??? ?????? ??????
        axisRight.setDrawGridLines(false); // ??????
        axisRight.setDrawAxisLine(false); // ??? ????????? ??????
    }

    // ??? ???????????? ????????? BarData??? ?????? BarData ????????? ???????????? BarChart??? ????????? ???????????? ??????
    private BarData createBarchartData(ArrayList<Float> chartValues) {
        // 1. [BarEntry] BarChart??? ????????? ????????? ??? ??????
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            float x = i;
            float y = chartValues.get(i);
            values.add(new BarEntry(x, y));
        }

        // 2. [BarDataSet] ?????? ???????????? ?????? ???????????? ??????, BarChart??? ?????? ?????????
        BarDataSet set = new BarDataSet(values, "???????????????");
        set.setDrawIcons(false);
        set.setDrawValues(false); // ?????? ?????? ??? ?????? ??????
        set.setColor(Color.parseColor("#EB3314")); // ?????? ??????(??????)

        // 3. [BarData] ????????? ????????? ??????
        BarData data = new BarData(set);
        data.setBarWidth(0.6f);
        data.setValueTextSize(15);

        return data;
    }

    // ??? ???????????? ????????? LineData??? ?????? LineData ????????? ???????????? LineChart??? ????????? ???????????? ??????
    private LineData createLinechartData(ArrayList<Float> chartValues) {
        // 1. [Entry] LineChart??? ????????? ????????? ??? ??????
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            float x = i;
            float y = chartValues.get(i);
            values.add(new Entry(x, y));
        }

        // 2. [LineDataSet] ?????? ???????????? ????????? ???????????? ??????, LineChart??? ?????? ?????????
        LineDataSet set = new LineDataSet(values, "??????????????????");
        set.setDrawIcons(false);
        set.setDrawValues(false);
        //???????????? ????????? onCreate??? ??????
//        green = ContextCompat.getColor(getContext(), R.color.green_project);
//        gray = ContextCompat.getColor(getContext(), R.color.gray_project);
        set.setFillColor(green);
        set.setColor(green);
        set.setDrawCircleHole(true);
        set.setCircleColor(green); // ?????? ??? ???
        set.setCircleHoleColor(gray); // ?????? ??? ???
        set.setLineWidth(1.5f); // ??? ??????
        set.setCircleSize(4f);
        set.setDrawCircles(true); //??? ????????? ?????????
        set.setDrawFilled(false); //????????? ????????? ??????X

        // 3. [LineData] ????????? ????????? ??????
        LineData data = new LineData(set);
        data.setValueTextSize(15);

        return data;
    }

    public String weatherJsonParser(String jsonString) throws JSONException {
        long mNow = System.currentTimeMillis();
        Date mReDate = new Date(mNow);
        SimpleDateFormat currentTime = new SimpleDateFormat("HH00");
        String mCurrentTime = String.format("%04d", (Integer.parseInt(currentTime.format(mReDate))));
        Log.i("currentTime", mCurrentTime);
        String rain = "0";

        //response ?????? ????????? ???????????? ??????
        if (jsonString != null) {
            JSONObject jsonObject1 = new JSONObject(jsonString);
            String response = jsonObject1.getString("response");

            // response ??? ?????? body ??????
            if (response != null) {
                JSONObject jsonObject2 = new JSONObject(response);
                String body = jsonObject2.getString("body");

                // body ??? ?????? items ??????
                if (body != null) {
                    JSONObject jsonObject3 = new JSONObject(body);
                    String items = jsonObject3.getString("items");


                    // itmes??? ?????? itemlist ??? ??????
                    if (items != null) {
                        JSONObject jsonObject4 = new JSONObject(items);
                        JSONArray jsonArray = jsonObject4.getJSONArray("item");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject4 = jsonArray.getJSONObject(i);
                            String fcstTime = jsonObject4.getString("fcstTime");
                            String fcstValue = jsonObject4.getString("fcstValue");
                            String category = jsonObject4.getString("category");

                            if (fcstTime.equals(mCurrentTime)) {
                                Log.d("ITEM", jsonObject4.toString());
                                if (category.equals("PTY")) {
                                    if (fcstValue.equals("1") || fcstValue.equals("2") || fcstValue.equals("5") || fcstValue.equals("6")) {
                                        weather = "???\n";
                                        rain = "1";
                                    } else if (fcstValue.equals("3") || fcstValue.equals("7")) {
                                        weather = "???\n";
                                        rain = "1";
                                    } else if (fcstValue.equals("0")) {
                                        rain = "0";
                                    }
                                }
                                if (category.equals("SKY") && rain.equals("0")) {
                                    if (fcstValue.equals("1")) {
                                        weather = "??????\n";
                                    } else if (fcstValue.equals("3")) {
                                        weather = "?????? ??????\n";
                                    } else if (fcstValue.equals("4")) {
                                        weather = "??????\n";
                                    }
                                }

                                if (category.equals("T1H")) {
                                    tmperature = fcstValue + "???";
                                }
                            }
                        }
                    }
                }
            }
        }
        return weather + tmperature;
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        String result;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
            result = requestHttpConnection.requset(url, values); //?????? URL????????? ??????
            try {
                weatherJsonParser(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            weatherInfo.setText(weather);
            temperature.setText(tmperature);
            //doInBackground()????????? ????????? ?????? onPostExecute()??? ??????????????? ??????????????? s??? ??????
            if (weather.equals("??????\n")) {
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_sunny);
            } else if (weather.equals("???\n")) {
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_rainy);
            } else if (weather.equals("?????? ??????\n")) {
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_heavy_cloudy);
            } else if (weather.equals("??????\n")) {
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_cloudy);
            } else if (weather.equals("???\n")) {
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_snow);
            }
        }
    }
}