#ifndef __AIDL_NATIVE_SERVICE_H__
#define __AIDL_NATIVE_SERVICE_H__

#include "../common/IAidlNativeService.h"


class AidlNativeService : public BnAidlNativeService
{
public:
    String16 GetWeather();
    int GetTemperature();
    static void instance()
    {
        defaultServiceManager()->addService(String16(SERVICE_NAME), new AidlNativeService());
    };
};

#endif /* __AIDL_NATIVE_SERVICE_H__ */








