package com.taiyi.metting.entity;
import java.io.Serializable;
import java.util.List;

public class TableCardReponse implements Serializable {

    private int code;
    private int count;
    private boolean result;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<TableCardInfo> getData() {
        return data;
    }

    public void setData(List<TableCardInfo> data) {
        this.data = data;
    }

    private List<TableCardInfo> data;

    public class TableCardInfo implements Serializable{
        private int id;
        private String name;
        private String modifytime;
        private int stauts;
        private String templatefilename;
        private int seatrule;
        private int meetingstate;
        private int seatnum;
        private int isshared;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModifytime() {
            return modifytime;
        }

        public void setModifytime(String modifytime) {
            this.modifytime = modifytime;
        }

        public int getStauts() {
            return stauts;
        }

        public void setStauts(int stauts) {
            this.stauts = stauts;
        }

        public String getTemplatefilename() {
            return templatefilename;
        }

        public void setTemplatefilename(String templatefilename) {
            this.templatefilename = templatefilename;
        }

        public int getSeatrule() {
            return seatrule;
        }

        public void setSeatrule(int seatrule) {
            this.seatrule = seatrule;
        }

        public int getMeetingstate() {
            return meetingstate;
        }

        public void setMeetingstate(int meetingstate) {
            this.meetingstate = meetingstate;
        }

        public int getSeatnum() {
            return seatnum;
        }

        public void setSeatnum(int seatnum) {
            this.seatnum = seatnum;
        }

        public int getIsshared() {
            return isshared;
        }

        public void setIsshared(int isshared) {
            this.isshared = isshared;
        }

        public int getPresentmeeting() {
            return presentmeeting;
        }

        public void setPresentmeeting(int presentmeeting) {
            this.presentmeeting = presentmeeting;
        }

        private int presentmeeting;
    }


}
