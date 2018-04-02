package net.pupil.newlife.vim.sdk;

import android.content.Context;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.royasoft.im.api.IMClient;
import com.royasoft.im.api.Message;
import com.royasoft.im.api.ResponseFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.pupil.newlife.vim.sdk.PushConsts.APP_SEC;


public class LinkagePushUtils {

    private static Context mContext;
    private String  sdkVer = "1.0";


    private static LinkagePushUtils instance;

    private static ResponseFuture connectResponseFuture;
    private Handler mHandler1;


    public static LinkagePushUtils getInstance(Context context) {
        if (instance == null) {
            instance = new LinkagePushUtils();
            instance.mContext = context;
        }
        return instance;
    }


    /**
     * 获取离线信息
     */
    public static void getOfflineMessages()
    {
        IMClient. getOfflineMessages ();
    }

    public void disconnect()
    {
        Log.i("=====","disconnect");
        IMClient.disconnect(mContext);
    }
    //

    public static void initPushUser(Context context,final String deviceID,String userId,final ConnectPushInterface myInterface)
    {
        mContext = context;
        OkHttpClient client = new OkHttpClient();
        String url = PushConsts.SERVER_IP;

        String data = getNetWorkSettingHashMap( deviceID,userId);

        try {

            data = Encrypt(data, APP_SEC);

        }catch (Exception e){
            e.printStackTrace();
        }
        FormBody.Builder params=new FormBody.Builder();
        params.add("r_a_k", PushConsts.APP_KEY);
        params.add("r_b",data);
//        params.add("r_b",userId);
        try {
        OkHttpClient okHttpClient=new OkHttpClient();

        Request request=new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call arg0, Response response) throws IOException {
                //   响应成功  response.body().string() 获取字符串数据，当然还可以获取其他
                try {
                    String htmlStr =  response.body().string();
                    JSONObject jsonobj = null;
                    jsonobj = new JSONObject(htmlStr);

                    if (jsonobj.optInt("code") == 1) {
                        myInterface.SuccessConnectPush();

                    }else{
                        myInterface.FailerConnectPush(jsonobj.optInt("code"),jsonobj.getString("desc"));
                    }
                } catch (JSONException e) {
                    myInterface.FailerConnectPush(-1000,"json 错误");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                //   响应失败
                String url = PushConsts.SERVER_IP;
                myInterface.FailerConnectPush(-1001,"通讯错误");
            }
        });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     *
     * @param context
     * @param appid        应用id
     * @param appkey       应用秘钥
     * @param deviceId     设备id，用于登录
     * @param connectPushInterface  返回结果
     */
    public static void connectPush(Context context,String appid, String appkey, String deviceId,final ConnectPushInterface connectPushInterface)
    {
        mContext = context;
        connectResponseFuture = IMClient.connect(mContext,appid,appkey,deviceId,true);

        connectResponseFuture.setCallback(new ResponseFuture.Callback() {
                                              @Override
                                              public void onSuccess(Object resp) {
                                                  PushReceiver pushReceiver = new PushReceiver();
                                                  IMClient.registerReceiver(mContext, pushReceiver);

                                                  connectPushInterface.SuccessConnectPush();

                                              }

                                              @Override
                                              public void onError(String msg) {
                                                  connectPushInterface.FailerConnectPush(-1, msg);
                                                  String htmlStr = msg;
                                              }
                                          }
            );

    }


    /**
     * 关闭连接
     */
    public static void closePush()
    {

        IMClient.disconnect(mContext);

    }

    public static void UploadFile(String file,int timeout, UploadCallback uploadCallback)
    {
        IMClient.uploadFile(file,timeout,uploadCallback);
    }

    public static void sendMessage(int type,String text, String deviceID,String extended,MessageCallback callback)
    {
        IMClient.sendMessage(deviceID,type,text,extended).setCallback(callback);
    }

    public static void sendGroupMessage(int type,String text, String groupID,String extended,MessageCallback callback)
    {
        IMClient.sendMessageToGroup(groupID,type,text,extended).setCallback(callback);
   }

    public static void sendBatchMessages(int type, String text, List<String> deviceIDs, String extended, MessageCallback callback)
    {
        IMClient.sendBatchMessages(deviceIDs,type,text,extended).setCallback(callback);
    }


    public static void queryHisMsg(String deviceID, long beginTime, long endTime, int qerMessageNum, String lastIndex,final HisInterface hisInterface) {

        try {
            ResponseFuture ft = IMClient.queryHistoryMessages(deviceID, beginTime, endTime, qerMessageNum, lastIndex);
            ft.setCallback(new ResponseFuture.Callback() {
                @Override
                public void onSuccess(Object resp) {

                    hisInterface.onSuccess(resp);

                }

                @Override
                public void onError(String msg) {
                    hisInterface.onError(-1, msg);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void queryGroupHisMsg(String groupId, long beginTime, long endTime, int qerMessageNum, String lastIndex,final HisInterface hisInterface)
    {

        ResponseFuture ft = IMClient.queryGroupHistoryMessages(groupId, beginTime, endTime, qerMessageNum, lastIndex);
        ft.setCallback(new ResponseFuture.Callback() {
                           @Override
                           public void onSuccess(Object resp) {

                               hisInterface.onSuccess(resp);

                           }

                           @Override
                           public void onError(String msg) {
                               hisInterface.onError(-1,msg);
                           }
                       }
        );

    }

    //加密
    private static String Encrypt(String message, String key)
            throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptbyte = cipher.doFinal(message.getBytes());
        return new String(Base64.encode(encryptbyte, Base64.DEFAULT));
    }


    private static String Decrypt(String message, String key)
            throws Exception {

        message = message.replaceAll(" ","+");
        byte[] bytesrc = Base64.decode(message.getBytes(), Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    /**
     * 向公共后台发送用户信息
     * @param userId 用户id
     * timestamp  时间戳
     * random     随机数
     * registerId 设备id   appid+userid+随机数
     * appKey     设备key
     * deviceType android 或是 ios
     * @return
     */
    private  static String getNetWorkSettingHashMap(String deviceID, String userId) {
        String jsonresult = "";
        JSONObject jsonObjM = new JSONObject();
        try {

            long time = System.currentTimeMillis();
            JSONObject jsonObj = new JSONObject();//pet对象，json形式
            jsonObjM.put("timestamp",time);//向pet对象里面添加值
            jsonObjM.put("random", String.valueOf((int) ((Math.random() * 9 + 1) * 10000)));

            jsonObjM.put("appKey", PushConsts.APP_KEY);
            jsonObj.put("userId", userId);
            jsonObj.put("deviceType", "android");
            jsonObj.put("deviceId", deviceID);


//            // 把每个数据当作一对象添加到数组里
            jsonObjM.put("object",jsonObj);//向json数组里面添加pet对象
            jsonresult = jsonObjM.toString();//生成返回字符串
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonresult;
    }








}
