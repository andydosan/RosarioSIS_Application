package com.example.rosariosisapplication;

public class CardHolder {
    private String teacher2, percentage2, classname2, lettergrade2;

    public CardHolder(String classname, String teachername, String lettergrade, String percentgrade) {
        this.classname2 = classname;
        this.teacher2 = teachername;
        this.lettergrade2 = lettergrade;
        this.percentage2 = percentgrade;
    }

    public String getTeacher2() {
        return teacher2;
    }

    public String getPercentage2() {
        return percentage2;
    }

    public String getClassname2() {
        return classname2;
    }

    public String getLettergrade2() {
        return lettergrade2;
    }
}
