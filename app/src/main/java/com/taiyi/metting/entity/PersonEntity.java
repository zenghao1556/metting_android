package com.taiyi.metting.entity;

import java.io.Serializable;
import java.util.List;

public class PersonEntity implements Serializable{


    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    private boolean isExpand;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private List<String> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }


    public class PersonData implements Serializable {
        private int id;
        private String name;
        private String company;
        private String duties;
        private int state;
        private int sexid;
        private int isleave;

        public String getAttributes() {
            return attributes;
        }

        public void setAttributes(String attributes) {
            this.attributes = attributes;
        }

        private String attributes;
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

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getDuties() {
            return duties;
        }

        public void setDuties(String duties) {
            this.duties = duties;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getSexid() {
            return sexid;
        }

        public void setSexid(int sexid) {
            this.sexid = sexid;
        }

        public int getIsleave() {
            return isleave;
        }

        public void setIsleave(int isleave) {
            this.isleave = isleave;
        }


    }
}
