package konkuk.gdsc.memotion.ui.diary.dialog

import android.os.Bundle
import android.util.Log
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
import konkuk.gdsc.memotion.util.TAG

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
        Log.d(TAG, "FragmentEmotionSelect - onCreateView() called")
        _binding = FragmentEmotionSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "FragmentEmotionSelect - onViewCreated() called")
        binding.apply {
            llEmotionSelectBackground.setOnClickListener {}

            btnEmotionSelect.setOnClickListener {
                emotinoResult.value?.let { btnSelect(it) }
            }

            rbEmotion1.setOnClickListener {
                rgEmotionSelect2.clearCheck()
                emotinoResult.value = getEmotionResult(it.id)
            }
            rbEmotion2.setOnClickListener {
                rgEmotionSelect2.clearCheck()
                emotinoResult.value = getEmotionResult(it.id)
            }
            rbEmotion3.setOnClickListener {
                rgEmotionSelect2.clearCheck()
                emotinoResult.value = getEmotionResult(it.id)
            }
            rbEmotion4.setOnClickListener {
                emotinoResult.value = getEmotionResult(it.id)
                rgEmotionSelect2.clearCheck()
            }
            rbEmotion5.setOnClickListener {
                emotinoResult.value = getEmotionResult(it.id)
                rgEmotionSelect1.clearCheck()
            }
            rbEmotion6.setOnClickListener {
                emotinoResult.value = getEmotionResult(it.id)
                rgEmotionSelect1.clearCheck()
            }
            rbEmotion7.setOnClickListener {
                emotinoResult.value = getEmotionResult(it.id)
                rgEmotionSelect1.clearCheck()
            }

        }

        val observer = Observer<Emotion> {
            binding.btnEmotionSelect.isEnabled = true
        }
        emotinoResult.observe(viewLifecycleOwner, observer)
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