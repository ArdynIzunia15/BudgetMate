package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ArrayList<History> arrHistory = new ArrayList<>();
    RecyclerView recyclerView;
    HistoryRecyclerViewAdapter adapter;
    MaterialButton btnAccount, btnAddTransaction;
    TextView txtHistoriTransaksiKosong, txtPengeluaran, txtPemasukan, txtBalance;
    HorizontalBarChart barChart;
    FragmentContainerView fragmentAddTransaction;
    ImageView imageHistoriTransaksiKosong, btnEdit, bubblePopup;
    boolean isOpenLogoutFragment = false;

    private String formatBalance(int balance){
        return String.format("IDR %,d", balance);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        // Clear resources
        databaseHandler.close();
        databaseHandler = null;

        arrHistory.clear();
        arrHistory = null;

        recyclerView.setAdapter(null);
        recyclerView = null;

        adapter = null;

        btnAccount = null;
        btnAddTransaction = null;

        txtHistoriTransaksiKosong = null;
        txtPengeluaran = null;
        txtPemasukan = null;
        txtBalance = null;

        barChart = null;

        fragmentAddTransaction = null;

        imageHistoriTransaksiKosong = null;
        btnEdit = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // Change Notification Bar Color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary));

        // Read Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("activeUserAccount", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", 0);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");

        // If active account present
        if(userId != 0 && !username.equals("") && !password.equals("")){
            // Init Components
            databaseHandler = new DatabaseHandler(this);
            btnAccount = findViewById(R.id.btnAccount);
            txtHistoriTransaksiKosong = findViewById(R.id.txtHistoriTransaksiKosong);
            txtPengeluaran = findViewById(R.id.txtPengeluaran);
            txtPemasukan = findViewById(R.id.txtPemasukan);
            txtBalance = findViewById(R.id.txtBalance);
            barChart = findViewById(R.id.chartHistoriTransaksi);
            btnAddTransaction = findViewById(R.id.btnTambahTransaksi);
            fragmentAddTransaction = findViewById(R.id.frameLayoutAddTransaction);
            imageHistoriTransaksiKosong = findViewById(R.id.imageHistoriTransaksiKosong);
            btnEdit = findViewById(R.id.btnEdit);
            bubblePopup = findViewById(R.id.bubblePopup);

            // Init Operations
            // Inflate Fragment
            btnAccount.setText("Halo, " + username + "!");
            // Retrieve user data
            User user = new User();
            user = databaseHandler.getUser(username);
            int userBalance = user.getBalance();

            btnAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isOpenLogoutFragment){
                        isOpenLogoutFragment = true;
                        bubblePopup.setVisibility(View.VISIBLE);
                        Fragment fragment = new LogoutFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.containerFragmentLogout, new LogoutFragment()).addToBackStack("logoutFragment").commit();
                    } else {
                        isOpenLogoutFragment = false;
                        bubblePopup.setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                }
            });

            btnAddTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Call fragment and pass data
                    AddTransaction fragment = AddTransaction.newInstance(userId);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                    fragmentTransaction.add(R.id.frameLayoutAddTransaction, fragment);
                    fragmentTransaction.commit();
                    fragmentAddTransaction.setVisibility(View.VISIBLE);
                }
            });

            // Initial data retrieval
            // User Balance
            txtBalance.setText(formatBalance(userBalance));
            // User Income
            txtPemasukan.setText(formatBalance(databaseHandler.getIncome(userId)));
            // User Outcome
            txtPengeluaran.setText(formatBalance(databaseHandler.getOutcome(userId)));
            // History Data
            arrHistory = databaseHandler.getAllData(userId);
            if(arrHistory.size() == 0){
                txtHistoriTransaksiKosong.setVisibility(View.VISIBLE);
                imageHistoriTransaksiKosong.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.INVISIBLE);
            }
            else{
                txtHistoriTransaksiKosong.setVisibility(View.INVISIBLE);
                imageHistoriTransaksiKosong.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.VISIBLE);
            }
            // Set RecyclerView Adapter
            adapter = new HistoryRecyclerViewAdapter(arrHistory);
            recyclerView = findViewById(R.id.recyclerHistoriTransaksi);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
            recyclerView.setAdapter(adapter);
            // Chart
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0f, new float[]{databaseHandler.getIncome(userId)}));
            entries.add(new BarEntry(1f, new float[]{databaseHandler.getOutcome(userId)}));
            BarDataSet dataSet = new BarDataSet(entries, "Income vs Outcome");
            dataSet.setColors(new int[]{Color.GREEN, Color.RED});
            BarData barData = new BarData(dataSet);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Income", "Outcome"}));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(2);
            barChart.disableScroll();
            barChart.setDragEnabled(false);
            barChart.setDrawGridBackground(false);
            barChart.setDescription(null);
            barChart.animateXY(2000, 2000);
            barChart.setData(barData);
            barChart.invalidate();
        }
        else{
            Toast.makeText(this, "Login lagi dong", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void updateData(String username){
        User user = new User();
        user = databaseHandler.getUser(username);


        // User Balance
        txtBalance.setText(formatBalance(user.getBalance()));
        // User Income
        txtPemasukan.setText(formatBalance(databaseHandler.getIncome(user.getId())));
        // User Outcome
        txtPengeluaran.setText(formatBalance(databaseHandler.getOutcome(user.getId())));
        // Chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, new float[]{databaseHandler.getIncome(user.getId())}));
        entries.add(new BarEntry(1f, new float[]{databaseHandler.getOutcome(user.getId())}));
        BarDataSet dataSet = new BarDataSet(entries, "Income vs Outcome");
        dataSet.setColors(new int[]{Color.GREEN, Color.RED});
        BarData barData = new BarData(dataSet);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Income", "Outcome"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(2);
        barChart.setData(barData);
        barChart.invalidate();
        // RecyclerView
        arrHistory = databaseHandler.getAllData(user.getId());
        adapter.updateArray(arrHistory);
        adapter.notifyDataSetChanged();
        // Ilangin ato munculin empty status
        if(arrHistory.size() != 0){
            imageHistoriTransaksiKosong.setVisibility(View.INVISIBLE);
            txtHistoriTransaksiKosong.setVisibility(View.INVISIBLE);
            barChart.setVisibility(View.VISIBLE);
        }
        else{
            imageHistoriTransaksiKosong.setVisibility(View.VISIBLE);
            txtHistoriTransaksiKosong.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.INVISIBLE);
        }
    }
}