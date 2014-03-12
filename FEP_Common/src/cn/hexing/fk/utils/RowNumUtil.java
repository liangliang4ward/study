package cn.hexing.fk.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 行号计算工具
 */
public class RowNumUtil {

    /**
     * 计算开始和结束行号
     * @param pageNum 页号
     * @param pageSize 页大小。小于 0 表示返回所有记录
     * @return 包含开始和结束行号的 Map，"startRowNum" - 开始记录号（不包含），"endRowNum" - 结束记录号（包含）
     */
    public static Map<String,Integer> calcRowNum(int pageNum, int pageSize) {
        // 开始记录号（不包含）
        int startRowNum = (pageNum - 1) * pageSize;
        // 结束记录号（包含）
        int endRowNum = startRowNum + pageSize;
        if (pageSize < 0) {
            startRowNum = 0;
            endRowNum = Integer.MAX_VALUE;
        }
        
        Map<String,Integer> params = new HashMap<String,Integer>();
        params.put("startRowNum", new Integer(startRowNum));
        params.put("endRowNum", new Integer(endRowNum));
        
        return params;
    }
}
