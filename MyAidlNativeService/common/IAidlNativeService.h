#ifndef __I_AIDL_NATIVE_SERVICE_H__
#define __I_AIDL_NATIVE_SERVICE_H__


#include <utils/RefBase.h>
#include <binder/IInterface.h>
#include <binder/Parcel.h>

using namespace android;

#define SERVICE_NAME    "com.example.aidlnativeservice"

enum {
    GET_WEATHER = IBinder::FIRST_CALL_TRANSACTION,
    GET_TEMPERATURE
};


class IAidlNativeService : public IInterface
{
public:
    DECLARE_META_INTERFACE(AidlNativeService);

    virtual String16 GetWeather() = 0;
    virtual int GetTemperature() = 0;
};


class BnAidlNativeService : public BnInterface<IAidlNativeService>
{
public:
    virtual status_t onTransact(uint32_t code, const Parcel& data, Parcel* reply, uint32_t flags = 0); 
};

#endif /* __I_AIDL_NATIVE_SERVICE_H__ */















