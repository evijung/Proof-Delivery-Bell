package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

/**
 * Created by musz on 10/11/2016.
 */

public class MyConstant {
    //Explicit
    private int iconAnInt = R.drawable.doremon48;// Image for icon 48x48
    private String titleHaveSpaceString = "มีช่องว่าง";
    private String messageHaveeSpaceString = "กรุณากรอกข้อมูลให้ครบทุกช่อง ค่ะ!";

    private  String urlUserString  = "http://service.eternity.co.th/TmsPXD/app/CenterService/getUser.php";
    private String[] columLogin = new String[]{"drv_id","drv_name"};
    private String titleUserFalesString = "User False";
    private String messageUserFalesString = "ไม่มี User นี้ในฐานข้อมูลของเรา";
    private String titlePasswordFalse = "Password ผิด";
    private String messagePasswordFalse = "กรุณาลองใหม่ Password ผิด";
    private String urlDataWhereDriverID = "http://service.eternity.co.th/TmsPXD/app/CenterService/getPlan.php";
    private String urlDataWhereDriverIDanDate = "http://service.eternity.co.th/TmsPXD/app/CenterService/getPlanDtl.php";

    public String getUrlDataWhereDriverIDanDate() {
        return urlDataWhereDriverIDanDate;
    }

    public String getUrlDataWhereDriverID() {
        return urlDataWhereDriverID;
    }

    public String getTitlePasswordFalse() {
        return titlePasswordFalse;
    }

    public String getMessagePasswordFalse() {
        return messagePasswordFalse;
    }

    public String getTitleUserFalesString() {
        return titleUserFalesString;
    }

    public String getMessageUserFalesString() {
        return messageUserFalesString;
    }

    public String[] getColumLogin() {
        return columLogin;
    }

    public String getUrlUserString() {
        return urlUserString;
    }

    public int getIconAnInt() {
        return iconAnInt;
    }

    public String getTitleHaveSpaceString() {
        return titleHaveSpaceString;
    }

    public String getMessageHaveeSpaceString() {
        return messageHaveeSpaceString;
    }
}//Main Class