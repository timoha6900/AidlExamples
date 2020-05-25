LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
    ../common/IAidlNativeService.cpp \
    AidlNativeService.cpp

LOCAL_SHARED_LIBRARIES:= \
	libcutils \
	libutils \
	libbinder \
	liblog

LOCAL_MODULE := AidlNativeService

include $(BUILD_EXECUTABLE)
