package com.bienlongtuan.applocker.helpers


class ImageDirectory {
    var parentName: String? = null
    var parentPath: String? = null
    var countSelectedImage = 0
        set(countSelectedImage) {
            field = if (countSelectedImage < 0) {
                0
            } else countSelectedImage
        }
    var isCurrentSelect = false
    var listFile = ArrayList<ImageInfo>()

    constructor() {}
    constructor(imageInfo: ImageInfo) {
        listFile.add(imageInfo)
    }

    constructor(listFile: ArrayList<ImageInfo>) {
        this.listFile = listFile
    }

    val firstImage: ImageInfo?
        get() = if (listFile.isNotEmpty()) {
            listFile[0]
        } else null

    fun addListFile(imageInfo: ImageInfo) {
        listFile.add(imageInfo)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ImageDirectory
        return (parentName == that.parentName)
    }

    companion object {
        //
        const val PATH_ALL_IMAGE = "all_image"
    }
}