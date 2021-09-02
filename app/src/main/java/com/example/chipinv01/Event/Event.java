package com.example.chipinv01.Event;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Event {
    String CreditorName, CreditTime, Deadline, CreditName, Fullamount, MemberAmount;
    int UserAvatar;
    private String UniqueId;

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    ArrayList<Member>Members;

    public ArrayList<Member> getMembers() {
        return Members;
    }

    public void setMembers(ArrayList<Member> members) {
        Members = members;
    }

    public String getCreditorName() {
        return CreditorName;
    }

    public void setCreditorName(String creditorName) {
        CreditorName = creditorName;
    }

    public String getCreditTime() {
        return CreditTime;
    }

    public void setCreditTime(String creditTime) {
        CreditTime = creditTime;
    }

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }

    public String getCreditName() {
        return CreditName;
    }

    public void setCreditName(String creditName) {
        CreditName = creditName;
    }

    public String getFullamount() {
        return Fullamount;
    }

    public void setFullamount(String fullamount) {
        Fullamount = fullamount;
    }

    public String getMemberAmount() {
        return MemberAmount;
    }

    public void setMemberAmount(String memberAmount) {
        MemberAmount = memberAmount;
    }

    public int getUserAvatar() {
        return UserAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        UserAvatar = userAvatar;
    }
}
