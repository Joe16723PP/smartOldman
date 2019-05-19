package kku.en.coe.smartoldman;


public class ListItem {
    private String header,desc,imgUrl, color, pointer;

    public String getHeader() {
        return header;
    }

    public String getColor() {
        return color;
    }

    public String getDesc() {
        return desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPointer() {
        return pointer;
    }

    public ListItem(String header, String desc, String imgUrl , String color, String pointer) {
        this.header = header;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.color = color;
        this.pointer = pointer;
    }


}
