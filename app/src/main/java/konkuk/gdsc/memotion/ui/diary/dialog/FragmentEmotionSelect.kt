package konkuk.gdsc.memotion.ui.diary.dialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.databinding.FragmentEmotionSelectBinding
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import konkuk.gdsc.memotion.util.TAG

class FragmentEmotionSelect(
    private val btnSelect: (emotion: Emotion) -> Unit
) : Fragment() {

    private var _binding: FragmentEmotionSelectBinding? = null
    private val binding: FragmentEmotionSelectBinding
        get() = requireNotNull(_binding) { "FragmentEmotionSelect's binding is null" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
                btnSelect(Emotion.ANGER)
            }
        }

        /*binding.rgEmotionSelect1.setOnCheckedChangeListener { radioGroup, i ->
            binding.rgEmotionSelect2.clearCheck()
            binding.rgEmotionSelect3.clearCheck()
        }
        binding.rgEmotionSelect2.setOnCheckedChangeListener { radioGroup, i ->
            binding.rgEmotionSelect1.clearCheck()
            binding.rgEmotionSelect3.clearCheck()
        }
        binding.rgEmotionSelect3.setOnCheckedChangeListener { radioGroup, i ->
            binding.rgEmotionSelect1.clearCheck()
            binding.rgEmotionSelect2.clearCheck()
        }

        val observer = Observer<Int>{ id ->
            Log.d(TAG, "FragmentEmotionSelect - observer 활성화\nid:$id")
            when(id){
                R.id.rb_emotion_1, R.id.rb_emotion_2, R.id.rb_emotion_3 -> {
                    binding.rgEmotionSelect2.clearCheck()
                    binding.rgEmotionSelect3.clearCheck()
                }
                R.id.rb_emotion_4, R.id.rb_emotion_5, R.id.rb_emotion_6 -> {
                    binding.rgEmotionSelect1.clearCheck()
                    binding.rgEmotionSelect3.clearCheck()
                }
                R.id.rb_emotion_6-> {
                    binding.rgEmotionSelect1.clearCheck()
                    binding.rgEmotionSelect2.clearCheck()
                }
            }
        }
        viewModel.currentId.observe(viewLifecycleOwner, observer)*/
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}