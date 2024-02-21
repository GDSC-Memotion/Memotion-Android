package konkuk.gdsc.memotion.ui.diary.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.databinding.FragmentEmotionSelectBinding
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryViewModel

@AndroidEntryPoint
class FragmentEmotionSelect(
    private val btnSelect: (emotion: Emotion) -> Unit
) : Fragment() {

    private var _binding: FragmentEmotionSelectBinding? = null
    private val binding: FragmentEmotionSelectBinding
        get() = requireNotNull(_binding) { "FragmentEmotionSelect's binding is null" }

    private val emotinoResult: MutableLiveData<Emotion> = MutableLiveData()
    private val viewModel: WritingDiaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emotionResultObserver = Observer<Emotion> {
            when (it) {
                Emotion.ANGER -> binding.rbEmotion1.isChecked = true
                Emotion.DISGUST -> binding.rbEmotion2.isChecked = true
                Emotion.FEAR -> binding.rbEmotion3.isChecked = true
                Emotion.JOY -> binding.rbEmotion4.isChecked = true
                Emotion.NEUTRAL -> binding.rbEmotion5.isChecked = true
                Emotion.SADNESS -> binding.rbEmotion6.isChecked = true
                Emotion.SURPRISE -> binding.rbEmotion7.isChecked = true
            }
            binding.btnEmotionSelect.isEnabled = true
        }
        viewModel.emotionResult.observe(viewLifecycleOwner, emotionResultObserver)

        binding.apply {
            llEmotionSelectBackground.setOnClickListener {}

            btnEmotionSelect.setOnClickListener {
//                viewModel.setEmotionResult(emotinoResult.value ?: Emotion.ANGER)
                btnSelect(Emotion.ANGER)
//                emotinoResult.value?.let { btnSelect(it) }
            }

            rbEmotion1.setOnClickListener {
                rgEmotionSelect2.clearCheck()
                viewModel.setEmotionResult(getEmotionResult(it.id))
            }
            rbEmotion2.setOnClickListener {
                rgEmotionSelect2.clearCheck()
                viewModel.setEmotionResult(getEmotionResult(it.id))
            }
            rbEmotion3.setOnClickListener {
                rgEmotionSelect2.clearCheck()
                viewModel.setEmotionResult(getEmotionResult(it.id))
            }
            rbEmotion4.setOnClickListener {
                viewModel.setEmotionResult(getEmotionResult(it.id))
                rgEmotionSelect2.clearCheck()
            }
            rbEmotion5.setOnClickListener {
                viewModel.setEmotionResult(getEmotionResult(it.id))
                rgEmotionSelect1.clearCheck()
            }
            rbEmotion6.setOnClickListener {
                viewModel.setEmotionResult(getEmotionResult(it.id))
                rgEmotionSelect1.clearCheck()
            }
            rbEmotion7.setOnClickListener {
                viewModel.setEmotionResult(getEmotionResult(it.id))
                rgEmotionSelect1.clearCheck()
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun getEmotionResult(@IdRes id: Int): Emotion {
        return when (id) {
            R.id.rb_emotion_2 -> Emotion.DISGUST
            R.id.rb_emotion_3 -> Emotion.FEAR
            R.id.rb_emotion_4 -> Emotion.JOY
            R.id.rb_emotion_5 -> Emotion.NEUTRAL
            R.id.rb_emotion_6 -> Emotion.SADNESS
            R.id.rb_emotion_7 -> Emotion.SURPRISE
            else -> Emotion.ANGER
        }
    }
}