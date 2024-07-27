package com.ianlee.lazy.base.lib.utils


/**
 * Created by Ian on 2024/7/24
 * Email: yixin0212@qq.com
 * Function :
 */
object ObjectToMapUtil {


    fun objectToMap(`object`: Any): MutableMap<String, String> {
        val dataMap: MutableMap<String, String> = HashMap()
        val clazz: Class<*> = `object`.javaClass
        for (field in clazz.declaredFields) {
            try {
                field.isAccessible = true
                dataMap[field.getName()] = field[`object`].toString()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        return dataMap
    }


}