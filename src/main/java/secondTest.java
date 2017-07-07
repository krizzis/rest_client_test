import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by VBeliaev on 06.07.2017.
 */
public class secondTest {

    public static String baseUrl = "http://bpm-uat-app2.luxoft.com:18088";
    public static CookieStore cookieStore = new BasicCookieStore();
    public static HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    public static int requestId;
    public static int taskId;

    public static void Login () throws IOException {

        HttpPost post = new HttpPost(baseUrl+"/login");

        post.setHeader("Content-Type","application/x-www-form-urlencoded");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("username", "kiev\\vbeliaev"));
        urlParameters.add(new BasicNameValuePair("password", "1"));
        urlParameters.add(new BasicNameValuePair("submit", "Login"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "null");
        cookieStore.addCookie(cookie);
        cookie.setPath("/");
        cookie.setDomain("*.luxoft.com");

        client.execute(post);

    }

    public static void authInfo () throws IOException {

        HttpGet get = new HttpGet(baseUrl+"/rest/auth");
        HttpResponse response = client.execute(get);

        HttpEntity entity = response.getEntity();

        System.out.println(EntityUtils.toString(entity));

    }

    public static void search () throws Exception{

        HttpPost post = new HttpPost(baseUrl+"/rest/getLuxHireOfferByODataQueryStringAsyncFunc");
        post.setHeader("Content-Type", "application/json;  charset=utf-8");

        StringEntity body = new StringEntity("{\"odataQueryString\":\"$top=70&$filter=substringof('a',tolower(value)) eq true\"}");

        post.setEntity(body);

        HttpResponse response = client.execute(post);

        HttpEntity entity = response.getEntity();

        String stringEntity = EntityUtils.toString(entity);

        Gson gson = new Gson();

        SearchEntity searchEntity = gson.fromJson(stringEntity,SearchEntity.class);

        System.out.println(searchEntity.results.Items.get(0).id);
    }

    public static void newRequest () throws IOException {
        HttpPost post = new HttpPost(baseUrl+"/rest/request");

        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("processKey","GR");

        Gson gson = new Gson();
        StringEntity input = new StringEntity(gson.toJson(map));
        input.setContentType("application/json");

        post.setEntity(input);
        HttpResponse response = client.execute(post);

        String s = EntityUtils.toString(response.getEntity());
        BpmRequest request = gson.fromJson(s,BpmRequest.class);

        requestId = request.id;
        }

    public static HashMap<String,BpmTaskMeta> readTaskMeta(int reqId, int taskId) throws IOException {
        HttpGet get = new HttpGet(baseUrl+"/rest/request/"+reqId+"/task/"+taskId+"/meta");
        HttpResponse response = client.execute(get);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Value.class,new ValueDeserializer());

        Gson gson = builder.create();

        String json = EntityUtils.toString(response.getEntity());
        //System.out.println(json);

        BpmTaskMeta[] tasks = gson.fromJson(json, BpmTaskMeta[].class);
        //System.out.println(tasks[0].label);

        HashMap<String,BpmTaskMeta> map = new HashMap<String, BpmTaskMeta>();

        for (BpmTaskMeta task: tasks){
            map.put(task.label,task);
        }

        return map;

        //System.out.println(tasks[0].value.toString());

        //Value value = gson.fromJson(tasks[0].value.toString(),Value.class);

    }

    public static void getTasksInfo(int requestId) throws IOException {
        HttpPost post = new HttpPost(baseUrl+"/rest/request/"+requestId+"/progress-tracking");
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("filter",0);
        Gson gson = new Gson();

        StringEntity input = new StringEntity(gson.toJson(map));
        input.setContentType("application/json");

        post.setEntity(input);
        HttpResponse response = client.execute(post);

        String s = EntityUtils.toString(response.getEntity());
        BpmTask[] tasks = gson.fromJson(s,BpmTask[].class);

        taskId = tasks[0].id;

        System.out.println(taskId);
    }

    public static void saveTask(int requestId, int taskId, String comment, String type) throws IOException {
        HttpPost post = new HttpPost(baseUrl+"/rest/request/"+requestId+"/task/"+taskId+"/data");
        final HashMap<String,Object> map = new HashMap<String, Object>();
        Gson gson = new Gson();
        HashMap<String,BpmTaskMeta> tasks = readTaskMeta(requestId,taskId);

        map.put(tasks.get("Choose relocation type").field,type);
        map.put(tasks.get("Comment").field,comment);

        StringEntity input = new StringEntity(gson.toJson(map));
        input.setContentType("application/json");

        post.setEntity(input);
        HttpResponse response = client.execute(post);
    }


    public static void main(String[] args) throws Exception {
        Login();
        newRequest();
        getTasksInfo(requestId);
        saveTask(requestId,taskId,"test comment", "ext");


    }


}
