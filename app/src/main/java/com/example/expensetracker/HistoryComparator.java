package com.example.expensetracker;

import java.util.Comparator;

public class HistoryComparator implements Comparator<History> {
    @Override
    public int compare(History history1, History history2) {
        return history2.getDateTime().compareTo(history1.getDateTime());
    }
}