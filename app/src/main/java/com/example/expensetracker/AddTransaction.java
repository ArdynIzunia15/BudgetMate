package com.example.expensetracker;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;

public class AddTransaction extends Fragment {

    View view;
    TextInputEditText inputTransactionTitle, inputTotal;
    MaterialButton btnTambah, btnCancel;
    RadioButton radioPengeluaran, radioPemasukan;
    TextInputLayout layoutInputTransactionTitle, layoutInputTotal;
    RadioGroup radioGroupTransactionType;
    boolean isValidInputTransactionTitle;
    boolean isValidInputTotal;
    boolean isValidTransactionType;

    public static AddTransaction newInstance(int userId) {
        AddTransaction fragment = new AddTransaction();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        // Init Components
        inputTransactionTitle = view.findViewById(R.id.inputTransactionTitle);
        inputTotal = view.findViewById(R.id.inputTotal);
        btnTambah = view.findViewById(R.id.btnTambah);
        btnCancel = view.findViewById(R.id.btnBatal);
        radioPengeluaran = view.findViewById(R.id.radioPengeluaran);
        radioPemasukan = view.findViewById(R.id.radioPemasukan);
        layoutInputTransactionTitle = view.findViewById(R.id.layoutInputTransactionTitle);
        layoutInputTotal = view.findViewById(R.id.layoutInputTotal);
        radioGroupTransactionType = view.findViewById(R.id.radioGroupTransactionType);

        ThousandFormatterTextWatcher textWatcher = new ThousandFormatterTextWatcher(inputTotal);
        inputTotal.addTextChangedListener(textWatcher);
        inputTransactionTitle.addTextChangedListener(new CapitalizeTextWatcher(inputTransactionTitle));

        int userId = getArguments().getInt("userId");

        // Init operations
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                // Nama Transaksi
                if(inputTransactionTitle.getText().toString().equals("")){
                    isValidInputTransactionTitle = false;
                    layoutInputTransactionTitle.setErrorEnabled(true);
                    layoutInputTransactionTitle.setError("Tolong diisi yah");
                }
                else{
                    isValidInputTransactionTitle = true;
                    layoutInputTransactionTitle.setErrorEnabled(false);
                    layoutInputTransactionTitle.setError(null);
                }
                // Radio Group
                if(radioPengeluaran.isChecked() || radioPemasukan.isChecked()){
                    isValidTransactionType = true;
                }
                else{
                    isValidTransactionType = false;
                }
                // Jumlah Transaksi
                if(inputTotal.getText().toString().equals("")){
                    isValidInputTotal = false;
                    layoutInputTotal.setErrorEnabled(true);
                    layoutInputTotal.setError("Tolong diisi yah");
                }
                else{
                    isValidInputTotal = true;
                    layoutInputTotal.setErrorEnabled(false);
                    layoutInputTotal.setError(null);
                }
                if(isValidInputTotal && isValidTransactionType && isValidInputTransactionTitle){
                    addRecord(userId);
                }
                else {
                    Toast.makeText(getActivity(), "Masih false", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void addRecord(int userId){
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        History history = new History();

        // Create History Object
        history.setUserId(userId);
        String transactionTitle = inputTransactionTitle.getText().toString();
        history.setTransaksiJudul(transactionTitle);
        int totalTransaction = Integer.parseInt(inputTotal.getText().toString().replace(",", ""));
        history.setJumlahTransaksi(totalTransaction);
        // Income = 0 ; outcome = 1
        if(radioPemasukan.isChecked()){
            history.setTipeTransaksi(0);
        }
        else if (radioPengeluaran.isChecked()){
            history.setTipeTransaksi(1);
        }
        history.setDateTime(LocalDateTime.now());
        // Substract or add the balance
        if(history.getTipeTransaksi() == 0){
            databaseHandler.updateBalance(userId, databaseHandler.getBalance(userId) + history.getJumlahTransaksi());
        }
        else if (history.getTipeTransaksi() == 1){
            databaseHandler.updateBalance(userId, databaseHandler.getBalance(userId) - history.getJumlahTransaksi());
        }
        // Push to table
        System.out.println("MAU MASUKIN KE DATABASE");
        if(databaseHandler.addHistoryRecord(history) == 1){
            System.out.println("BENAR");
            Toast.makeText(getActivity(), "Transaksi berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
        }
        else{
            System.out.println("SALAH");
            Toast.makeText(getActivity(), "Transaksi gagal ditambahkan", Toast.LENGTH_SHORT).show();
        }

        // Update Data
        String username = databaseHandler.getUsername(userId);
        ((DashboardActivity)getActivity()).updateData(username);
        closeFragment();
    }

    private void closeFragment() {
        // Get the fragment manager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Remove this fragment from its container
        fragmentManager.beginTransaction().remove(this).commit();
    }
}