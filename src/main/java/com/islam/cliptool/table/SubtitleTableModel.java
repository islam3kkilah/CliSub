/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliptool.table;

import com.islam.cliptool.model.Subtitle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author i3akk
 */
public class SubtitleTableModel extends AbstractTableModel{
    private final String[] cols = {"#", "Start", "End", "Subtitle"};
    private List<Subtitle> data = new ArrayList<>();

    public void setData(List<Subtitle> data) {
        this.data = data;
        fireTableDataChanged();
    }
    
    public List<Subtitle> getData() {
        return data;
    }
    
    @Override
    public int getRowCount() {
        return Math.max(1, data.size());
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int col) {
        return cols[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
         if (data.isEmpty()) {
            return switch (col) {
                case 0 -> "1";
                case 1 -> "00:00:00,000";
                case 2 -> "00:00:05,000";
                case 3 -> "Drag & Drop an SRT File, or Begin Translating";
                default -> "";
            };
        }
        Subtitle s = data.get(row);

        return switch (col) {
            case 0 -> s.getIndex();
            case 1 -> format(s.getStartTime());
            case 2 -> format(s.getEndTime());
            case 3 -> s.getText();
            default -> "";
        };
    }

    private String format(long ms) {

        long h = ms / 3600000;
        ms %= 3600000;

        long m = ms / 60000;
        ms %= 60000;

        long s = ms / 1000;
        long millis = ms % 1000;

        return String.format("%02d:%02d:%02d,%03d",h,m,s,millis);
}
}
