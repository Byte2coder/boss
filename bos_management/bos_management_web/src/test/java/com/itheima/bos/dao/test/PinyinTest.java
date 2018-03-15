package com.itheima.bos.dao.test;

import com.itheima.utils.PinYin4jUtils;

/**
 * ClassName:PinyinTest <br/>
 * Function: <br/>
 * Date: 2018年3月15日 下午5:44:35 <br/>
 */
public class PinyinTest {
    public static void main(String[] args) {

        String pro="广东省";
        String city="深圳市";
        String dist="宝安区";
        
         pro = pro.substring(0, pro.length()-1);
         city=city.substring(0, city.length()-1);
         dist=dist.substring(0, dist.length()-1);
         
        String pString = PinYin4jUtils.hanziToPinyin(pro);
      /* String hanziToPinyin = PinYin4jUtils.hanziToPinyin(dist, "").toUpperCase();
       System.out.println(hanziToPinyin);*/
        String[] headByString = PinYin4jUtils.getHeadByString(pro+city+dist);
        String stringArrayToString = PinYin4jUtils.stringArrayToString(headByString);
        System.out.println(stringArrayToString);
    }
}
