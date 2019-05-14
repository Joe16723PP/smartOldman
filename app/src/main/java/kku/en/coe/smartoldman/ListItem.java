package kku.en.coe.smartoldman;


public class ListItem {
    private String header,desc,imgUrl;

    public String getHeader() {
        return header;
    }

    public String getDesc() {
        return desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ListItem(String header, String desc, String imgUrl) {
        this.header = header;
        this.desc = desc;
        this.imgUrl = imgUrl;
    }
}
