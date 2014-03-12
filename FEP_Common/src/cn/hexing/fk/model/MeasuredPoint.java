package cn.hexing.fk.model;

/**
 * 测量点
 */
public class MeasuredPoint {
    /** CT/PT 的缺省值 */
    private static final String DEFAULT_CT_PT = "1/1";
    
    /** 终端局号ID */
    private String rtuId;
    /** 户号 */
    private String customerNo;
    /** 测量点号 */
    private String tn;
    /** 测量点局号 */
    private String stationNo;
    /** CT */
    private String ct = DEFAULT_CT_PT;
    /** PT */
    private String pt = DEFAULT_CT_PT;
    /** 数据保存ID */
    private String dataSaveID;
    /** 测量点地址，允许为空 */
    private String tnAddr;
    /**从数据库高权限密码取得*/
    private String hiPassword;

    /**
     * 以字符串格式设置 CT 值
     * @param ctStr CT 的字符串形式
     */
    public void setCtStr(String ctStr) {
        if (ctStr == null) {
            ct = DEFAULT_CT_PT;
        }
        else {
            try {
                ct = ctStr;
            }
            catch (Exception ex) {
                ct = DEFAULT_CT_PT;
            }
        }
    }
    
    /**
     * 以字符串格式设置 PT 值
     * @param ptStr PT 的字符串形式
     */
    public void setPtStr(String ptStr) {
        if (ptStr == null) {
            pt = DEFAULT_CT_PT;
        }
        else {
            try {
                pt = ptStr;
            }
            catch (Exception ex) {
                pt = DEFAULT_CT_PT;
            }
        }
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[rtuId=").append(rtuId)
            .append(", tn=").append(tn)
            .append(", stationNo=").append(stationNo).append("]");
        return sb.toString();
    }
    
    /**
     * @return Returns the rtuId.
     */
    public String getRtuId() {
        return rtuId;
    }
    /**
     * @param rtuId The rtuId to set.
     */
    public void setRtuId(String rtuId) {
        this.rtuId = rtuId;
    }
    /**
     * @return Returns the tn.
     */
    public String getTn() {
        return tn;
    }
    /**
     * @param tn The tn to set.
     */
    public void setTn(String tn) {
        this.tn = tn;
    }    

	/**
	 * @return 返回 customerNo。
	 */
	public String getCustomerNo() {
		return customerNo;
	}

	/**
	 * @param customerNo 要设置的 customerNo。
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	/**
	 * @return 返回 dataSaveID。
	 */
	public String getDataSaveID() {
		return dataSaveID;
	}

	/**
	 * @param dataSaveID 要设置的 dataSaveID。
	 */
	public void setDataSaveID(String dataSaveID) {
		this.dataSaveID = dataSaveID;
	}


	/**
     * @return Returns the stationNo.
     */
    public String getStationNo() {
        return stationNo;
    }
    /**
     * @param stationNo The stationNo to set.
     */
    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }
    /**
     * @return Returns the ct.
     */
    public String getCt() {
        return ct;
    }
    /**
     * @param ct The ct to set.
     */
    public void setCt(String ct) {
        this.ct = ct;
    }
    /**
     * @return Returns the pt.
     */
    public String getPt() {
        return pt;
    }
    /**
     * @param pt The pt to set.
     */
    public void setPt(String pt) {
        this.pt = pt;
    }

	public String getTnAddr() {
		return tnAddr;
	}

	public void setTnAddr(String tnAddr) {
		this.tnAddr = tnAddr;
	}

	public String getHiPassword() {
		return hiPassword;
	}

	public void setHiPassword(String hiPassword) {
		this.hiPassword = hiPassword;
	}
   
}
