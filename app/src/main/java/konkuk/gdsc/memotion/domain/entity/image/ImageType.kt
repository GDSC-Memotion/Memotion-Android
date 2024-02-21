package konkuk.gdsc.memotion.domain.entity.image

data class Image(
    val url: String,
    val type: ImageType
)

enum class ImageType {
    EXISTING,
    NEW
}
