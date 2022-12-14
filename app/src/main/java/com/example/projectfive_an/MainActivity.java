package com.example.projectfive_an;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ImageView newYorkPizzImageView;
    private ImageView storeOrdersImageView;
    private ImageView currentOrderImageView;
    private Order currentOrder;
    private StoreOrders storeOrders;
    private String ORDER_NUMBER;
    private ArrayList<String> allPizzas = new ArrayList<String>();
    private ArrayList<Double> subtotals = new ArrayList<Double>();
    private double subtotal = 0.00;
    private double orderTotal = 0.00;
    private double clearButton = 0.00;
    private int orderNumber = 0;
    private double salesTax = 0.00;
    private double[] sbList = changeLst(subtotals);
    private ArrayList<String> storeOrder = new ArrayList<>();
    private ArrayList<Integer> orderNumbers = new ArrayList<>();
    private ArrayList<Double> orderTotals = new ArrayList<>();

    /**
     * Default constructor.
     */
    public MainActivity() {
    }

    /**
     * General method for handle the clicking events.
     */
    private void clickEvents() {
        newYorkPizzImageView = (ImageView) findViewById(R.id.newYorkPizzaImageView);
        newYorkPizzImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nyTrigger = new Intent(getApplicationContext(), NewYorkPizzaActivity.class);
                nyTrigger.putExtra("PizzaList", allPizzas);
                startActivityForResult(nyTrigger, 1);
            }
        });
        storeOrdersImageView = (ImageView) findViewById(R.id.storeOrdersImageView);
        storeOrdersImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storeTrigger = new Intent(getApplication(), StoreOrdersActivity.class);
                storeTrigger.putIntegerArrayListExtra("orderNumberLst", orderNumbers);
                storeTrigger.putStringArrayListExtra("ordersLst", storeOrder);
                double[] ost;
                ost = changeLst(orderTotals);
                storeTrigger.putExtra("orderTotalLst", ost);
                startActivityForResult(storeTrigger, 3);
            }
        });
        currentOrderImageView = (ImageView) findViewById(R.id.currentOrderImageView);
        currentOrderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent curOrderTrigger = new Intent(getApplication(), CurrentOrderActivity.class);
                curOrderTrigger.putStringArrayListExtra("pizzaLst", allPizzas);
                curOrderTrigger.putExtra("orderNum", orderNumber);
                curOrderTrigger.putStringArrayListExtra("storeOrders", storeOrder);
                sbList = changeLst(subtotals);
                curOrderTrigger.putExtra("subtotals", sbList);
                curOrderTrigger.putExtra("orderTotal", orderTotal);
                curOrderTrigger.putExtra("salesTax", salesTax);
                startActivityForResult(curOrderTrigger, 2);
            }
        });
    }

    /**
     * This method is responsible for
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                allPizzas = data.getStringArrayListExtra("pizzalst");
                currentOrder.setStringList(allPizzas);
                subtotal = data.getDoubleExtra("subtotal", 0.00);
                subtotals.add(subtotal);
                orderTotal += subtotal;
                salesTax = orderTotal * (6.625 / 100);
            }
        }
        if (requestCode == 2) {
            if (resultCode == -111) {
                allPizzas = data.getStringArrayListExtra("pizzaLst");
                orderNumber = data.getIntExtra("orderNum", 0);
                sbList = data.getDoubleArrayExtra("subtotals");
                orderTotal = data.getDoubleExtra("orderTotal", 0.00);
                salesTax = data.getDoubleExtra("salesTax", 0.00);
            } else if (resultCode == RESULT_OK) {
                Intent intent = data;
                storeOrder = intent.getStringArrayListExtra("storeOrders");
                orderNumber = intent.getIntExtra("orderNum", 0);
                orderNumbers.add(orderNumber);
                orderTotals.add(orderTotal);
                orderNumber++;
                allPizzas = new ArrayList<>();
                sbList = new double[0];
                orderTotal = 0.00;
                salesTax = intent.getDoubleExtra("salesTax", 0.00);
            }
        }
        saveLength(requestCode, resultCode, data);
    }

    /**
     * Just for save length.
     *
     * @param requestCode The requestCode that we set before sending data to another activity.
     * @param resultCode  The resultCode that we set before sending back data to another activity.
     * @param data        The Data we set and receive.
     */
    private void saveLength(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3) {
            if (resultCode == 111) {
                storeOrder = data.getStringArrayListExtra("ordersLst");
                orderNumbers = data.getIntegerArrayListExtra("orderNumberLst");
                double[] ots;
                ots = data.getDoubleArrayExtra("orderTotalLst");
                if (ots == null) {
                    return;
                }
                orderTotals = changeToArrayList(ots);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.currentOrder = new Order();
        currentOrder.setOrderNumber(orderNumber);
        this.storeOrders = new StoreOrders();
        clickEvents();
    }

    private double[] changeLst(ArrayList<Double> doubles) {
        double[] res = new double[doubles.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = doubles.get(i);
        }
        return res;
    }

    private ArrayList<Double> changeToArrayList(double[] lst) {
        ArrayList<Double> newArrayList = new ArrayList<>();
        for (int i = 0; i < lst.length; i++) {
            newArrayList.add(lst[i]);
        }
        return newArrayList;
    }
}