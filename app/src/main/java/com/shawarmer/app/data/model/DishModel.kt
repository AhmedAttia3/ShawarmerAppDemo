package com.shawarmer.app.data.model


import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import javax.annotation.Nullable

@Entity(tableName = "cart")
data class DishModel(
    @PrimaryKey
    @Nullable
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "filename")
    val filename: String,
    @field:Json(name = "price")
    val price: Double,
    @field:Json(name = "rating")
    val rating: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val onAddedTimestamp: String,
    var quantity: Int = 0
) : Parcelable {


    fun getImageUrl() = filename.replace(
        "https://github.com/wedeploy-examples/supermarket-web-example/blob",
        "https://raw.githubusercontent.com/wedeploy-examples/supermarket-web-example/"
    )
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DishModel

        if (title != other.title) return false
        if (filename != other.filename) return false
        if (price != other.price) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + filename.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + rating
        result = 31 * result + title.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + quantity
        return result
    }

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readLong().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeString(description)
        parcel.writeString(filename)
        parcel.writeDouble(price)
        parcel.writeInt(rating)
        parcel.writeString(onAddedTimestamp)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DishModel> {
        override fun createFromParcel(parcel: Parcel): DishModel {
            return DishModel(parcel)
        }

        override fun newArray(size: Int): Array<DishModel?> {
            return arrayOfNulls(size)
        }
    }
}