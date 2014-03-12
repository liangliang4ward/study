package cn.hexing.fk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警编码定义
 */
public class RtuAlertCode {

    /** 告警编码 */
    private String code;
    /** 告警参数定义[String] */
    private List<String> args=new ArrayList<String>();	//与未支持告警有所区别
    
    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return Returns the args.
     */
    public List<String> getArgs() {
        return args;
    }
    /**
     * @param args The args to set.
     */
    public void setArgs(List<String> args) {
        if(args!=null){
        	this.args = args;
        }
    }
}
