package konkuk.gdsc.memotion.ui.diary.dialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.databinding.FragmentEmotionCheckBinding
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryViewModel
import konkuk.gdsc.memotion.util.TAG

@AndroidEntryPoint
class FragmentEmotionCheck(
    private val btnNo: () -> Unit,
    private val btnYes: () -> Unit,
) : Fragment() {

    private var _binding: FragmentEmotionCheckBinding? = null
    private val binding: FragmentEmotionCheckBinding
        get() = requireNotNull(_binding) { "FragmentEmotionCheck's binding is null" }
    private val viewModel: WritingDiaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "FragmentEmotionCheck - onCreateView() called")
        _binding = FragmentEmotionCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "FragmentEmotionCheck - onViewCreated() called")
        binding.apply {
            llEmotionCheckBackground.setOnClickListener { }

            btnEmotionCheckNo.setOnClickListener {
                btnNo()
            }
            btnEmotionCheckYes.setOnClickListener {
                btnYes()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}