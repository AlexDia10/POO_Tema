package org.example;

public class Rating {
    private String username;
    private int grade;
    private String comment;

    public Rating(String username, int grade, String comments) {
        this.username = username;
        this.grade = grade;
        this.comment = comments;
    }

    public String getUsername() {
        return username;
    }

    public int getGrade() {
        return grade;
    }

    public String getComment() {
        return comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void displayInfo() {
        System.out.println("grade: " + grade);
        System.out.println("comment: " + comment);
    }
}
