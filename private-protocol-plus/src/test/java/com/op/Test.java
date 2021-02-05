package com.op;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/511:18
 */
public class Test {

    public static void main(String[] args) {
        int[] arr = new int[]{2,3,5};
        //第七次抽取数字的结果
        List<String> extract = extract(7, arr);
        //结果大小输出为2187，用数学公式可知道3^7=2187，说明结果数量无误
        System.out.println("list size : " + extract.size());
        //创建一个放置乘积结果的容器，这个容器默认只存放不重复的值
        Set<Integer> set = new HashSet<Integer>();
        for (String s : extract){
            //刚刚拼接的类似于 2*2*2*2*2*2*2 这样的
            String[] split = s.split("\\*");
            int a = 1;
            //计算乘积
            for (String p : split){
                a *= Integer.parseInt(p);
            }
            //将乘积放入容器中
            set.add(a);
        }
        System.out.println("set size : " + set.size());
        System.out.println(set);
    }

    /**
     * 使用递归的方式：
     *     第一次抽取数字，index为1，结果为{2，3，5}
     *     第二次收取数字，此时已知第一次结果，将数组中的2,3,5分别组合第一次的结果
     *     第三次抽取数字，此时已知第二次结果，同理将数组中的2,3,5分别组合第二次的结果
     *     同理可以得到最终第几次抽取时的结果
     * index是第几次抽取数字
     * arr是每次可抽取的数字: 2，3，5
     * list是存放结果的容器
     */
    public static List<String> extract(int index ,int[] arr){
        List<String> list = new ArrayList<String>();
        if (index == 1){
            for (int i = 0 ; i < arr.length ; i ++){
                list.add(arr[i]+"");
            }
        }else {
            for (int i = 0 ; i < arr.length ; i ++){
                List<String> sublist = extract(index -1, arr);
                for (String s : sublist){
                    list.add(arr[i] + "*" + s);
                }
            }
        }
        return list;
    }
}
