

#include <binder/IServiceManager.h>
#include <binder/IPCThreadState.h>  
#include "../common/IAidlNativeService.h"
#include "AidlNativeService.h"
#include <stdlib.h>

#define LOG_NDEBUG 0
#define LOG_TAG "AidlNativeService"
  
#include <log/log.h>

const char *weathers[6] = {"Sunny", "Cloudy", "Rain", "Downpour", "Show", "Storm"};

String16 AidlNativeService::GetWeather()
{
    ALOGD("AidlNativeService GetWeather");
    int i = rand() % 6;
    String16 result = String16(weathers[i]);
    return result;
}

int AidlNativeService::GetTemperature()
{
    ALOGD("AidlNativeService GetTemperature");
    int result = rand() % 80 - 40;
    return result;
}

int main(int argc, char** argv)  
{  
    AidlNativeService::instance();

    ProcessState::self()->startThreadPool();  
    IPCThreadState::self()->joinThreadPool();  
  
    return 0;  
} 


































