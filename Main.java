import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.net.*;

public class Main {
    public static void main(String[] args){
        System.out.print("\n検索キーワード>>>");
        Scanner sc = new Scanner(System.in);
        String keyword = sc.nextLine();
        sc.close();
        List<String> list = googleSearch(keyword);
        list.stream().forEach(s -> {System.out.println(s);});
        try {
            ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe","https://www.google.com" + list.get(0));
            pb.start();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    static List<String> googleSearch(String keyword) {
        List<String> list = new ArrayList<>();
        URL u = null;
        BufferedReader br = null;
        try {
            u = new URL(String.format("https://google.com/search?q=%s&ie=utf-8", keyword));
            URLConnection conn = u.openConnection();
            conn.setRequestProperty("User-agent","Mozilla/5.0");  
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s = null ;
            while ((s = br.readLine()) != null) {
                list.add(s);
            } 
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        List<String> results = analyzeResults(list);
        return results;
    }
    static List<String> analyzeResults(List<String> list) {
        StringTokenizer st =null;
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i< list.size();i++){
            st = new StringTokenizer(list.get(i), "<,>");
            while (st.hasMoreTokens()) {
                String ss = st.nextToken();
                if (ss.indexOf("a") == 0) {
                    String aa =ss.substring(ss.indexOf("herf") != -1?ss.indexOf("href"):0);
                    String aaa = aa.substring(aa.indexOf("\"") + 1 !=0?ss.indexOf("\""):0);
                    String aaaa = aaa.substring(0, aaa.lastIndexOf("\"") != -1?aaa.lastIndexOf("\""):aaa.length());
                    if (aaaa.length() != 0) {
                        String result = aaaa.substring(1);
                        if (result.indexOf("/url") == 0) {
                            results.add(result);
                        }
                    }   
                    
                }
            }
        }
        return results;
    }
}
