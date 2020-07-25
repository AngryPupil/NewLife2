#include <Python.h>
#include <jni.h>
#include <android/log.h>

#define LOG(x) __android_log_write(ANDROID_LOG_WARN, "jni_test", (x))

/* --------------- */
/*   Android log   */
/* --------------- */

static PyObject *androidlog(PyObject *self, PyObject *args)
{
    char *str;
    if (!PyArg_ParseTuple(args, "s", &str))
        return NULL;

    LOG(str);
    Py_RETURN_NONE;
}


static PyMethodDef AndroidlogMethods[] = {
    {"log", androidlog, METH_VARARGS, "Logs to Android stdout"},
    {NULL, NULL, 0, NULL}
};


static struct PyModuleDef AndroidlogModule = {
    PyModuleDef_HEAD_INIT,
    "androidlog",        /* m_name */
    "Log for Android",   /* m_doc */
    -1,                  /* m_size */
    AndroidlogMethods    /* m_methods */
};


PyMODINIT_FUNC PyInit_androidlog(void)
{
    return PyModule_Create(&AndroidlogModule);
}

void setAndroidLog()
{
    // Inject  bootstrap code to redirect python stdin/stdout
    // to the androidlog module
    PyRun_SimpleString(
            "import sys\n" \
            "import androidlog\n" \
            "class LogFile(object):\n" \
            "    def __init__(self):\n" \
            "        self.buffer = ''\n" \
            "    def write(self, s):\n" \
            "        s = self.buffer + s\n" \
            "        lines = s.split(\"\\n\")\n" \
            "        for l in lines[:-1]:\n" \
            "            androidlog.log(l)\n" \
            "        self.buffer = lines[-1]\n" \
            "    def flush(self):\n" \
            "        return\n" \
            "sys.stdout = sys.stderr = LogFile()\n"
    );
}


/* --------------------------------------------------------------- */
/* 以上部分代码，为C语言为Python3编写拓展模块的标准模板代码。           */
/* 这套模板写起来有些繁琐，我们之前已经用SWIG自动化实现过拓展模块        */
/* 这部分代码主要功能是将Python的print输出连接到Android的Log输出中      */
/* 与我们要探讨的内容联系不大，无须感到困惑                             */
/* ---------------------------------------------------------------- */


/* java对应的native方法 */
JNIEXPORT jint
JNICALL Java_net_pupil_newlife_ndk_Util_run(JNIEnv *env, jobject obj, jstring path)
{
	LOG("Initializing the Python interpreter");
	const char *pypath = (*env)->GetStringUTFChars(env, path, NULL);

	// Build paths for the Python interpreter
	char paths[512];
	snprintf(paths, sizeof(paths), "%s:%s/stdlib.zip", pypath, pypath);

	// Set Python paths
	wchar_t *wchar_paths = Py_DecodeLocale(paths, NULL);
	Py_SetPath(wchar_paths);

	PyImport_AppendInittab("androidlog", PyInit_androidlog);
	Py_Initialize();//初始化Python解析器

	if (!Py_IsInitialized())
	{
		LOG("Initialize failed");
		return -1;
	}

	setAndroidLog();

	PyRun_SimpleString("import test_pil");

	// Cleanup
	(*env)->ReleaseStringUTFChars(env, path, pypath);

    PyMem_RawFree(wchar_paths);
	Py_Finalize();//释放Python解析器

	return 0;
}
