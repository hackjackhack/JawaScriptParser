package test.org.jackace.jawascriptparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Thanks to https://gist.github.com/isa/878708
 */
class MultiLineStringLiteral {

    public static String S() {
        StackTraceElement element = new RuntimeException().getStackTrace()[1];
        String name = element.getClassName().replace('.', '\\') + ".java";
        String baseDir = System.getProperty("user.dir") + "\\src\\";
        InputStream in = null;
        try {
            in = new FileInputStream(baseDir + name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = convertStreamToString(in, element.getLineNumber());

        return s.substring(s.indexOf("/*") + 2, s.indexOf("*/"));
    }

    private static String convertStreamToString(InputStream is, int lineNum) {
        /*
        * To convert the InputStream to String we use the
        * BufferedReader.readLine() method. We iterate until the BufferedReader
        * return null which means there's no more data to read. Each line will
        * appended to a StringBuilder and returned as String.
        */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        int i = 1;
        try {
            while ((line = reader.readLine()) != null) {
                if (i++ >= lineNum) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
