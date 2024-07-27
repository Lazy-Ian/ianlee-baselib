package com.ianlee.lazy.base.lib.view.appupdate.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Created by Ian on 2024/3/19
 * Email: yixin0212@qq.com
 * Function : 文件管理类工具
 */
object FileUtil {
    /**
     * 创建保存的文件夹
     */
    fun createDirDirectory(downloadPath: String?) {
        val dirDirectory = File(downloadPath)
        if (!dirDirectory.exists()) {
            dirDirectory.mkdirs()
        }
    }

    /**
     * 创建一个随机读写
     */
    fun createRAFile(downloadPath: String?, fileName: String?): RandomAccessFile? {
        //断点读写
        try {
            return RandomAccessFile(createFile(downloadPath, fileName), "rwd")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 创建一个文件
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return 文件
     */
    fun createFile(downloadPath: String?, fileName: String?): File {
        return File(downloadPath, fileName)
    }

    /**
     * 查看一个文件是否存在
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return true | false
     */
    fun fileExists(downloadPath: String?, fileName: String?): Boolean {
        return File(downloadPath, fileName).exists()
    }

    /**
     * 删除一个文件
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return true | false
     */
    fun delete(downloadPath: String?, fileName: String?): Boolean {
        return File(downloadPath, fileName).delete()
    }

    /**
     * 获取一个文件的MD5
     *
     * @param file 文件
     * @return MD5
     */
    fun getFileMD5(file: File?): String {
        return try {
            val buffer = ByteArray(1024)
            var len: Int
            val digest = MessageDigest.getInstance("MD5")
            val `in` = FileInputStream(file)
            while (`in`.read(buffer).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            `in`.close()
            val bigInt = BigInteger(1, digest.digest())
            bigInt.toString(16).toUpperCase()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
