/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliptool.model;

/**
 *
 * @author i3akk
 */
public class Subtitle {
    private int index;
    private long startTime;
    private long endTime;
    private String text;

    public Subtitle(int index, long startTime, long endTime, String text) {
        this.index = index;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    public int getIndex() { return index; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    public String getText() { return text; }
}
