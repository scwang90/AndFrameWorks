package com.andcloud.model;

import com.andcloud.AndCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;

import java.util.Date;

/**
 * 用户
 *
 * @author 树朾
 */
public class AvUser extends com.avos.avoscloud.AvUser implements AvModel {

    /**
     * 昵称
     */
    public static final String NickName = "NickName";
    /**
     * 个性化头像
     */
    public static final String Avatar = "Avatar";
    /**
     * 性别
     */
    public static final String Gender = "Gender";
    /**
     * 个性前面
     */
    public static final String Signature = "Signature";
    /**
     * 上一次登陆时间
     */
    public static final String LastLogin = "LastLogin";

    public AvUser() {
    }

    public void setNickName(String nickname) {
        this.put(NickName, nickname);
    }

    public String getNickName() {
        return this.getString(NickName);
    }

    public AVFile getAvatar() {
        return getAVFile(Avatar);
    }

    public void setAvatar(AVFile avatar) {
        put(Avatar, avatar);
    }

    public boolean getGender() {
        return getBoolean(Gender);
    }

    public void setGender(boolean gender) {
        put(Gender, gender);
    }

    public String getSignature() {
        return getString(Signature);
    }

    public void setSignature(String signature) {
        put(Signature, signature);
    }

    public Date getLastLogin() {
        return getDate(LastLogin);
    }

    public void setLastLogin(Date lastLogin) {
        put(LastLogin, lastLogin);
    }

    @Override
    public void saveInBackground() {
        if (AndCloud.mDebug) {
            super.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            super.saveInBackground();
        }
    }
}
