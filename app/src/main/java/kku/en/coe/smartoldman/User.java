package kku.en.coe.smartoldman;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    public String name;
    public String age;
    public String gender;
    public String weight;
    public String height_str;
    public String bmi;
    public String pre_Hyper, pre_Oste, pre_Lipid, pre_Diab, pre_Dep;
    public String post_Hyper, post_Oste, post_Lipid, post_Diab, post_Dep;

    public User(String name, String age, String gender, String weight, String height_str, String bmi, String pre_Hyper, String pre_Oste, String pre_Lipid, String pre_Diab, String pre_Dep, String post_Hyper, String post_Oste, String post_Lipid, String post_Diab, String post_Dep) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height_str = height_str;
        this.bmi = bmi;

        this.pre_Hyper = pre_Hyper;
        this.pre_Oste = pre_Oste;
        this.pre_Lipid = pre_Lipid;
        this.pre_Diab = pre_Diab;
        this.pre_Dep = pre_Dep;

        this.post_Hyper = post_Hyper;
        this.post_Oste = post_Oste;
        this.post_Lipid = post_Lipid;
        this.post_Diab = post_Diab;
        this.post_Dep = post_Dep;
    }

    public User() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("age", age);
        result.put("gender", gender);
        result.put("weight", weight);
        result.put("height", height_str);
        result.put("bmi", bmi);

        result.put("pre_Hyper", pre_Hyper);
        result.put("pre_Oste", pre_Oste);
        result.put("pre_Lipid", pre_Lipid);
        result.put("pre_Diab", pre_Diab);
        result.put("pre_Dep", pre_Dep);

        result.put("post_Hyper", post_Hyper);
        result.put("post_Oste", post_Oste);
        result.put("post_Lipid", post_Lipid);
        result.put("post_Diab", post_Diab);
        result.put("post_Dep", post_Dep);

        return result;

    }
}
