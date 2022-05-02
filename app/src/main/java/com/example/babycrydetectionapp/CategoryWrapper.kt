package com.example.babycrydetectionapp

import android.os.Parcel
import android.os.Parcelable
import org.tensorflow.lite.support.label.Category

class CategoryWrapper : Parcelable {

    private var index = 0
    private var label: String? = null
    private var displayName: String? = null
    private var score = 0f

    fun wrappedCategory(): Category = Category.create(label, displayName, score, index)

    constructor(data: Category) {
        index = data.index
        label = data.label
        displayName = data.displayName
        score = data.score
    }

    constructor(parcel: Parcel) {
        index = parcel.readInt()
        label = parcel.readString()
        displayName = parcel.readString()
        score = parcel.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(index)
        parcel.writeString(label)
        parcel.writeString(displayName)
        parcel.writeFloat(score)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<CategoryWrapper> {
        override fun createFromParcel(parcel: Parcel): CategoryWrapper {
            return CategoryWrapper(parcel)
        }

        override fun newArray(size: Int): Array<CategoryWrapper?> {
            return arrayOfNulls(size)
        }
    }
}