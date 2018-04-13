import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maozy on 2018/3/28.
 */
public class Test {

    public static void main(String[] args) {

        Map<Object, Object> map = new HashMap<Object, Object>();

        List<String> list = (List<String>)map.get("aaa");

        System.out.println(list);



    }

    public static void bigger(Circle c2){

        Circle c3 = c2;

        c3.r = c3.r+3;

        System.out.println(c3.r);

    }

}


class Circle{

    int r;

}
