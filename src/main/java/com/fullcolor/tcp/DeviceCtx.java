package com.fullcolor.tcp;

/**
 * 留这备份使用 记录设备session 信心
 *
 * @author wang xiao
 * date 2023/9/3
 */
public class DeviceCtx {

    private String pid;

    private String barcode;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
