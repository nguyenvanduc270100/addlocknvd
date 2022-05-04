@file:Suppress("DEPRECATION")

package com.bienlongtuan.applocker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.FileObserver
import android.provider.MediaStore
import android.util.Log
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.helpers.ImageDirectory
import com.bienlongtuan.applocker.helpers.ImageInfo
import java.io.File
import java.net.URLConnection
import java.util.*


class MediaFileBusiness(private var mContext: Context) {
    private lateinit var mAllMediaDirectoryList: ArrayList<ImageDirectory?>
    private lateinit var mVideoDirectoryList: ArrayList<ImageDirectory?>
    private lateinit var mImageDirectoryList: ArrayList<ImageDirectory?>
    private var IMAGE_FOLDER = "/Bitel Images"
    private val storageFolder = mContext.applicationInfo.dataDir.toString() + "/Applock"

    private val fileObserver: FileObserver =
        object : FileObserver(storageFolder + IMAGE_FOLDER) {
            override fun onEvent(event: Int, fileName: String?) {
                if ((event == CREATE || event == DELETE || event == MODIFY)
                    && fileName != ".probe"
                ) {
                    val filePath: String =
                        storageFolder + IMAGE_FOLDER + File.separator.toString() + fileName
                    Log.d(TAG, "File created [$filePath]")
                    // check and update
                    for (mediaDirectory in mAllMediaDirectoryList) {
                        val parentName: String? = mediaDirectory?.parentName
                        if (parentName == IMAGE_FOLDER) {
                            val listImage: ArrayList<ImageInfo> = mediaDirectory.listFile
                            var has = false
                            for (imageInfo in listImage) {
                                if (fileName?.let { imageInfo.imagePath!!.contains(it) } == true) {
                                    has = true
                                    break
                                }
                            }
                            if (!has) { // add to ImageDirect
                                val imageInfo = ImageInfo(filePath)
                                mediaDirectory.addListFile(imageInfo)
                            }
                        }
                    }
                    if (isVideoFile(filePath)) for (videoDirectory in mVideoDirectoryList) {
                        val parentName: String? = videoDirectory?.parentName
                        if (parentName == IMAGE_FOLDER) {
                            val listImage: ArrayList<ImageInfo> = videoDirectory.listFile
                            var has = false
                            for (imageInfo in listImage) {
                                if (fileName?.let { imageInfo.imagePath?.contains(it) } == true) {
                                    has = true
                                    break
                                }
                            }
                            if (!has) { // add to ImageDirect
                                val imageInfo = ImageInfo(filePath)
                                videoDirectory.addListFile(imageInfo)
                            }
                        }
                    }
                    if (isImageFile(filePath)) for (imageDirectory in mImageDirectoryList) {
                        val parentName: String? = imageDirectory?.parentName
                        if (parentName == IMAGE_FOLDER) {
                            val listImage: ArrayList<ImageInfo> = imageDirectory.listFile
                            var has = false
                            for (imageInfo in listImage) {
                                if (fileName?.let { imageInfo.imagePath?.contains(it) } == true) {
                                    has = true
                                    break
                                }
                            }
                            if (!has) { // add to ImageDirect
                                val imageInfo = ImageInfo(filePath)
                                imageDirectory.addListFile(imageInfo)
                            }
                        }
                    }
                }
            }
        }

    @Synchronized
    fun getImageDirectorOfRoot(): ArrayList<ImageDirectory?> {
        if (mImageDirectoryList.isNotEmpty()) return mImageDirectoryList
        val mImageDirectoryList = ArrayList<ImageDirectory?>()
        val allImages = ArrayList<ImageInfo>()
        val hashMap = HashMap<String, ImageDirectory?>()
        var imageCursor: Cursor? = null
        try {
            val columns = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN
            )
            val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"
            imageCursor = mContext.applicationContext.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, orderBy
            )

