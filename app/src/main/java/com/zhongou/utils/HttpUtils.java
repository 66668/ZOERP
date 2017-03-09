package com.zhongou.utils;

import android.util.Log;

import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.common.HttpParameter;
import com.zhongou.common.MyException;
import com.zhongou.common.NetworkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * 访问网络方法
 * (2)检查网络设置，
 * 13 14 获取json的JSONObject的方法，(HttpURLConnection post方式,HashMap<String,String>)
 * 15 获取json的JSONObject的方法(HttpURLConnection post方式, HashMap<String,String>和File)
 * 16 对JSON结果统一处理 handleResult
 *
 * @author JackSong
 */
public class HttpUtils {

    public static final int TIMEOUT1 = 10;// 超时设置
    public static final int TIMEOUT = 10000;// 超时设置

    /**
     * 单例模式设置
     */
    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        return HttpUtilsContainer.instance;
    }

    public static class HttpUtilsContainer {
        private static HttpUtils instance = new HttpUtils();
    }

    /**
     * 检查网络
     *
     * @throws MyException
     */
    void checkNetwork() throws MyException {
        if (!NetworkManager.isNetworkAvailable(MyApplication.getInstance())) {
            throw new MyException(R.string.network_invalid);
        }
    }

    /**
     * 01 url
     * <p>
     * HttpURLConnection Get
     *
     * @param urlStr
     * @return
     * @throws MyException
     * @throws Exception
     */
    public JSONObject getByURL(String urlStr) throws MyException {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT);// 读取超时
            conn.setConnectTimeout(TIMEOUT);// 链接超时
            conn.setRequestMethod("GET");// get
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                Log.d("HTTP", baos.toString());
                JSONObject jsonObject = new JSONObject(baos.toString());
                return jsonObject;
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            throw new MyException(e.getMessage());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            }
            // 关闭连接
            conn.disconnect();
        }
    }

    /**
     * 02 提交数据，发送请求参数(密码登录/修改密码/获取记录)
     * <p>
     * (HttpURLConnection post)
     *
     * @param urlPost
     * @param params
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws JSONException
     */
    public JSONObject postStringURL(String urlPost, HttpParameter params, boolean withLogin) throws MyException {
        if (!withLogin) {
            return null;
        }

        return postStringURL(urlPost, params);
    }

    /**
     * 02-01 提交数据，发送请求参数(密码登录/修改密码)
     * 纯参数
     * (HttpURLConnection post)
     *
     * @param urlPost
     * @param params
     * @return
     * @throws MyException
     */
    public JSONObject postStringURL(String urlPost, HttpParameter params) throws MyException {
        //变量
        JSONObject js = null;
        DataOutputStream outStream = null;
        InputStreamReader in = null;
        HttpURLConnection conn = null;
        //常量
        String BOUNDARY = java.util.UUID.randomUUID().toString();// 边界标识 随机生成
        String PREFIX = "--";
        String LINEND = "\r\n";
        String MULTIPART_FORM_DATA = "multipart/form-data";// 内容类型
        String CHARSET = "UTF-8";
        try {
            //(1)创建url对象
            URL url = new URL(urlPost);
            //(2)利用HttpURLConnectioncont从网络中获取数据
            conn = (HttpURLConnection) url.openConnection();
            //(3)设置连接要求
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);// 缓存最长时间
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);// 不允许缓存，避免多余问题
            conn.setRequestMethod("POST");// 请求方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "UTF-8");// 设置编码
            conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY);
            conn.connect();

            /*
             * 首先组拼文本类型参数
			 */
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);//--
                sb.append(BOUNDARY);
                sb.append(LINEND);//\r\n
                sb.append("Content-Disposition:form-data;name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
            // 获取输出流
            outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(sb.toString().getBytes());
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();


            //(4)得到响应码:根据公司的js编写
            int res = conn.getResponseCode();

            if (res == 200) {

                //(5)得到数据流
                in = new InputStreamReader(conn.getInputStream(), "utf-8");
                int ch;
                StringBuilder sb3 = new StringBuilder();

                while ((ch = in.read()) != -1) {
                    sb3.append((char) ch);
                }
                js = new JSONObject(sb3.toString());
                return js;
            } else {
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(e.toString());
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                throw new MyException("你竟然敢报IO异常");
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                throw new MyException("你竟然敢报IO异常");
            }
            // 关闭连接
            conn.disconnect();
        }

        return js;
    }

    /**
     * <p>
     * 上传 文本和文件
     * <p>
     * (HttpURLConnection post )
     *
     * @param urlPost
     * @param params
     * @param files
     * @return
     * @throws MyException
     */

    public JSONObject postStringURL(String urlPost, Map<String, String> params, File files) throws MyException {
        //变量
        DataOutputStream outStream = null;
        InputStreamReader in = null;
        HttpURLConnection conn = null;
        //常量
        String BOUNDARY = java.util.UUID.randomUUID().toString();// 边界标识 随机生成
        String PREFIX = "--";
        String LINEND = "\r\n";
        String MULTIPART_FORM_DATA = "multipart/form-data";// 内容类型
        String CHARSET = "UTF-8";

        try {
            URL url = new URL(urlPost);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);//conn-->HttpURLConnection
            conn.setReadTimeout(TIMEOUT);// 缓存最长时间
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);// 不允许缓存
            conn.setRequestMethod("POST");// 请求方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "UTF-8");// 设置编码
            conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY);
            conn.connect();
            /*
             * 首先组拼文本类型参数
			 */
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition:form-data;name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
            // 获取输出流
            outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(sb.toString().getBytes());

			/*
             * 拼接文件数据
			 */
            if (files != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(PREFIX);
                sb2.append(BOUNDARY);
                sb2.append(LINEND);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb2.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + files.getName() + "\"" + LINEND);
                sb2.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb2.append(LINEND);

                outStream.write(sb2.toString().getBytes());
                // ?
                InputStream is = new FileInputStream(files);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();

            // 得到响应码:根据公司的js编写
            int res = conn.getResponseCode();
            JSONObject js = null;
            if (res == 200) {
                //(5)得到数据流
                in = new InputStreamReader(conn.getInputStream(), "utf-8");
                int ch;
                StringBuilder sb3 = new StringBuilder();
                while ((ch = in.read()) != -1) {
                    sb3.append((char) ch);
                }
                js = new JSONObject(sb3.toString());
                return js;
            }
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
            // 关闭连接
            conn.disconnect();
        }
        return null;
    }


    /**
     * <p>
     * 03 上传 文本 和 文件数组
     * <p>
     * (HttpURLConnection post )
     *
     * @param urlPost
     * @param params
     * @param files
     * @return
     * @throws MyException
     */

    public JSONObject postMultiURL(String urlPost, Map<String, String> params, File[] files) throws MyException {
        //变量
        DataOutputStream outStream = null;
        InputStreamReader in = null;
        HttpURLConnection conn = null;
        //常量
        String BOUNDARY = java.util.UUID.randomUUID().toString();// 边界标识 随机生成
        String PREFIX = "--";
        String LINEND = "\r\n";
        String MULTIPART_FORM_DATA = "multipart/form-data";// 内容类型
        String CHARSET = "UTF-8";

        try {
            URL url = new URL(urlPost);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);//conn-->HttpURLConnection
            conn.setReadTimeout(TIMEOUT);// 缓存最长时间
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);// 不允许缓存
            conn.setRequestMethod("POST");// 请求方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "UTF-8");// 设置编码
            conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY);
            conn.connect();
            /*
             * 首先组拼文本类型参数
			 */
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition:form-data;name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
            // 获取输出流
            outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(sb.toString().getBytes());


			/*
             * 拼接文件数据
			 */

            for (int i = 0; i < files.length; i++) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(PREFIX);
                sb2.append(BOUNDARY);
                sb2.append(LINEND);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb2.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + files[i].getName() + "\"" + LINEND);
                sb2.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb2.append(LINEND);

                outStream.write(sb2.toString().getBytes());
                // ?
                InputStream is = new FileInputStream(files[i]);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }


            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();

            // 得到响应码:根据公司的js编写
            int res = conn.getResponseCode();
            JSONObject js = null;
            if (res == 200) {
                //(5)得到数据流
                in = new InputStreamReader(conn.getInputStream(), "utf-8");
                int ch;
                StringBuilder sb3 = new StringBuilder();
                while ((ch = in.read()) != -1) {
                    sb3.append((char) ch);
                }
                js = new JSONObject(sb3.toString());
                return js;
            }
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
            // 关闭连接
            conn.disconnect();
        }
        return null;
    }


    /**
     * 16 对JSON结果统一处理handleResult
     *
     * @param jsonObject
     * @param successRunnable
     * @param errorRunnable
     * @return
     */
    public static String handleResult(JSONObject jsonObject, Runnable successRunnable, Runnable errorRunnable) {
        String result = "";
        String message = "";
        try {
            String code = jsonObject.getString("code");
            result = jsonObject.getString("result");
            message = jsonObject.getString("message");
            if (code.equals("1")) {
                if (successRunnable != null) {
                    successRunnable.run();
                }
            } else {
                if (errorRunnable != null) {
                    errorRunnable.run();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 获取附加头信息 在MyHttpGet 和MyHttpPost 中调用
     *
     * @return
     */
    public HashMap<String, String> getHeaders() {
        HashMap<String, String> map = new HashMap<String, String>();

        return map;
    }

} 
