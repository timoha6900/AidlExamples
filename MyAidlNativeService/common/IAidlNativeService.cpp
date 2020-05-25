#define LOG_NDEBUG 0
#define LOG_TAG "AidlNativeService"
  
#include <log/log.h>

#include "IAidlNativeService.h"

using namespace android;

class BpAidlNativeService : public BpInterface<IAidlNativeService>
{
public:
    BpAidlNativeService(const sp<IBinder>& impl) : BpInterface<IAidlNativeService>(impl){};
    String16 GetWeather()
    {
        ALOGD("BpAidlNativeService GetWeather");

        Parcel data;
        data.writeInterfaceToken(IAidlNativeService::getInterfaceDescriptor());

        Parcel reply;
        remote()->transact(GET_WEATHER, data, &reply);

        int32_t exception = reply.readExceptionCode();
        return reply.readString16();
    }
    int GetTemperature()
    {
        ALOGD("BpAidlNativeService GetTemperature");

        Parcel data;
        data.writeInterfaceToken(IAidlNativeService::getInterfaceDescriptor());

        Parcel reply;
        remote()->transact(GET_TEMPERATURE, data, &reply);

        int32_t exception = reply.readExceptionCode();
        return reply.readInt32();
    }
};

IMPLEMENT_META_INTERFACE(AidlNativeService, "com.example.aidlnativeservice.IAidlNativeService");


status_t BnAidlNativeService::onTransact(uint32_t code,const Parcel & data,Parcel * reply, uint32_t flags)
{
    switch(code)
    {
        case GET_WEATHER:
        {
            CHECK_INTERFACE(IAidlNativeService, data, reply);
            String16 ret = GetWeather();
            ALOGD("BnAidlNativeService GET_WEATHER ret:%s\n", ret.string());
            reply->writeNoException();
            reply->writeString16(ret);
            return NO_ERROR;
        }
        case GET_TEMPERATURE:
         {
            CHECK_INTERFACE(IAidlNativeService, data, reply);
            int ret = GetTemperature();
            ALOGD("BnAidlNativeService GET_TEMPERATURE ret:%d\n", ret);
            reply->writeNoException();
            reply->writeInt32(ret);
            return NO_ERROR;
         }
        default:
            return BBinder::onTransact(code, data, reply, flags);
    }
}











































