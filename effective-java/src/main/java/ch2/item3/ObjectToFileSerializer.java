package ch2.item3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectToFileSerializer<T> {

    public static <T> void serialize(T item, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(item);
            System.out.println("Serializer Success.");
        } catch (Exception e) {
            System.out.println("Serialize Fail : " + e.getMessage());
        }
    }

    public static <T> T deserialize(String filename) {
        T item = null;
        try (FileInputStream fis = new FileInputStream(filename);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            item = (T) in.readObject();
            System.out.println("Deserialize Success.");

        } catch (Exception e) {
            System.out.println("Deserialize Fail : " + e.getMessage());
        }
        return item;
    }

}
