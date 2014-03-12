package cn.hexing.fk.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组工具类
 */
public class ArraysUtil {

    /**
     * 判断数组中是否包含给定的元素
     * @param elements 数组
     * @param element 给定的元素
     * @return true - 包含
     */
    public static boolean contains(Object[] elements, Object element) {
        return indexOf(elements, element) > -1;
    }
    
    /**
     * 返回给定元素在数组中的位置
     * @param elements 数组
     * @param element 给定的元素
     * @return 从 0 开始的位置。如果数组中没有给定的元素，返回 -1
     */
    public static int indexOf(Object[] elements, Object element) {
        if (elements == null || element == null) {
            return -1;
        }
        
        int index = -1;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) {
                continue;
            }
            
            if (elements[i].equals(element)) {
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    /**
     * 将数组元素转移到列表中，并返回这个列表。列表的大小和元素顺序与数组一致
     * @param elements 数组
     * @return 列表
     */
    public static List<Object> asList(Object[] elements) {
        return asList(elements, 0, elements.length);
    }
    
    /**
     * 将数组元素转移到列表中，并返回这个列表。列表的大小为 length 指定的值，如果
     * offset + length 大于数组的最大下标，则列表的大小为 elements.length - offset
     * @param elements 数组
     * @param offset 偏移量
     * @param length 长度
     * @return 列表
     */
    public static List<Object> asList(Object[] elements, int offset, int length) {
        int size = length;
        if (offset + length >= elements.length) {
            size = elements.length - offset;
        }
        
        List<Object> l = new ArrayList<Object>(size);
        for (int i = 0; i < length; i++) {
            int index = offset + i;
            if (index >= elements.length) {
                break;
            }
            l.add(elements[index]); 
        }
        
        return l;
    }
    
    /**
     * 将数组转为字符串描述
     * @param elements 数组
     * @return 字符串描述。格式为 [e1, e2, e3, ...]
     */
    public static String asString(Object[] elements) {
        if (elements == null) {
            return "null";
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i].toString());
        }
        sb.append("]");
        
        return sb.toString();
    }
}
