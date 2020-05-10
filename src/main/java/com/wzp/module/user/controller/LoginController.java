package com.wzp.module.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.wzp.module.core.dto.ResultDataModel;
import com.wzp.module.core.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
@RestController
public class LoginController {


    @RequestMapping(value = "/open/user/authenticateByWx",method = RequestMethod.GET)
    public ResultDataModel authenticateByWx(@RequestParam("callback") String callback) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=wx688d7fb14884b6ed" +
                "&redirect_uri=http://127.0.0.1/api/open/user/loginFromWx" +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE" +
                "#wechat_redirect";
        response.sendRedirect(url);
        return ResultDataModel.handleSuccessResult();
    }

    @RequestMapping(value = "/open/user/loginFromWx",method = RequestMethod.GET)
    public ResultDataModel loginFromWx(@RequestParam("code") String code,@RequestParam("state") String state) throws IOException {

        // 获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx688d7fb14884b6ed&secret=5e6122cddbba0414251983e31a29b49d&code="
                + code + "&grant_type=authorization_code";
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse httpResponse = httpClient.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HttpServletResponse.SC_OK) {
            HttpEntity httpEntity = httpResponse.getEntity();
            String json = EntityUtils.toString(httpEntity);
            String accessToken = JSONObject.parseObject(json).getString("access_token");
            String openid = JSONObject.parseObject(json).getString("openid");

            String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
            HttpGet getUserInfo = new HttpGet(userInfoUrl);
            HttpResponse userInfoResponse = httpClient.execute(getUserInfo);
            if (userInfoResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
                String userInfo = EntityUtils.toString(userInfoResponse.getEntity(),"UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(userInfo);
                String temp = "{0}：{1}";
                System.out.println(MessageFormat.format(temp,"openid",jsonObject.getString("openid")));
                System.out.println(MessageFormat.format(temp,"nickname",jsonObject.getString("nickname")));
                System.out.println(MessageFormat.format(temp,"sex",jsonObject.getString("sex")));
                System.out.println(MessageFormat.format(temp,"language",jsonObject.getString("language")));
                System.out.println(MessageFormat.format(temp,"city",jsonObject.getString("city")));
                System.out.println(MessageFormat.format(temp,"province",jsonObject.getString("province")));
                System.out.println(MessageFormat.format(temp,"country",jsonObject.getString("country")));
                System.out.println(MessageFormat.format(temp,"headimgurl",jsonObject.getString("headimgurl")));
                System.out.println(MessageFormat.format(temp,"privilege",jsonObject.getString("privilege")));
            }
        }
        return ResultDataModel.handleFailureResult();
    }

    @RequestMapping(value = "/open/wechat/message")
    public String getMessageFromWx(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException, JAXBException, DocumentException {
        String token = "56m35QeuXEJKtczS";
        System.out.println(token);
        httpServletRequest.setCharacterEncoding("UTF-8");
        String echostr = httpServletRequest.getParameter("echostr");
        String timestamp = httpServletRequest.getParameter("timestamp");
        String nonce = httpServletRequest.getParameter("nonce");
        String signature = httpServletRequest.getParameter("signature");
        List<String> sortList = new ArrayList<>();
        sortList.add(token);
        sortList.add(timestamp);
        sortList.add(nonce);
        Collections.sort(sortList);
        StringBuilder builder = new StringBuilder();
        sortList.forEach(builder::append);
        String encrypted = DigestUtils.sha1Hex(builder.toString());
        if (encrypted.equals(signature)) {
            InputStream inputStream = httpServletRequest.getInputStream();
            httpServletResponse.setCharacterEncoding("UTF-8");
            Map<String,Object> xmlToMap = FileUtil.readXmlToMap(inputStream);
            PrintWriter  writer  = httpServletResponse.getWriter();
            String xml = mapToXml(xmlToMap);
            writer.print(xml);
        }
        return echostr;
    }

    private void createTxt(InputStream inputStream) throws IOException {
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}; // 设置文件为UTF-8格式
        String dirPath = FileUtil.getWebappPath() + "incoming";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String address = dirPath + "/res.txt";
        File file = new File(address);
        byte[] buffer = new byte[1024];
        OutputStream outPutStream = new FileOutputStream(file);
        int len = 0;
        while ((len = inputStream.read(buffer)) > 0) {
            outPutStream.write(buffer, 0, len);
        }
        inputStream.close();
        outPutStream.flush();
        outPutStream.close();
    }

    private String mapToXml(Map<String,Object> map) {
        String resultStr = "";
        String msgType = (String) map.get("MsgType");
        String event = (String) map.get("Event");
        String content = (String) map.get("Content");
        String xmlTemplate = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2}</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[{3}]]></Content></xml>";
        if ("event".equals(msgType) && "subscribe".equals(event)) {
            resultStr = "爷，进来玩玩嘛";
        } else if ("event".equals(msgType) && "unsubscribe".equals(event)) {
            resultStr = "滚";
        } else if ("text".equals(msgType)) {
            resultStr = "你刚才发送的信息是 ----> “" + content + "”";
        }
        return MessageFormat.format(xmlTemplate,map.get("FromUserName"),map.get("ToUserName"),System.currentTimeMillis() / 1000,resultStr);
    }


    @GetMapping("/open/test/get")
    public ResultDataModel get(@RequestParam("a") String a,@RequestParam("b") String b) {
        return ResultDataModel.handleSuccessResult(a + b);
    }

    @PostMapping("/open/test/post/body")
    public ResultDataModel body(@RequestBody Map<String,Object> map) {
        return ResultDataModel.handleSuccessResult("post_body : " + map.get("a").toString() + map.get("b").toString());
    }

    @PostMapping("/open/test/post/param")
    public ResultDataModel param(@RequestParam("a") String a,@RequestParam("b") String b) {
        return ResultDataModel.handleSuccessResult("post_param: " + a + b);
    }

    @PostMapping("/open/test/post/upload")
    public ResultDataModel upload(@RequestParam("pic") MultipartFile multipartFile,@RequestParam("fileName") String fileName) throws IOException {
        String dirPath = FileUtil.getWebappPath() + "incoming/";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        InputStream inputStream = multipartFile.getInputStream();
        String filePath = dirPath + fileName;
        File file = new File(filePath);
        byte[] buffer = new byte[1024];
        OutputStream outputStream = new FileOutputStream(file);
        int len = 0;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,len);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
        return ResultDataModel.handleSuccessResult();
    }

    @RequestMapping("/payNotify")
    public String payNotify(HttpServletRequest request,HttpServletResponse response){
        try {
            Map<String,Object> map =  FileUtil.readXmlToMap(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void main(String[] args) throws URISyntaxException, IOException {


    }





}
