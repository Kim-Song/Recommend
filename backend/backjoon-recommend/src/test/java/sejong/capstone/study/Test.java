package sejong.capstone.study;

import java.util.ArrayList;

public class Test {

    @org.junit.jupiter.api.Test
    void 문자열을_배열로_파싱한다() {
        String algo = "\"['조합론', '다이나믹 프로그래밍', '수학']\"";
        ArrayList<String> strings = new ArrayList<>();

        // Remove the outer quotes and brackets
        algo = algo.substring(2, algo.length() - 2);

        // Split the string by ', ' and add each part to the list
        String[] parts = algo.split("', '");
        for (String part : parts) {
            strings.add(part.replace("'", ""));
        }
    }

}
