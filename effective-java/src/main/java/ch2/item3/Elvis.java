package ch2.item3;

import java.io.Serializable;

import static ch2.item3.ObjectToFileSerializer.*;

public class Elvis implements Serializable {

//    public static final 필드 방식의 싱글턴
//    private static final Elvis INSTANCE = new Elvis();

    private transient static final Elvis INSTANCE = new Elvis();

    private Elvis() {

    }

//     정적 팩터리 방식의 싱글턴
    public static Elvis getInstance() {
        return INSTANCE;
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        String filename = "elvis.txt";
        Elvis elvis = Elvis.getInstance();
        serialize(elvis, filename);
        Elvis e1 = deserialize(filename);
        Elvis e2 = deserialize(filename);

        System.out.println(e1 == e2);
    }
}
