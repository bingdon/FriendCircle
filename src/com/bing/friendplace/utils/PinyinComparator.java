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
     * �Ƚ������ַ��� 
     */  
    public int compare(FriendBean o1, FriendBean o2) {  
        String name1 =  o1.getNickname();  
        String name2 =  o2.getNickname();  
        String str1 = getPingYin(name1);  
        String str2 = getPingYin(name2);  
        int flag = str1.compareTo(str2);  
        return flag;  
    }  
  
    /** 
     * ���ַ����е�����ת��Ϊƴ��,�����ַ����� 
     *  
     * @param inputString 
     * @return 
     */  
    public String getPingYin(String inputString) {  
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        format.setVCharType(HanyuPinyinVCharType.WITH_V);  
  
        char[] input = inputString.trim().toCharArray();// ���ַ���ת�����ַ�����  
        String output = "";  
  
        try {  
            for (int i = 0; i < input.length; i++) {  
                // \\u4E00��unicode���룬�ж��ǲ�������  
                if (java.lang.Character.toString(input[i]).matches(  
                        "[\\u4E00-\\u9FA5]+")) {  
                    // ������ƴ����ȫƴ�浽temp����  
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(  
                            input[i], format);  
                    // ȡƴ���ĵ�һ������  
                    output += temp[0];  
                }  
                // ��д��ĸת����Сд��ĸ  
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