            val albumAllImage = ImageDirectory()
            albumAllImage.parentName = mContext.getString(R.string.all_media)
            albumAllImage.parentPath = ImageDirectory.PATH_ALL_IMAGE
            if (imageCursor != null && imageCursor.count > 0) {
                // add album All Image
                var dir: ImageDirectory?
                while (imageCursor.moveToNext()) {
                    val dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    //  get Image path
                    val filePath = imageCursor.getString(dataColumnIndex)
                    // get Parent directory path,  name
                    val parentPath = getParentPath(filePath)
                    val parentName = getParentName(filePath)
                    val dateCreated =
                        imageCursor.getLong(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN))
                    if (parentPath != null) {
                        dir = hashMap[parentPath]
                        if (dir == null) {
                            dir = ImageDirectory()
                            dir.parentPath = parentPath
                            dir.parentName = parentName
                            hashMap[parentPath] = dir
                        }
                        val imageInfo = ImageInfo(filePath)
                        imageInfo.dateCreate = dateCreated
                        dir.addListFile(imageInfo)
                        allImages.add(imageInfo)
                    }
                }
                albumAllImage.listFile = allImages
                mImageDirectoryList.addAll(hashMap.values)
                mImageDirectoryList.add(0, albumAllImage)
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception", e)
        } finally {
            imageCursor?.close()
        }
        this.mImageDirectoryList = mImageDirectoryList
        return mImageDirectoryList
    }


    //  get Image path
    @get:Synchronized
    val videoDirectorOfRoot:
            ArrayList<ImageDirectory?>
        @SuppressLint("Range")
        get() {
            if (mVideoDirectoryList.isNotEmpty()) return mVideoDirectoryList
            val mVideoDirectoryList: ArrayList<ImageDirectory?> = ArrayList<ImageDirectory?>()
            var imageCursor: Cursor? = null
            try {
                val columns = arrayOf(
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.RESOLUTION,
                    MediaStore.Images.Media.DATE_TAKEN
                )
                val orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC"
                imageCursor = mContext.applicationContext.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy
                )
            } catch (e: Exception) {
                Log.e(TAG, "Exception", e)
            } finally {
                imageCursor?.close()
            }
            // show newest photo at beginning of the list
            // sap xep  theo thoi gian - lam sau
            // Collections.reverse(directoryList);mVideoDirectoryList
            this.mVideoDirectoryList = mImageDirectoryList
            return mVideoDirectoryList
        }

    fun getImageDirectorOfAssets(pathAssets: String): ArrayList<ImageDirectory> {
        val mBackgroundDirectoryList: ArrayList<ImageDirectory> = ArrayList<ImageDirectory>()
        try {
            val subPath = pathAssets.replace("assets:".toRegex(), "")
            val listPathFile: Array<out String> = mContext.assets.list(subPath)!!
            val imageDirectory = ImageDirectory()
            for (pFile in listPathFile) {
                val imageInfo = ImageInfo(pathAssets + File.separator + pFile)
                imageDirectory.addListFile(imageInfo)
            }
            mBackgroundDirectoryList.add(imageDirectory)
        } catch (ex: Exception) {
            Log.e(TAG, "Exception", ex)
        }
        return mBackgroundDirectoryList
    }

    fun getFirstImageOfAssets(pathAssets: String): ImageInfo? {
        return try {
            val subPath = pathAssets.replace("assets:".toRegex(), "")
            val listPathFile: Array<out String> = mContext.assets.list(subPath)!!
            ImageInfo(pathAssets + File.separator + listPathFile[0])
        } catch (ex: Exception) {
            Log.e(TAG, "Exception", ex)
            null
        }
    }

    val firstImageFromDevice: ImageInfo?
        get() {
            if (mImageDirectoryList.isEmpty() || mImageDirectoryList.size == 0) getImageDirectorOfRoot()
            return mImageDirectoryList[0]?.firstImage
        }

    @get:Synchronized
    val allMediaDirectorOfRoot: ArrayList<ImageDirectory?>
        get() {
            if (mAllMediaDirectoryList.isNotEmpty()) return mAllMediaDirectoryList
            val mAllMediaDirectoryList: ArrayList<ImageDirectory?> = ArrayList<ImageDirectory?>()
            val directoryImageList: ArrayList<ImageDirectory?> = getImageDirectorOfRoot()
            val directoryVideoList: ArrayList<ImageDirectory?> = videoDirectorOfRoot
            for (imageDir in directoryImageList) {
                if (directoryVideoList.contains(imageDir)) {
                    val videoDir: ImageDirectory? =
                        directoryVideoList[directoryVideoList.indexOf(imageDir)]
                    imageDir!!.listFile.addAll(videoDir!!.listFile)
                    directoryVideoList.remove(imageDir)
                    directoryVideoList.remove(videoDir)
                    imageDir.listFile.sort()
                }
                mAllMediaDirectoryList.add(imageDir)
            }
            mAllMediaDirectoryList.addAll(directoryVideoList)
            for (i in mAllMediaDirectoryList.indices) {
                val directory: ImageDirectory? = mAllMediaDirectoryList[i]
                if (directory?.parentName
                        .equals(mContext.getString(R.string.all_media))
                ) {
                    mAllMediaDirectoryList.remove(directory)
                    mAllMediaDirectoryList.add(0, directory)
                    break
                }
            }
            this.mAllMediaDirectoryList = mAllMediaDirectoryList
            return mAllMediaDirectoryList
        }

    private fun getParentPath(filePath: String?): String? {
        if (filePath == null) return "Other"
        val file = File(filePath)
        return if (file.exists()) {
            file.parent
        } else {
            // reget with error file
            //String temp = filePath.substring(0, filePath.lastIndexOf("/"));
            null
        }
    }

    private fun getParentName(filePath: String?): String {
        if (filePath == null) return "Other"
        val file = File(filePath)
        if (file.exists()) {
            return Objects.requireNonNull(file.parentFile).name
        } else {
            var temp = filePath.substring(0, filePath.lastIndexOf("/"))
            if (temp.indexOf("/") > 0) {
                temp = temp.substring(filePath.lastIndexOf("/"))
                return temp
            }
        }
        return "Other"
    }

    companion object {
        private val TAG = MediaFileBusiness::class.java.simpleName
        fun isImageFile(path: String?): Boolean {
            val mimeType = URLConnection.guessContentTypeFromName(path)
            return mimeType != null && mimeType.startsWith("image")
        }

        fun isVideoFile(path: String?): Boolean {
            val mimeType = URLConnection.guessContentTypeFromName(path)
            return mimeType != null && mimeType.startsWith("video")
        }
    }

    init {
        mAllMediaDirectoryList = ArrayList<ImageDirectory?>()
        mVideoDirectoryList = ArrayList<ImageDirectory?>()
        mImageDirectoryList = ArrayList<ImageDirectory?>()
    }
}