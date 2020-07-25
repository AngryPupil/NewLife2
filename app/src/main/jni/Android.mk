LOCAL_PATH := $(call my-dir)
CRYSTAX_PATH := E:\install\crystax-ndk-10.3.2

include $(CLEAR_VARS)
LOCAL_MODULE    := jni_test
LOCAL_SRC_FILES := jni_test.c
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES := python3.6m
include $(BUILD_SHARED_LIBRARY)

# Include libpython3.6m.so

include $(CLEAR_VARS)
LOCAL_MODULE    := python3.6m
LOCAL_SRC_FILES := E:\install\lib-source\CLE\android.python.3.6.6\$(TARGET_ARCH_ABI)\libpython3.6m.so
LOCAL_EXPORT_CFLAGS := -I E:\install\lib-source\CLE\android.python.3.6.6\$(TARGET_ARCH_ABI)\include\python3.6m
include $(PREBUILT_SHARED_LIBRARY)