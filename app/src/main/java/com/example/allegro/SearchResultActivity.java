package com.example.allegro;

import android.graphics.Color;
import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SearchResultActivity extends AppCompatActivity {

    String productName;
    String[] Values, PriceRanges;
    int[] CountedPriceRanges;
    int MIN_PRICE = 0,
        AVG_PRICE = 1,
        MAX_PRICE = 2,
        MEDIAN = 3,
        SELLER_COMPANY = 4,
        SELLER_PRIVATE = 5,
        DELIVERY_FREE = 6,
        DELIVERY_PAID = 7,
        COUNTER = 8,
        PROMOTED = 9,
        REGULAR = 10,
        BUY_NOW = 11,
        AUCTION = 12,
        PRODUCT_AVG = 13,
        PRODUCT_MAX = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productName = getIntent().getExtras().getString("ProductName");
        Values = getIntent().getExtras().getStringArray("Values");
        PriceRanges = getIntent().getExtras().getStringArray("PriceRanges");
        CountedPriceRanges = getIntent().getExtras().getIntArray("CountedPriceRanges");
        TextView textView;

        textView = findViewById(R.id.textView_ProductName);
        textView.setText(productName);

        textView = findViewById(R.id.textView_LowestPrice);
        textView.append(" " + Values[MIN_PRICE] + "zł");
        textView = findViewById(R.id.textView_AveragePrice);
        textView.append(" " + Values[AVG_PRICE] + "zł");
        textView = findViewById(R.id.textView_HighestPrice);
        textView.append(" " + Values[MAX_PRICE] + "zł");
        textView = findViewById(R.id.textView_MedianPrice);
        textView.append(" " + Values[MEDIAN] + "zł");

        textView = findViewById(R.id.textView_Offers_Promoted);
        textView.append(" " + Values[PROMOTED]);
        textView = findViewById(R.id.textView_Offers_Regular);
        textView.append(" " + Values[REGULAR]);
        textView = findViewById(R.id.textView_Offers_Buy_now);
        textView.append(" " + Values[BUY_NOW]);
        textView = findViewById(R.id.textView_Offers_Auction);
        textView.append(" " + Values[AUCTION]);

        textView = findViewById(R.id.textView_Products_Average);
        textView.append(" " + Values[PRODUCT_AVG]);
        textView = findViewById(R.id.textView_Products_Maximum);
        textView.append(" " + Values[PRODUCT_MAX]);

        textView = findViewById(R.id.textView_SellersShop);
        textView.append(" " + Values[SELLER_COMPANY]);
        textView = findViewById(R.id.textView_SellersPerson);
        textView.append(" " + Values[SELLER_PRIVATE]);

        textView = findViewById(R.id.textView_DeliveryFree);
        textView.append(" " + Values[DELIVERY_FREE]);
        textView = findViewById(R.id.textView_DeliveryPaid);
        textView.append(" " + Values[DELIVERY_PAID]);

        textView = findViewById(R.id.textView_OffersCount);
        textView.append(" " + Values[COUNTER] + " ofert");

        if(!Values[COUNTER].equals("0")){
            drawColumnChart();
        }

    }

    public void drawColumnChart(){

        AnyChartView columnChart = findViewById(R.id.column_chart);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for (int i = 0 ; i < CountedPriceRanges.length; i++){
            data.add(new ValueDataEntry(PriceRanges[i] + "zł - " + PriceRanges[i + 1] + "zł", CountedPriceRanges[i]));
        }

        Column column = cartesian.column(data);
        column.color("#FA5A00");
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: } ofert");

        cartesian.animation(true);
        //cartesian.title("Top 10 Cosmetic Products by Revenue");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.xAxis(0).labels().format("");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Zakres cen (zł)");
        cartesian.yAxis(0).title("Ilość ofert");

        columnChart.setChart(cartesian);
    }
}
