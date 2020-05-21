package com.example.myaidlservice;

oneway interface IAidlServiceCallback {

    void valueChanged(String wheather, int temperature);
}
