import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * @program: BYSJ
 * @description: 测试
 * @author: CN_ssh
 * @create: 2019-04-07 10:35
 **/

public class test {
//    public int solution(String source, String target) {
//        if (source.equals("") || target.equals("")){
//            return -1;
//        }
//
//        Map<Integer, String> map = new HashMap<Integer, String>();
//        // 每截取一段，判断是否在source中存在
//        for (int i = 0; i < source.length(); i++) {
//            for (int j = i; j < source.length() - 1; j++) {
//                // 所有子串存入map中
//                map.put(i + j, source.substring(i, i + j));
//            }
//        }
//        int start = 0;
//        int count = 0;
//        for (int i = 1; i < target.length(); i++) {
//            int flag = 0;
//            Set<Integer> keys = map.keySet();
//            for (Integer key : keys) {
//                if (map.get(key).equals(target.substring(start, i))) {
//                    // 说明此段子串匹配
//                    flag = 1;
//                }
//            }
//            // 说明此段子串不匹配了,向后滑动，继续判断后方字符串
//            if (flag == 0){
//                if (i-1 == start)
//                    return -1;
//                start = i;
//                count = count + 1;
//            }
//        }
//        return count;
//    }



}
