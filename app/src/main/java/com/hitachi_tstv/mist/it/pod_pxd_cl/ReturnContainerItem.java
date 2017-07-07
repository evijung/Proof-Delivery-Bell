package com.hitachi_tstv.mist.it.pod_pxd_cl;

/**
 * Created by tunyaporn on 6/16/2017.
 */

public class ReturnContainerItem {
    private String idString, imageString, contNameString;
    private int returnQtyAnInt;

    public ReturnContainerItem() {
        idString = "";
        imageString = "";
        contNameString = "";
        returnQtyAnInt = 0;
    }

    public ReturnContainerItem(String idString, String imageString, String contNameString, int returnQtyAnInt) {
        this.idString = idString;
        this.imageString = imageString;
        this.contNameString = contNameString;
        this.returnQtyAnInt = returnQtyAnInt;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getContNameString() {
        return contNameString;
    }

    public void setContNameString(String contNameString) {
        this.contNameString = contNameString;
    }

    public String getReturnQtyAnInt() {
        return String.valueOf(returnQtyAnInt);
    }

    public void setReturnQtyAnInt(int returnQtyAnInt) {
        this.returnQtyAnInt = returnQtyAnInt;
    }
}
