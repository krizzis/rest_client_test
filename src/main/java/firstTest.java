import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class firstTest {

    public static String baseUrl =  "http://bpm-uat-app2.luxoft.com:18088";
    public static Client client = ClientBuilder.newClient();

    public static void testLogin () {


    WebTarget webTarget = client.target(baseUrl).path("login");

    Form form = new Form();

    form.param("username","kiev\\vbelaiev");
    form.param("password","");
    form.param("submit","Login");

    Invocation.Builder builder = webTarget.request();
    Response response = builder.post(Entity.form(form));
        System.out.println(response.getStatusInfo());


        Map<String, NewCookie> cookieMap = response.getCookies();



        for(Map.Entry<String,NewCookie> entry: cookieMap.entrySet()){
            System.out.println(entry.getKey()+": "+entry.getValue());
        }

        System.out.println(response.getHeaderString("Set-Cookie"));
    }

    public static void authInfo(){

        WebTarget webTarget = client.target(baseUrl).path("rest/auth");
        System.out.println("-----");
        Response response = webTarget.request().get();

        System.out.println(response.getHeaderString("Set-Cookie"));


    }

    public static void main(String[] args) {
        testLogin();

        authInfo();
    }


}
