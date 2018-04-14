/**
 * Created by maozy on 2018/4/13.
 */
public class TryCatchFinally {

    @SuppressWarnings("finally")
    public static final String test() {
        String t = "";
        try {
            t = "try";
            return t;
        } catch (Exception e) {
            t = "catch";
            return t;
        } finally {
            t = "finally";
        }
    }

    public static void main(String[] args) {
        System.out.print(TryCatchFinally.test());
    }

}