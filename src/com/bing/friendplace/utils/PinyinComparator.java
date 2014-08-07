package com.bing.friendplace.utils;

import java.util.Comparator;

import com.bing.bean.FriendBean;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import android.util.Log;

public class PinyinComparator implements Comparator<FriendBean>{

	  /** 
     * 比较两个字符串 
     */  
    public int compare(FriendBean o1, FriendBean o2) {  
        String name1 =  o1.getUsername();  
        String name2 =  o2.getUsername();  
        String str1 = getPingYin(name1);  
        String str2 = getPingYin(name2);  
        int flag = str1.compareTo(str2);  
        return flag;  
    }  
  
    /** 
     * 将字符串中的中文转化为拼音,其他字符不变 
     *  
     * @param inputString 
     * @return 
     */  
    public String getPingYin(String inputString) {  
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        format.setVCharType(HanyuPinyinVCharType.WITH_V);  
  
        char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组  
        String output = "";  
  
        try {  
            for (int i = 0; i < input.length; i++) {  
                // \\u4E00是unicode编码，判断是不是中文  
                if (java.lang.Character.toString(input[i]).matches(  
                        "[\\u4E00-\\u9FA5]+")) {  
                    // 将汉语拼音的全拼存到temp数组  
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(  
                            input[i], format);  
                    // 取拼音的第一个读音  
                    output += temp[0];  
                }  
                // 大写字母转化成小写字母  
                else if (input[i] > 'A' && input[i] < 'Z') {  
                    output += java.lang.Character.toString(input[i]);  
                    output = output.toLowerCase();  
                }  
                output += java.lang.Character.toString(input[i]);  
            }  
        } catch (Exception e) {  
            Log.e("Exception", e.toString());  
        }  
        return output;  
    }  
	
}
