package org.springframework.core.io;

import org.springframework.util.ResourceUtils;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件解析资源
 */
public abstract class AbstractFileResolvingResource extends AbstractResource {
    @Override
    public boolean exists() {

        try{
            URL url = getURL();
            if(ResourceUtils.isFileUrl(url)){
                //如果为文件资源定位符
                File file = getFile();
                //文件存在并且文件不是文件夹
                return file.canRead() && !file.isDirectory();
            }else{
                //如果为http网络资源，发送head请求，返回码为ok，则http资源存在
                //打开连接
                URLConnection con = url.openConnection();

                //自定义连接,发送header请求
                customizeConnection(con);
                if(con instanceof HttpsURLConnection){
                    HttpsURLConnection httpCon = (HttpsURLConnection)con;
                   int httpCode = httpCon.getResponseCode();
                   if(httpCode != HttpsURLConnection.HTTP_OK){
                       //请求失败，资源不存在
                       httpCon.disconnect();
                       return false;
                   }
                }

                //获取header内容
                long contentLength = con.getContentLengthLong();
                if(contentLength > 0){
                    return true;
                }else if(contentLength == 0){
                    return false;
                }else{
                    //再次判断是否为输入流资源
                    getInputStream().close();
                    return true;
                }
            }
        }catch (IOException e){
            return false;
        }
    }

    /**
     * 自定义连接
     * @param con
     */
    private void customizeConnection(URLConnection con) throws IOException {
        //设置缓存
        ResourceUtils.useCacheIfNecessary(con);
        if(con instanceof HttpURLConnection){
            //如果为http连接
            customizeConnection((HttpsURLConnection)con);
        }
    }

    /**
     * 自定义连接，发送header请求
     * @param con http连接
     */
    private  void customizeConnection(HttpURLConnection con) throws  IOException{
        con.setRequestMethod("HEAD");
    }
}
