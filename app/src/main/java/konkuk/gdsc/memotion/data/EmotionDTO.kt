package konkuk.gdsc.memotion.data

data class EmotionResult(
    val emotion: Emotion,
    val percentage: Float,
    val isTitle: Boolean,
){
    companion object {
        val sample: List<EmotionResult> = listOf(
            EmotionResult(Emotion.ANGER, 20.00f, true),
            EmotionResult(Emotion.ANGER, 20.00f, false),
            EmotionResult(Emotion.ANGER, 20.00f, false),
            EmotionResult(Emotion.ANGER, 20.00f, false),
            EmotionResult(Emotion.ANGER, 20.00f, false),
            EmotionResult(Emotion.ANGER, 20.00f, false),
            EmotionResult(Emotion.ANGER, 20.00f, false),
            )
    }
}