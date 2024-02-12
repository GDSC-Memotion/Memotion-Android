package konkuk.gdsc.memotion.domain.entity.emotion

data class EmotionResult(
    val emotion: Emotion,
    val percentage: Float,
    val isTitle: Boolean,
) {
    companion object {
        val sample: List<EmotionResult> = listOf(
            EmotionResult(Emotion.ANGER, 20.00f, true),
            EmotionResult(Emotion.DISGUST, 20.00f, false),
            EmotionResult(Emotion.FEAR, 20.00f, false),
            EmotionResult(Emotion.JOY, 20.00f, false),
            EmotionResult(Emotion.NEUTRAL, 20.00f, false),
            EmotionResult(Emotion.SADNESS, 20.00f, false),
            EmotionResult(Emotion.SURPRISE, 20.00f, false),
        )
    }
}