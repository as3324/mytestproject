import com.yej.test.A;

/**
 * @Author: yej
 * @Date: 2019/8/30 12:19
 * @Version 1.0
 */
public class ClassA implements IClassA {
    @Override
    public void call() {

    }

    public static void main(String[] args) {
        System.out.println("hello");
        new A();
    }
}
