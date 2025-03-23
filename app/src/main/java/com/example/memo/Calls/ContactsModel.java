package com.example.memo.Calls;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactsModel implements Parcelable {
    private String name;
    private String relation;
    private String num1;
    private String num2;
    private String photo;
    private int id;

    public ContactsModel(String name, String relation, String num1, String num2, String photo, int id) {
        this.name = name;
        this.relation = relation;
        this.num1 = num1;
        this.num2 = num2;
        this.photo = photo;
        this.id = id;
    }

    protected ContactsModel(Parcel in) {
        name = in.readString();
        relation = in.readString();
        num1 = in.readString();
        num2 = in.readString();
        photo = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(relation);
        dest.writeString(num1);
        dest.writeString(num2);
        dest.writeString(photo);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactsModel> CREATOR = new Creator<ContactsModel>() {
        @Override
        public ContactsModel createFromParcel(Parcel in) {
            return new ContactsModel(in);
        }

        @Override
        public ContactsModel[] newArray(int size) {
            return new ContactsModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
