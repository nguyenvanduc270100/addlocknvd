package com.lgi.applock.helpers

import java.io.Serializable


class ImageInfo : Serializable, Comparable<ImageInfo?> {
    var imagePath: String? = null
    var isSelected = false
    var fileSize = 0L
    var durationInSecond = 0
    var duration: Long = 0
    var videoContentURI: String? = null
    var dateCreate = 0L
    var isVideo = false
    var indexSelect = 0
    var resolution: String? = null

    constructor() {}
    constructor(imagePath: String?) {
        this.imagePath = imagePath
    }

    constructor(imagePath: String?, videoContentURI: String?) {
        this.imagePath = imagePath
        this.videoContentURI = videoContentURI
    }

    constructor(imagePath: String?, dateCreate: Long) {
        this.imagePath = imagePath
        this.dateCreate = dateCreate
    }

    constructor(
        imagePath: String?,
        fileSize: Long,
        durationInSecond: Int,
        videoContentURI: String?,
        dateCreate: Long
    ) {
        this.imagePath = imagePath
        this.fileSize = fileSize
        this.durationInSecond = durationInSecond
        this.videoContentURI = videoContentURI
        this.dateCreate = dateCreate
        isVideo = true
    }

    constructor(
        imagePath: String?,
        fileSize: Long,
        durationInSecond: Int,
        videoContentURI: String?,
        dateCreate: Long,
        resolution: String?
    ) {
        this.imagePath = imagePath
        this.fileSize = fileSize
        this.durationInSecond = durationInSecond
        this.videoContentURI = videoContentURI
        this.dateCreate = dateCreate
        this.resolution = resolution
        isVideo = true
    }

    override fun toString(): String {
        return "ImageInfo{" +
                "imagePath='" + imagePath + '\'' +
                ", selected=" + isSelected +
                ", fileSize=" + fileSize +
                ", durationInSecond=" + durationInSecond +
                ", videoContentURI='" + videoContentURI + '\'' +
                ", dateCreate=" + dateCreate +
                ", isVideo=" + isVideo +
                '}'
    }

//    override fun compareTo(other: ImageInfo): Int {
//        return java.lang.Long.compare(other.dateCreate, dateCreate)
//    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val imageInfo = other as ImageInfo
        return imagePath == imageInfo.imagePath
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun compareTo(other: ImageInfo?): Int {
        return other!!.dateCreate.compareTo(dateCreate)
    }
}