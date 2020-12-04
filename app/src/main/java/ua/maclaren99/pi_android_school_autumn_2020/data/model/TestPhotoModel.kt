import android.os.Parcel
import android.os.Parcelable

class TestPhotoModel : Parcelable {
    var url: String?
    var title: String?

    constructor(url: String?, title: String?) {
        this.url = url
        this.title = title
    }

    protected constructor(`in`: Parcel) {
        url = `in`.readString()
        title = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(url)
        parcel.writeString(title)
    }

    companion object {
        val CREATOR: Parcelable.Creator<TestPhotoModel?> = object : Parcelable.Creator<TestPhotoModel?> {
            override fun createFromParcel(`in`: Parcel): TestPhotoModel? {
                return TestPhotoModel(`in`)
            }

            override fun newArray(size: Int): Array<TestPhotoModel?> {
                return arrayOfNulls(size)
            }
        }
        val testPhotoModels: Array<TestPhotoModel>
            get() = arrayOf(
                TestPhotoModel("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
                TestPhotoModel("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
                TestPhotoModel("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
                TestPhotoModel("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
                TestPhotoModel("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
                TestPhotoModel("http://i.imgur.com/3wQcZeY.jpg", "Satellite")
            )
    }
}