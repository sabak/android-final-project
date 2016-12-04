package com.saba.bogchat.model;

import java.util.Date;

/**
 * Basic model representing single immutable message
 */
public class Message {
    private String mMemberId;
    private String mMsg;
    private Date mDateSent;
    private boolean mIsFromMember;
    private boolean mIncoming;

    /**
     * @param memberId     Member id from conversation
     * @param msg          Message content represented as text
     * @param dateSent     Date the message was sent
     * @param isFromMember represents if conversation member is sender or receiver
     * @param incoming     represents if message is unread or not
     */
    public Message(String memberId, String msg, Date dateSent,
                   boolean isFromMember, boolean incoming) {
        mMemberId = memberId;
        mMsg = msg;
        mDateSent = dateSent;
        mIsFromMember = isFromMember;
        mIncoming = incoming;
    }

    /**
     * @return Member id from conversation
     */
    public String getMemberId() {
        return mMemberId;
    }

    /**
     * @return Message content represented as text
     */
    public String getMessage() {
        return mMsg;
    }

    /**
     * @return Date the message was sent
     */
    public Date getMessageDate() {
        return (Date) mDateSent.clone();
    }

    /**
     * @return true if message is sent from conversation member, false otherwise
     */
    public boolean isFromMember() {
        return mIsFromMember;
    }

    /**
     * @return true if message is unread, false otherwise
     */
    public boolean isIncoming() {
        return mIncoming;
    }

}
