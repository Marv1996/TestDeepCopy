import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {

        try {
            List<String> books = new ArrayList<>(Arrays.asList("Book 1", "Book 2", "Book 3"));

            Man obj = new Man("John", 30, books);
            Man copy = CopyUtils.deepCopy(obj);

            System.out.println("Original Man: " + obj);
            System.out.println("Copied Man After Modification: " + copy);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
