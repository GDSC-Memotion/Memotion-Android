package konkuk.gdsc.memotion.domain.entity.emotion

import konkuk.gdsc.memotion.R

enum class Emotion(val number: Int) {
    ANGER(0),
    DISGUST(1),
    FEAR(2),
    JOY(3),
    NEUTRAL(4),
    SADNESS(5),
    SURPRISE(6);

    fun getResource(): Int {
        return when (this) {
            ANGER -> R.drawable.icon_emoji_anger
            DISGUST -> R.drawable.icon_emoji_disgust
            FEAR -> R.drawable.icon_emoji_fear
            JOY -> R.drawable.icon_emoji_joy
            NEUTRAL -> R.drawable.icon_emoji_neutral
            SADNESS -> R.drawable.icon_emoji_sadness
            SURPRISE -> R.drawable.icon_emoji_surprise
        }
    }

    //Emotion.ANGER.toString().toLowerCase() -> anger 출력 가능
}