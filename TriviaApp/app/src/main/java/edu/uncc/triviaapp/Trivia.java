package edu.uncc.triviaapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NANDU on 07-02-2017.
 */

public class Trivia implements Parcelable {
    int Id;
    String question,imagePath;
    ArrayList<String> choice;
    int answer,userAnswer;
    boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer = userAnswer;
    }

    public Trivia() {
        choice= new ArrayList<>();
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public ArrayList<String> getChoice() {
        return choice;
    }

    public void setChoice(ArrayList<String> choice) {
        this.choice = choice;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(Id);
        dest.writeString(question);
        dest.writeString(imagePath);
        dest.writeSerializable(choice);
        dest.writeInt(answer);
        dest.writeInt(userAnswer);
        dest.writeByte((byte) (result ? 1 : 0));

    }
    public static final Parcelable.Creator<Trivia> CREATOR
            = new Parcelable.Creator<Trivia>() {
        public Trivia createFromParcel(Parcel in) {
            return new Trivia(in);
        }

        public Trivia[] newArray(int size) {
            return new Trivia[size];
        }
    };

    private Trivia(Parcel in) {
        Id = in.readInt();
        question=in.readString();
        imagePath=in.readString();
        choice=(ArrayList<String>)in.readSerializable();
        answer=in.readInt();
        userAnswer=in.readInt();
        result = in.readByte() != 0;
    }
}
