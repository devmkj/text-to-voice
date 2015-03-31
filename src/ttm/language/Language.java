package ttm.language;

import java.util.ArrayList;

/**
 * Language data.
 * Created by shibaprasad on 3/19/2015.
 */
@SuppressWarnings("UnusedDeclaration")
public class Language {

    private static ArrayList<String[]> data = null;

    public static ArrayList<String[]> getData() {
        if (data == null) {
            initData();
        }
        return data;
    }

    private static void initData() {
        data = new ArrayList<>();

        data.add(new String[]{"en", "English"});
        data.add(new String[]{"da", "Danish"});
        data.add(new String[]{"ja", "Japanese"});
        data.add(new String[]{"de", "German"});
        data.add(new String[]{"th", "Thai"});
        data.add(new String[]{"hi", "Hindi"});
        data.add(new String[]{"fi", "Finnish"});
        data.add(new String[]{"vi", "Vietnamese"});
        data.add(new String[]{"el", "Greek"});
        data.add(new String[]{"nl", "Dutch"});
        data.add(new String[]{"pl", "Polish"});
        data.add(new String[]{"tl", "Filipino"});
        data.add(new String[]{"in", "Indonesian"});

    }
}
