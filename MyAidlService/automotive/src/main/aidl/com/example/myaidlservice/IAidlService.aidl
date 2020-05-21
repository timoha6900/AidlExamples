package com.example.myaidlservice;

import com.example.myaidlservice.IAidlServiceCallback;

interface IAidlService {

    void registerCallback(IAidlServiceCallback cb);

    void unregisterCallback(IAidlServiceCallback cb);

}
