package com.zhp.jewhone.crawhtml;
import com.zhp.jewhone.core.util.file.CMyFileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class CrawHtmlTest {
    private static HttpClient httpClient=new DefaultHttpClient();

    //主登录入口
    private static void loginDouban(){
//      String redir="https://www.douban.com/people/144537495/";
        String login_src="https://passport.tuniu.com/login";
        //输入用户名及密码
        String username="18280489256";
        String password="ll25734892";
        //获取验证码
       // String captcha_id=getImgID();
        String login="登录";
        /*String captcha_solution="";
        //输入验证码
        System.out.println("请输入验证码：");
        BufferedReader buff=new BufferedReader(new InputStreamReader(System.in));
        try {
            captcha_solution=buff.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //构建参数，即模拟需要输入的参数。这部分通过抓包获得。不会抓包的请看我之前写的一些博客
        List<NameValuePair> list= new ArrayList<>();
        list.add(new BasicNameValuePair("username", username));
        list.add(new BasicNameValuePair("password", password));
        //list.add(new BasicNameValuePair("captcha-solution", captcha_solution));
        //list.add(new BasicNameValuePair("captcha-id", captcha_id));
        list.add(new BasicNameValuePair("submit_login", login));
        HttpPost httpPost = new HttpPost(login_src);
        try {
            //向后台请求数据,登陆网站
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse response=httpClient.execute(httpPost);
            HttpEntity entity=response.getEntity();
            String result=EntityUtils.toString(entity,"utf-8");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*
     * 获取验证码图片“token”值
     * @return token
     */
   /* private static String getImgID(){
        //Json的地址[数据中包含验证码的地址]
        String src="https://www.douban.com/j/misc/captcha";
        HttpGet httpGet=new HttpGet(src);
        String token="";
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity=response.getEntity();
            //将json数据转化为map，对应的是key，value的形式。不理解json数据的，请看我前面的关于json解析的博客
            String content=EntityUtils.toString(entity,"utf-8");
            Map<String,String> mapList=getResultList(content);
            token=mapList.get("token");
            //获取验证码的地址
            String url="https:"+mapList.get("url");
            //下载验证码并存储到本地
            downImg(url);
            //System.out.println(token);
            //System.out.println(url);
            //System.out.println(content);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return token;
    }*/
    /*
     * 用JSON 把数据格式化，并生成迭代器，放入Map中返回
     * @param content 请求验证码时服务器返回的数据
     * @return Map集合
     */
    /*public static Map<String,String> getResultList(String content){
        Map<String,String> maplist=new HashMap<String,String>();
        try {
            JSONObject jo=new JSONObject(content.replaceAll(",\\\"r\\\":false", ""));
            Iterator it = jo.keys();
            String key="";
            String value="";
            while(it.hasNext()){
                key=(String) it.next();
                value=jo.getString(key);
                maplist.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maplist;
    }*/
    /*
     * 此方法是下载验证码图片到本地
     * @param src  给个验证图片完整的地址
     * @throws IOException
     */
    /*private static void downImg(String src) throws IOException{
        File fileDir=new File("E:\\钱洋个人\\IdentifyingCode");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        //图片下载保存地址
        File file=new File("E:\\钱洋个人\\IdentifyingCode\\yzm.png");
        if(file.exists()){
            file.delete();
        }
        InputStream input = null;
        FileOutputStream out= null;
        HttpGet httpGet=new HttpGet(src);
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            input = entity.getContent();
            int i=-1;
            byte[] byt=new byte[1024];
            out=new FileOutputStream(file);
            while((i=input.read(byt))!=-1){
                out.write(byt);
            }
            System.out.println("图片下载成功！");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }*/
    //登陆后，便可以输入一个或者多个url，进行请求
    private static String gethtml(String redirectLocation) {
        HttpGet httpget = new HttpGet(redirectLocation);
        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody;
        try {
            responseBody = httpClient.execute(httpget, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = null;
        } finally {
            httpget.abort();
//            httpClient.getConnectionManager().shutdown();
        }
        return responseBody;
    }
    //一个实例，只请求了一个url
    public static void main(String[] args) throws Exception {
        loginDouban();
        String redir="http://i.tuniu.com/list";
        String cc=gethtml(redir);
        CMyFileUtils.writeFile("D://text.html",cc,"utf-8");

    }
}