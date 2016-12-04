package com.saba.bogchat.model;

/**
 * Basic model representing single immutable contact
 */
public class Contact {
    private String mUserId;
    private String mDisplayName;
    private String mPhoneNumber;
    private String mAvatarPath;
    private boolean mIsOnline;

    /**
     * @param userId      Contact id username
     * @param displayName Chosen display name for contact
     * @param phoneNumber Phone number associated with contact
     * @param avatarPath  Avatar filename/path associated with contact
     */
    public Contact(String userId, String displayName, String phoneNumber,
                   String avatarPath) {
        mUserId = userId;
        mDisplayName = displayName;
        mPhoneNumber = phoneNumber;
        mAvatarPath = avatarPath;
    }

    /**
     * @return Contact username
     */
    public String getUserId() {
        return mUserId;
    }

    /**
     * @return Contact's display name
     */
    public String getDisplayName() {
        return mDisplayName;
    }

    /**
     * @return Contact's phone number
     */
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    /**
     * @return Contact's avatar filename/path
     */
    public String getAvatarPath() {
        return mAvatarPath;
    }

    /**
     * @param isOnline online status
     */
    public void setOnlineStatus(boolean isOnline) {
        mIsOnline = isOnline;
    }

    /**
     * @return true if user is online, false otherwise
     */
    public boolean isOnline() {
        return mIsOnline;
    }

}
