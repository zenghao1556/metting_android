package com.taiyi.metting.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences 工具类
 */
public class SPUtils {

    public static final String PROJECT = "metting";


    /**
     * 设置字符
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setStringPreferences(Application context, String preference, String key, String value) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    /**
     * 设置字符
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setStringPreferences(Context context, String preference, String key, String value) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    /**
     * 获取字符
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringPreference(Context context, String preference, String key, String defaultValue) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, defaultValue);
        }
    }

    /**
     * 获取字符
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringPreference(Application context, String preference, String key, String defaultValue) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, defaultValue);
        }
    }

    /**
     * 设置长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setLongPreference(Context context, String preference, String key, long value) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    /**
     * 获取长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLongPreference(Context context, String preference, String key, long defaultValue) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getLong(key, defaultValue);
        }
    }

    /**
     * 设置boolean类型
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setBooleanPreferences(Context context, String preference, String key, boolean value) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    /**
     * 获取长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanPreference(Context context, String preference, String key, boolean defaultValue) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getBoolean(key, defaultValue);
        }
    }

    /**
     * 设置int
     *
     * @param preference
     * @param key
     * @param value
     */
    public static void setIntPreferences(Application context, String preference, String key, int value) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    /**
     * 获取int
     *
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntPreference(Application context, String preference, String key, int defaultValue) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getInt(key, defaultValue);
        }
    }

    /**
     * 设置int
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setIntPreferences(Context context, String preference, String key, int value) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    /**
     * 获取int
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntPreference(Context context, String preference, String key, int defaultValue) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getInt(key, defaultValue);
        }
    }

    /**
     * 删除一个属性
     *
     * @param context
     * @param preference
     * @param key
     */
    public static void deletePrefereceKey(Context context, String preference, String key) {
        synchronized (key) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.commit();
        }
    }

}
