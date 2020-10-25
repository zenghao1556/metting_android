package com.taiyi.metting.entity;

import java.io.Serializable;
import java.util.List;

public class MeetingListResponse implements Serializable {
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

    public List<MeetingEntity> getData() {
        return data;
    }

    public void setData(List<MeetingEntity> data) {
        this.data = data;
    }

    private List<MeetingEntity> data;

    public class MeetingEntity implements Serializable {
        private int id;
        private String name;
        private int roomid;
        private String meetingtime;
        private String address;
        private String roomname;

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

        public int getRoomid() {
            return roomid;
        }

        public void setRoomid(int roomid) {
            this.roomid = roomid;
        }

        public String getMeetingtime() {
            return meetingtime;
        }

        public void setMeetingtime(String meetingtime) {
            this.meetingtime = meetingtime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRoomname() {
            return roomname;
        }

        public void setRoomname(String roomname) {
            this.roomname = roomname;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        private String memo;
    }
}


