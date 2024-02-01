package konkuk.gdsc.memotion.data

import konkuk.gdsc.memotion.R

enum class Emotion(val number: Int) {
    ANGER(0),
    DISGUST(1),
    FEAT(2),
    JOY(3),
    NEUTRAL(4),
    SADNESS(5),
    SURPRISE(6);

    fun getResource(): Int {
        return when(this) {
            ANGER -> R.drawable.icon_anger
            DISGUST -> TODO()
            FEAT -> TODO()
            JOY -> TODO()
            NEUTRAL -> TODO()
            SADNESS -> TODO()
            SURPRISE -> TODO()
        }
    }

    //Emotion.ANGER.toString().toLowerCase() -> anger 출력 가능
}